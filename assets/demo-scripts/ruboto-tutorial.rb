#######################################################
#
# ruboto_tutorial.rb (by Scott Moyer)
# 
# Walks through some of the core concepts of Ruboto
#
#######################################################

require "/sdcard/jruby/ruboto.rb"
confirm_ruboto_version(2)

require 'stringio'

$options = {
#   :main => ["About Ruboto", "Basics", "Understanding the Divide", "Enter Ruboto", 
#             "Advanced Ruboto", "Launching Scripts"],
   :main => ["About Ruboto", "Basics"],
   "Basics" => ["Basic Ruby", "Basic JRuby", "Basic Android"],

   "About Ruboto" => {
     :text => "Ruboto is a small Android wrapper that embeds JRuby. At launch time, the Android application starts up an instance of the JRuby interpreter. The Android application can then execute Ruby scripts and those Ruby scripts can access Java and Android classes.

Even with JRuby embedded within an Android application, there are several challenges to making the full functionality of Android available through scripts. We'll discuss the source of these challenges later. For now it's just important to point out that a second function of the Android wrapper is to workaround these challenges to open up the maximum amount of the Android API possible.

Finally, Ruboto provides the optional ruboto.rb script which uses the power of Ruby and JRuby to encapsulate and simplify access to the Android API (especially the Ruboto workarounds)."
   },
 
   "Basic Ruby" => {
     :text => "Ruboto gives you access to the Ruby language through the JRuby implementation. You can use Ruby classes (e.g., Date, Net::HTTP) instead of using similar classes from Java or Android. 

Note: Parse times can be long currently, especially on first generation hardware. We haven't yet taken steps to optimize Ruboto for the limitations of handsets. The first time you run scripts you will see a long delay before the results are produced.

This code sample loads and uses Date and Net:HTTP. Give it time to load...", 
     :source => ['require "date"', "d = Date.today", "d.to_s",
                 'require "net/http"', 'r = Net::HTTP.get_response("www.google.com", "/")',
                 'r.code == "200" ? r.body : "Page load failed"']
   },

   "Basic JRuby" => {
     :text => "JRuby give you the ability to load an manipulate Java classes. You can even reopen those classes and extend them. Additions to classes are available in JRuby only. The code sample here creates a new Java sting and then reopens the class to implement a reverse method.", 
     :source => ['java_import "java.lang.String"', 
                 's = String.new("Hello, Java String")', 
                 's.class', 's', 's.to_s', 
                 '# This next call should fail (unless you run a second time)', 
                 "begin\n  s.reverse\nrescue\n  \"Failed\"\nend",
                 '# Jruby allows you to reopen Java classes', 
                 "class Java::JavaLang::String\n  def reverse;to_s.reverse.to_java_string;end\nend",
                 "puts s.reverse"]
   },

   "Basic Android" => {
     :text => "The Android API is primarily implemented in Java, so you can use JRuby to load and manipulate much of the Android API. We'll talk more about what parts of Android can and can't be manipulated later. This code sample gets the Android vibrator service and vibrates SOS in Morse Code.", 
     :source => ['include_class "android.content.Context"', 
                 "# Get the vibrator service", 
                 'v = getSystemService(Context::VIBRATOR_SERVICE)',
                 "# setup vibration durations (on, off) for dot and dash", 
                 'dot = [100, 100]', 'dash = [300, 100]', 'pause = [0, 200]', 
                 "# setup S (...) and O (---)", 
                 's = [dot, dot, dot, pause]', 'o = [dash, dash, dash, pause]', 
                 "# convert array to a Java array of longs", 
                 'v.vibrate([0, s, o, s].flatten.to_java(:long), -1)'] 
   },
}

def launch_list(context, remote_variable, title, list, extra_text=nil)
  context.start_ruboto_activity(remote_variable) do
    setTitle title

      setup_content do
        linear_layout :orientation => LinearLayout::VERTICAL do
          text_view(:text => extra_text) if extra_text
          list_view :list => list
        end
      end
  
    handle_item_click do |adapter_view, view, pos, item_id| 
      if $options[list[pos]].is_a?(Array)
        launch_list(self, "$sub_menu", list[pos], $options[list[pos]])
      elsif $options[list[pos]].is_a?(Hash) 
        launch_tutorial(self, list[pos], 
          $options[list[pos]][:text], $options[list[pos]][:source])
      elsif $options[list[pos]].nil? 
        toast("'#{list[pos]}' not yet implemented")
      end
    end
  end
end

def launch_tutorial(context, title, text, source)
  context.start_ruboto_activity("$tutorial") do
    setTitle title

    setup_content do
      linear_layout(:orientation => LinearLayout::VERTICAL) do
        scroll_view do
          text_view :vertical_scroll_bar_enabled => true, :text => text
        end
        if source
          linear_layout do
            button :text => "Run Code"
            button :text => "View Code"
          end
        end
      end
    end
  
    handle_click do |view| 
      if view.getText == "View Code"
        view_source(self, "#{title} - View source", source)
      else
        run_source(self, "#{title} - Run source", source)
      end
    end
  end
end

def run_source(context, title, source)
  context.start_ruboto_activity("$run_source") do
    setTitle title

    source = [source] unless source.is_a?(Array)
    s = source.map {|i| ["puts '>> #{i}'", 
                         "r_v = 'NIL'", 
                         "r_v = #{i}", 
                         'puts "=> #{r_v.inspect}" unless r_v == "NIL"']}.flatten.join("\n")

    old_out = $stdout
    new_out = $stdout = StringIO.new
    eval("(lambda{#{s}}).call")
    $stdout = old_out

    setup_content do
      scroll_view do
        @tv = text_view :vertical_scroll_bar_enabled => true, :text => new_out.string
      end
    end
  end
end

def view_source(context, title, source)
  context.start_ruboto_activity("$source") do
    setTitle title

    source = source.join("\n") if source.is_a?(Array)

    setup_content do
      scroll_view do
        text_view :vertical_scroll_bar_enabled => true, :text => source
      end
    end
  end
end

launch_list($activity, "$main_options", "Ruboto Tutorial", $options[:main],
  "This is a (work in progress) script to teach and demo the basic functionality of Ruboto.")
