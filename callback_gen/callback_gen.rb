# callback_reflection.rb creates the interfaces.txt (JRuby can't do YAML with ruby 1.8, so it's just
# and inspect on the hash) on a device. Bring it off the device and put it in the callback_gen dir.
#
# Move this into a rake task later.
#

require 'erb'

@callbacks = eval(IO.read("callback_gen/interfaces.txt"))
File.open("src/org/ruboto/embedded/RubotoActivity.java", "w") do |file|
	file.write ERB.new(IO.read("callback_gen/RubotoActivity.erb"), 0, "%").result
end
