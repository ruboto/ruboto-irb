require 'rake/clean'

stdlib = File.join(File.dirname(__FILE__), "libs/jruby-stdlib.jar")
stdlib_precompiled = File.join(File.dirname(__FILE__), "libs/jruby-stdlib-precompiled.jar")
dirs = ['tmp/ruby', 'tmp/precompiled'] 

CLEAN << 'tmp'
dirs.each { |d| directory d }

file stdlib_precompiled => :compile_stdlib

desc "precompile ruby stdlib"
task :compile_stdlib => [:clean, *dirs] do
  Dir.chdir('tmp/ruby') { 
    sh "jar xfv #{stdlib}" 
    sh "jrubyc . -t ../precompiled"
  }
  sh "jar cfv libs/jruby-stdlib-precompiled.jar -C tmp/precompiled ."
end
