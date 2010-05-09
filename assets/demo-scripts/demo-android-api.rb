#######################################################
#
# demo-android-api.rb (by Scott Moyer)
# 
# Attempt to use Ruboto to implement as many of the 
# standard Android API Demos as possible.
#
#######################################################

require "ruboto.rb"
confirm_ruboto_version(3)

ruboto_import_widgets :LinearLayout, :TextView, 
  :Chronometer, :DatePicker, :TimePicker, :EditText, :ToggleButton

java_import "android.os.SystemClock"
java_import "android.view.Window"
java_import "android.view.Gravity"
java_import "android.content.Context"
java_import "android.util.AttributeSet"
java_import "android.graphics.drawable.GradientDrawable"
java_import "android.graphics.Color"
java_import "android.app.TimePickerDialog"
java_import "android.app.DatePickerDialog"

class RubotoActivity
  @@lists = {
#    :main      => %w(App Content Graphics Media OS Text Views),
    :main       => %w(App OS Views),
#    "App"       => ["Activity", "Alarm", "Dialog" "Intents", 
#                     "Launcher Shortcuts", "Menus", "Notification", 
#                     "Preferences", "Search", "Service", "Voice Recognition"],
    "App"       => ["Activity"],
    "Activity"  => ["Custom Dialog"],
    "Content"   => %w(Assests Resources),
    "Graphics"  => ["AlphaBitmap", "AnimatedDrawables", "Arcs", "BitmapDecode", 
                     "BitmapMesh", "BitmapPixles", "CameraPreview", "Clipping", 
                     "ColorMatrix", "Compass", "Density", "Drawable", 
                     "FingerPaint", "Layers", "MeasureText", "OpenGL ES",
                     "PathEffects", "PathFillTypes", "Patterns", "Pictures",
                     "Points", "PolyToPoly", "PurgeableBitmap", "Regions",
                     "RoundRects", "ScaleToFit", "SensorTest", 
                     "SurfaceView Overlay", "Sweep", "Text Align", "Touch Paint", 
                     "Typefaces", "UnicodeChart", "Verticies", "Xfermodes"],
    "Media"     => nil,
    "OS"        => ["Morse Code"],
    "Text"      => nil,
#    "Views"    => ["Animation", "Auto Complete", "Buttons", "Chronometer", 
#                     "Controls", "Custom", "Date Widgets", "Expandable Lists", 
#                     "Focus", "Gallery", "ImageButton", "ImageSwitcher", 
#                     "ImageView", "Layout Animation", "Layouts", "Lists", 
#                     "Progress Bar", "Radio Group", "Rating Bar", "ScrollBars", 
#                     "Seek Bar", "Spinner", "Tabs", "TextSwitcher", "Visibility", 
#                     "WebView"],
    "Views"     => ["Buttons", "Chronometer", "Date Widgets"],
    "Animation" => ["3D Transition", "Interpolators", "Push", "Shake"],
    "Date Widgets" => ["1. Dialog", "2. Inline"], 
  }

  def self.resolve_click(context, title)
    if @@lists[title]
      RubotoActivity.launch_list context, "$sl_#{title.downcase.gsub(' ', '_')}", "Api Demos", title
    else
      case title
      when "Custom Dialog": custom_dialog(context)
      when "Morse Code"   : morse_code(context)
      when "Buttons"      : buttons(context)
      when "Chronometer"  : chronometer_demo(context)
      when "1. Dialog"    : date_dialog(context)
      when "2. Inline"    : date_inline(context)
      else
        context.toast "Not Implemented Yet"
      end
    end
  end

  def self.launch_list(context, var, title, list_id, extra_text=nil)
    context.start_ruboto_activity var do
      setTitle title
      setup_content do
        linear_layout :orientation => LinearLayout::VERTICAL do
          text_view(:text => extra_text) if extra_text
          list_view :list => @@lists[list_id]
        end
      end
      handle_item_click do |adapter_view, view, pos, item_id| 
        RubotoActivity.resolve_click self, view.getText
      end
    end
  end

  def self.blank(context)
    context.start_ruboto_activity "$blank" do
      setTitle "Views/Buttons"
      setup_content do
        linear_layout :orientation => LinearLayout::VERTICAL do
        end
      end
    end
  end

  def self.custom_dialog(context)
    context.start_ruboto_dialog "$custom_dialog" do
      setTitle "App/Activity/Custom Dialog"

      setup_content do
        cd = GradientDrawable.new
        cd.setColor(Color.argb(240,96,0,0))
        cd.setStroke(3, Color.argb(255,255,128,128))
        cd.setCornerRadius(3)
        getWindow.setLayout(ViewGroup::LayoutParams::FILL_PARENT,
                            ViewGroup::LayoutParams::WRAP_CONTENT)
        getWindow.setBackgroundDrawable(cd)
        @lv = linear_layout :orientation => LinearLayout::VERTICAL do
          text_view :text => "Example of how you can use a custom Theme.Dialog theme to make an activity that looks like a customized dialog, here with an ugly frame.", 
            :text_size => 14,
            :gravity => Gravity::CENTER_HORIZONTAL
        end
        @lv.setPadding(10,0,10,10)
        @lv
      end
    end
  end

  def self.morse_code(context)
    context.start_ruboto_activity "$morse_code" do
      @base  = 100
      @durations = {"." => [@base, @base],  "-" => [@base* 3, @base], 
                    "|" => [0, @base * 2],  " " => [0, @base * 6]}
      @codes = {"A" => ".-",   "B" => "-...", "C" => "-.-.", "D" => "-..",
                "E" => ".",    "F" => "..-.", "G" => "--.",  "H" => "....",
                "I" => "..",   "J" => ".---", "K" => "-.-",  "L" => ".-..",
                "M" => "--",   "N" => "-.",   "O" => "---",  "P" => ".--.",
                "Q" => "--.-", "R" => ".-.",  "S" => "...",  "T" => "-",
                "U" => "..-",  "V" => "..-",  "W" => ".--",  "X" => "-..-",
                "Y" => "-.--", "Z" => "--..", "0" => "-----","1" => ".----",
                "2" => "..---","3" => "...--","4" => "....-","5" => ".....",
                "6" => "-....","7" => "--...","8" => "---..","9" => "----."}
      setTitle "OS/Morse Code"
      setup_content do
        linear_layout :orientation => LinearLayout::VERTICAL do
          @et = edit_text
          button :text => "Vibrate", :width => :wrap_content
        end
      end
      handle_click do |view|
        getSystemService(Context::VIBRATOR_SERVICE).vibrate(
          @et.getText.to_s.upcase.split('').
          map {|i| @codes[i] || " "}.join('|').split('').
          map {|i| @durations[i]}.flatten.unshift(0).to_java(:long), -1)
      end
    end
  end

  def self.chronometer_demo(context)
    context.start_ruboto_activity "$chronometer_demo" do
      setTitle "Views/Chronometer"
      setup_content do
        linear_layout :orientation => LinearLayout::VERTICAL, 
                      :gravity => Gravity::CENTER_HORIZONTAL do
          @c = chronometer :format => "Initial format: %s", :width => :wrap_content
          @c.setPadding(0,30,0,30)
          button :text => "Start", :width => :wrap_content
          button :text => "Stop", :width => :wrap_content
          button :text => "Reset", :width => :wrap_content
          button :text => "Set format string", :width => :wrap_content
          button :text => "Clear format string", :width => :wrap_content
        end
      end

      handle_click do |view|
        case view.getText
        when "Start"               : @c.start
        when "Stop"                : @c.stop
        when "Reset"               : @c.setBase SystemClock.elapsedRealtime
        when "Set format string"   : @c.setFormat("Formatted time (%s)")
        when "Clear format string" : @c.setFormat(nil)
        end
      end
    end
  end

  def self.buttons(context)
    context.start_ruboto_activity "$buttons" do
      setTitle "Views/Buttons"
      setup_content do
        linear_layout :orientation => LinearLayout::VERTICAL do
          nb = button :text => "Normal", :width => :wrap_content
          sb = button :text => "Small", :width => :wrap_content
          sb.setPadding(8,0,8,0)
          tb = toggle_button :width => :wrap_content
        end
      end
    end
  end

  def self.date_dialog(context)
    context.start_ruboto_activity "$date_dialog" do
      setTitle "Views/Date Widgets/1. Dialog"
      setup_content do
        linear_layout :orientation => LinearLayout::VERTICAL do
          @time  = Time.now
          @tv = text_view :text => @time.strftime("%m-%d-%Y %R")
          button :text => "change the date", :width => :wrap_content
          button :text => "change the time", :width => :wrap_content
        end
      end

      handle_click do |view|
        showDialog(view.getText == "change the time" ? 1 : 0)
      end

      handle_create_dialog do |dialog_id|
        if dialog_id == 1
          setScriptReturnObject TimePickerDialog.new(self, self, 
            @time.hour, @time.min, false)
        else
          setScriptReturnObject DatePickerDialog.new(self, self, 
            @time.year, @time.month-1, @time.day)
        end
      end

      handle_date_set do |view, year, month, day|
        @tv.setText("%d-%d-%d #{@tv.getText.split(' ')[1]}" % [month+1, day, year])
      end

      handle_time_set do |view, hour, minute|
        @tv.setText("#{@tv.getText.split(' ')[0]} %02d:%02d" % [hour, minute])
      end
    end
  end

  def self.date_inline(context)
    context.start_ruboto_activity "$date_inline" do
      setTitle "Views/Date Widgets/#{title}"
      setup_content do
        linear_layout do
          @dp = time_picker :on_time_changed_listener => self, 
                            :current_hour => 12, :current_minute=> 15
          @tv = text_view :text => "12:15"
        end
      end

      handle_time_changed do |view, hour, minute|
        @tv.setText("%02d:%02d" % [hour, minute]) if @tv
      end
    end
  end
end

RubotoActivity.launch_list $activity, "$main_list", "Api Demos", :main,
  "This is a Ruboto demo that attempts to duplicate the standard Android API Demo using Ruboto. It is in the early stages (more samples will be completed in the future)."
