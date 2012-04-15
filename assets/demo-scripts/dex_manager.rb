#######################################################
#
# dex_manager.rb (by Scott Moyer)
# 
# This script manages the dex cache (jars created
# through ruboto_generate). It can clear individual
# jars (also clearing the constant) or the entire cache.
#
#######################################################

class DexManager
  @dex_root = "#{$activity.files_dir.absolute_path}/dx"
  
  def self.root
    @dex_root
  end

  def self.clear_optimized_dex
    dex_files.each{|i| File.delete i}
  end

  def self.clear_files
    clear_optimized_dex
    jar_files.each{|i| File.delete i}
  end

  def self.files_of_type(type)
    return [] unless File.exists?(@dex_root)
    current_dir = Dir.pwd
    Dir.chdir(@dex_root)
    rv = Dir.glob("**/*.#{type}")
    Dir.chdir(current_dir)
    rv.map{|i| "#{@dex_root}/#{i}"}
  end

  def self.dex_files
    files_of_type("dex")
  end

  def self.jar_files
    files_of_type("jar")
  end
  
  def self.file_size
    dex_size + jar_size
  end

  def self.dex_size
    dex_files.inject(0){|size, i| size += File.size i}
  end

  def self.jar_size
    jar_files.inject(0){|size, i| size += File.size i}
  end

  def self.size_of(file)
    size = 0
    size += File.size(file) if File.exists?(file)
    file = file[-3..-1] == "jar" ? file.gsub(/jar$/, "dex") : file.gsub(/dex$/, "jar")
    size += File.size(file) if File.exists?(file)
    size
  end
end

#######################################################

require 'ruboto/activity'
require 'ruboto/widget'
require 'ruboto/util/toast'

ruboto_import_widgets :ListView

$activity.start_ruboto_activity "$dex_list" do
  setTitle "Manage Generated Classes"

  def on_create(bundle)
    build_jars_list
    setContentView(
      @list_view = list_view(:list => @list, :on_item_click_listener => @handle_item_click)
    )
  end
  
  def build_jars_list
    @jar_list = DexManager.jar_files
    @list = ["Delete All (#{DexManager.file_size} bytes)", 
             "Delete Optimized Dex Files (#{DexManager.dex_size} bytes)"] + 
            @jar_list.map{|i| "Delete #{i.split('/')[-1][0..-5]} (#{DexManager.size_of i} bytes)"}
  end

  @handle_item_click = Proc.new do |adapter_view, view, pos, item_id| 
    case view.text.to_s[7..9]
    when "All"
      @jar_list.each do |i|
        const = i.split('/')[-1][0..-5]
        Object.class_eval{remove_const(const)} if Object.const_defined?(const)
      end
      DexManager.clear_files
    when "Opt"
      DexManager.clear_optimized_dex
    else
      const = @jar_list[pos-2].split('/')[-1][0..-5]
      Object.class_eval{remove_const(const)} if Object.const_defined?(const)
      File.delete @jar_list[pos-2]
      dex = @jar_list[pos-2].gsub(/jar$/, "dex")
      File.delete(dex) if File.exists?(dex)
    end

    build_jars_list
    @list_view.reload_list(@list)
    toast view.text.to_s  
  end
end

