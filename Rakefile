raise "Needs JRuby 1.5" unless RUBY_PLATFORM =~ /java/
require 'ant'
require 'rake/clean'

generated_libs     = 'generated_libs'
stdlib             = 'libs/jruby-stdlib.jar'
jruby_jar          = 'libs/jruby.jar'
stdlib_precompiled = File.join(generated_libs, 'jruby-stdlib-precompiled.jar')
jruby_ruboto_jar   = File.join(generated_libs, 'jruby-ruboto.jar')
ant.property :name=>'external.libs.dir', :value => generated_libs
dirs = ['tmp/ruby', 'tmp/precompiled', generated_libs]
dirs.each { |d| directory d }

CLEAN.include('tmp', 'bin', generated_libs)

ant_import

file stdlib_precompiled => :compile_stdlib
file jruby_ruboto_jar => generated_libs do
  ant.zip(:destfile=>jruby_ruboto_jar) do
    zipfileset(:src=>jruby_jar) do
      exclude(:name=>'jni/**')
    end
  end
end

desc "precompile ruby stdlib"
task :compile_stdlib => [:clean, *dirs] do
  ant.unzip(:src=>stdlib, :dest=>'tmp/ruby')
  Dir.chdir('tmp/ruby') { sh "jrubyc . -t ../precompiled" }
  ant.zip(:destfile=>stdlib_precompiled, :basedir=>'tmp/precompiled')
end

task :generate_libs => [generated_libs, jruby_ruboto_jar] do
  cp stdlib, generated_libs
end

task :debug   => :generate_libs
task :release => :generate_libs

task :default => :debug
