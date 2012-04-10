######################################################
#
# generate.rb (by Scott Moyer)
#
# Uses the dexmaker project 
# (http://code.google.com/p/dexmaker/)
# to generate Ruboto callbacks.
#
######################################################

require 'ruboto/base'
require 'fileutils'

#
# Expand the functionality of TypeId
#

java_import 'com.google.dexmaker.TypeId'
class TypeId
  @@convert_hash = {
    nil => VOID,
    "java.lang.String" => STRING,
    "java.lang.Object" => OBJECT
  }

  @@corresponding_class = {
    INT     => get("Ljava/lang/Integer;"),
    FLOAT   => get("Ljava/lang/Float;"),
    DOUBLE  => get("Ljava/lang/Double;"),
    BYTE    => get("Ljava/lang/Byte;"),
    BOOLEAN => get("Ljava/lang/Boolean;"),
    CHAR    => get("Ljava/lang/Char;"),
    SHORT   => get("Ljava/lang/Short;"),
    LONG    => get("Ljava/lang/Long;")
  }

  @@conversion_method = {}

  %w(int float double byte boolean char short long).each do |i|
    @@convert_hash[i] = const_get(i.upcase)
    @@conversion_method[const_get(i.upcase)] = "#{i}Value"
  end

  def self.convert_type(type)
    # TODO: Handling arrays
    rv = @@convert_hash[type]
    unless rv
      rv = type.split("[")
      rv[-1] = "L#{rv[-1].gsub('.', '/')};" unless rv[-1].length == 1
      rv = get(rv.join("["))
    end
    rv
  end

  def corresponding_class
    @@corresponding_class[self]
  end

  def conversion_method
    @@conversion_method[self]
  end

  def primitive?
    @@corresponding_class.key? self
  end
end

######################################################
#
# Helper methods for class generation
#

java_import 'com.google.dexmaker.Code'
class Code
  def call_super(class_id, method_name, return_value, *parameters)
    method_id = class_id.getMethod(return_value ? return_value.type : TypeId::VOID,
                                    method_name, *(parameters.map{|i| i.type}))
    invokeSuper(method_id, return_value, getThis(class_id), *parameters)
  end
end
  
def create_constructor(dex_maker, super_class, class_id, *parameters)
  constructor_id = class_id.getConstructor(*parameters)
  dex_maker.declare(constructor_id, java.lang.reflect.Modifier::PUBLIC).instance_eval do
    parameter_array = []
    parameters.each_with_index{|param, i| parameter_array << getParameter(i, param)}

    invokeDirect(super_class.getConstructor(*parameters), nil, getThis(class_id), *parameter_array)
    returnVoid
  end
end

######################################################
#
# Generate a new class for an interface
#

def ruboto_clear_dex_cache
  FileUtils.remove_dir "#{$activity.files_dir.absolute_path}/dx"
end

def ruboto_generate(package_class_name, interface_or_class_name, options={})
  options = {
    :use_cache => true,
    :reload => false
  }.merge(options)
  
  #
  # Already loaded?
  #

  class_name = package_class_name.split('.')[-1]
  return Object.const_get(class_name) if Object.const_defined?(class_name) and not options[:reload]
  
  #
  # Set up directory
  #

  base_dir = "#{$activity.files_dir.absolute_path}/dx"
  package_dir = "#{base_dir}/#{package_class_name.split('.')[0..-2].join('/')}"
  FileUtils.mkpath package_dir unless File.exists?(package_dir)
  jar_file = java.io.File.new("#{package_dir}/#{class_name}.jar")
  puts "Exists: #{jar_file}" if File.exists?(jar_file.to_s)

  #
  # Already generated?
  #

  if options[:use_cache]
    rv = ruboto_load_class(jar_file.path, package_class_name)
    return rv if rv
  end

  if File.exists? jar_file.path
    File.delete jar_file.path
    File.delete jar_file.path.gsub(/\.jar$/, ".dex")
  end

  puts "Generating: #{jar_file.path}"

  #
  # Basic set up
  #

  dex_maker = com.google.dexmaker.DexMaker.new

  if interface_or_class_name.is_a?(String)
    interface_or_class = eval("Java::#{interface_or_class_name.gsub('$', '::')}")
  else
    interface_or_class = interface_or_class_name
    interface_or_class_name = interface_or_class.java_class.name 
  end
  interface_or_class_id = TypeId.convert_type(interface_or_class_name)
  interface =  interface_or_class.java_class.interface?
  class_type_id = TypeId.convert_type(package_class_name)
  parameters = [class_type_id, "#{package_class_name.split('.')[-1]}.generated", java.lang.reflect.Modifier::PUBLIC,
                  interface ? TypeId::OBJECT : interface_or_class_id]
  parameters << TypeId.convert_type(interface_or_class_name) if interface
  dex_maker.declare(*parameters)

  #
  # Create callbacks field
  #

  callbackProcs_field = class_type_id.getField(TypeId.get("[Ljava/lang/Object;"), "callbackProcs")
  dex_maker.declare(callbackProcs_field, java.lang.reflect.Modifier::PRIVATE, nil)

  #
  # Build constructor and create callbacks array
  #

  if interface
    create_constructor dex_maker, TypeId::OBJECT, class_type_id
  else
    interface_or_class.java_class.constructors.each do |c|
      parameter_type_array = c.parameter_types.map{|p| TypeId.convert_type(p.name)}
      create_constructor dex_maker, interface_or_class_id, class_type_id, *parameter_type_array
    end
  end

  #
  # Build a list of methods
  #

  methods = []
  if interface
    methods = interface_or_class.java_class.declared_instance_methods
  else
    method_hash, klass = {}, interface_or_class
    while klass != nil
      klass.java_class.declared_instance_methods.each do |i|
        if (i.name[0..1] == "on" or (i.modifiers & java.lang.reflect.Modifier::ABSTRACT) != 0) and 
           (i.modifiers & java.lang.reflect.Modifier::FINAL) == 0 and not method_hash[i.name]
          method_hash[i.name] = i
          methods << i
        end
      end
      klass = klass == java.lang.Object ? nil : klass.superclass
    end
  end

  #
  # Build setCallbackProc method
  #

  method_id = class_type_id.getMethod(TypeId::VOID, "setCallbackProc", TypeId::INT, TypeId::OBJECT)
  dex_maker.declare(method_id, java.lang.reflect.Modifier::PUBLIC).instance_eval do
    index = getParameter(0, TypeId::INT)
    block = getParameter(1, TypeId::OBJECT)
    array = newLocal(TypeId.get("[Ljava/lang/Object;"))
    size = newLocal(TypeId::INT)
    null = newLocal(TypeId::OBJECT)

    array_exists = com.google.dexmaker.Label.new
      
    # Does the calback proc array exist yet?
    iget(callbackProcs_field, array, getThis(class_type_id))
    loadConstant(null, nil)
    compare(com.google.dexmaker.Comparison::NE, array_exists, array, null)
    
    # Create the array the first time
    loadConstant(size, methods.length)
    newArray(array, size)
    iput(callbackProcs_field, getThis(class_type_id), array)    

    mark(array_exists)
    aput(array, index, block)

    returnVoid
  end

  #
  # Loop through and build a constant and method for each method in the interface
  #

  methods.each_with_index do |m, count|
    # Define the constant
    constant_name = "CB_" + m.name.gsub(/[A-Z]/, '_\0').upcase.gsub(/^ON_/, "")
    const = class_type_id.getField(TypeId::INT, constant_name)
    dex_maker.declare(const, 
      java.lang.reflect.Modifier::PUBLIC | java.lang.reflect.Modifier::STATIC | java.lang.reflect.Modifier::FINAL, 
      count.to_java(:int))

    # Build the method
    parameter_type_array = m.parameter_types.map{|j| TypeId.convert_type(j.name)}
    method_id = class_type_id.getMethod(TypeId.convert_type(m.return_type ? m.return_type.name : nil), m.name, *parameter_type_array)
    dex_maker.declare(method_id, java.lang.reflect.Modifier::PUBLIC).instance_eval do
      parameter_array = []
      parameter_type_array.each_with_index{|j, k| parameter_array << getParameter(k, j)}

      # Callback procs array
      index = newLocal(TypeId::INT)
      array = newLocal(TypeId.get("[Ljava/lang/Object;"))
      block = newLocal(TypeId::OBJECT)

      if parameter_array.length > 1
        # Call arguments array
        p_arr = newLocal(TypeId.get("[Ljava/lang/Object;"))
        p_size = newLocal(TypeId::INT)
        p_index = newLocal(TypeId::INT)
      end

      # Holds nil for comparison
      null = newLocal(TypeId::OBJECT)

      # Holds method name
      call_string = newLocal(TypeId::STRING)

      # Locals for possible return
      ret = retObject = nil
      if m.return_type
        ret = newLocal(TypeId.get(m.return_type))
        retObject = newLocal(TypeId::OBJECT)
        retClass = newLocal(TypeId.convert_type("java.lang.Class"))
      end

      # Create a local to help convert primitives
      tmp_locals = {}
      parameter_type_array.each do |p|
        tmp_locals[p] = newLocal(p.corresponding_class) if p.primitive?
      end
      tmp_locals[ret.type] = newLocal(ret.type.corresponding_class) if ret and ret.type.primitive? and tmp_locals[ret.type] == nil

      no_block = com.google.dexmaker.Label.new
      done = com.google.dexmaker.Label.new
      
      # Does the calback proc array exist yet?
      iget(callbackProcs_field, array, getThis(class_type_id))
      loadConstant(null, nil)
      compare(com.google.dexmaker.Comparison::EQ, no_block, array, null)

      # Do we have a callback proc?
      loadConstant(index, count)
      aget(block, array, index)
      compare(com.google.dexmaker.Comparison::EQ, no_block, block, null)

      call_super(class_type_id, m.name, ret, *parameter_array) unless interface

      # Prepare to call Script to call the method
      script_class_type_id = TypeId.convert_type("org.ruboto.Script")
      loadConstant(call_string, "call")
      parameter_types = [ret ? TypeId::OBJECT : TypeId::VOID, "callMethod", TypeId::OBJECT, TypeId::STRING]
      method_parameters = [retObject, block, call_string]
      
      # Set up for different arity
      if parameter_array.length == 1
        parameter_types << TypeId::OBJECT
        # Cast ?
        p = parameter_array[0]
        # Need to convert primitives to add to array
        if p.type.primitive?
          newInstance(tmp_locals[p.type], p.type.corresponding_class.getConstructor(p.type), p)
          method_parameters << tmp_locals[p.type]
        else
          method_parameters << p
        end
      elsif parameter_array.length > 1
        # Create and populate an array for method parameters
        loadConstant(p_size, parameter_type_array.length.to_java(:int))
        newArray(p_arr, p_size)
        parameter_array.each_with_index do |p, i|
          loadConstant(p_index, i)
  
          # Need to convert primitives to add to array
          if p.type.primitive?
            newInstance(tmp_locals[p.type], p.type.corresponding_class.getConstructor(p.type), p)
            aput(p_arr, p_index, tmp_locals[p.type])
          else
            aput(p_arr, p_index, p)
          end
        end
        
        parameter_types << TypeId.get("[Ljava/lang/Object;")
        method_parameters << p_arr
      end
      
      # Add return class to the call 
      if ret
        parameter_types << TypeId.convert_type("java.lang.Class")
        loadConstant(retClass, java.lang.Boolean.java_class)
        method_parameters << retClass
      end
      
      # Make the call 
      method_parameters = [script_class_type_id.getMethod(*parameter_types)] + method_parameters
      invokeStatic(*method_parameters)

      # Cast the return
      if ret and ret.type.primitive?
        cast(tmp_locals[ret.type], retObject)
        invokeVirtual(tmp_locals[ret.type].type.getMethod(ret.type, ret.type.conversion_method), ret, tmp_locals[ret.type])
      elsif ret
        # May need to just copy if type is OBJECT
        cast(ret, retObject)
      end
      
      jump(done)
      mark(no_block)

      call_super(class_type_id, m.name, ret, *parameter_array) unless interface

      mark(done)
      ret ? returnValue(ret) : returnVoid
    end
  end

  #
  # Generate and save
  #

  dex = dex_maker.generate
  jar_file.createNewFile
  jarOut = java.util.jar.JarOutputStream.new(java.io.FileOutputStream.new(jar_file))
  jarOut.putNextEntry(java.util.jar.JarEntry.new("classes.dex"))
  jarOut.write(dex)
  jarOut.closeEntry
  jarOut.close

  return ruboto_load_class(jar_file.path, package_class_name)
end

def ruboto_load_class(file_name, package_class_name)
  return nil unless File.exists? file_name

  loader = Java::dalvik.system.DexClassLoader.new(file_name, file_name.split('/')[0..-2].join('/'), nil,
              com.google.dexmaker.DexMaker.java_class.class_loader)

  runtime = org.jruby.Ruby.getGlobalRuntime
  rv = org.jruby.javasupport.Java.getProxyClass(runtime,
          org.jruby.javasupport.JavaClass.get(runtime, loader.loadClass(package_class_name)))
  Object.const_set(package_class_name.split('.')[-1], rv)
  ruboto_import rv
  rv
end

def ruboto_generate_widget(package_class_name, interface_or_class_name, options={})
  rv = ruboto_generate(package_class_name, interface_or_class_name, options)
  ruboto_import_widget rv
end

