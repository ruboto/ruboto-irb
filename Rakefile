raise "Needs JRuby 1.5" unless RUBY_PLATFORM =~ /java/
require 'ant'
require 'rake/clean'
require 'rexml/document'

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

task :tag => :release do
  unless `git branch` =~ /^\* master$/
    puts "You must be on the master branch to release!"
    exit!
  end
  sh "git commit --allow-empty -a -m 'Release #{version}'"
  sh "git tag #{version}"
  sh "git push origin master --tags"
  #sh "gem push pkg/#{name}-#{version}.gem"
end

def manifest
  @manifest ||= REXML::Document.new(File.read('AndroidManifest.xml'))
end

def strings(name)
  @strings ||= REXML::Document.new(File.read('res/values/strings.xml'))
  value = @strings.elements["//string[@name='#{name.to_s}']"] or raise "string '#{name}' not found in strings.xml"
  value.text
end

def package() manifest.root.attribute('package') end
def version() strings :version_name end
def app_name()  strings :app_name end
