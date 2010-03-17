require "/sdcard/jruby/ruboto.rb"

include_class "android.os.SystemClock"
include_class "android.view.Gravity"

class RubotoActivity
  @@lists = {
#    :main      => %w(App Content Graphics Media OS Text Views),
    :main      => %w(Views),
    "App"      => ["Activity", "Alarm", "Dialog" "Intents", 
                     "Launcher Shortcuts", "Menus", "Notification", 
                     "Preferences", "Search", "Service", "Voice Recognition"],
    "Content"  => %w(Assests Resources),
    "Graphics" => ["AlphaBitmap", "AnimatedDrawables", "Arcs", "BitmapDecode", 
                     "BitmapMesh", "BitmapPixles", "CameraPreview", "Clipping", 
                     "ColorMatrix", "Compass", "Density", "Drawable", 
                     "FingerPaint", "Layers", "MeasureText", "OpenGL ES",
                     "PathEffects", "PathFillTypes", "Patterns", "Pictures",
                     "Points", "PolyToPoly", "PurgeableBitmap", "Regions",
                     "RoundRects", "ScaleToFit", "SensorTest", 
                     "SurfaceView Overlay", "Sweep", "Text Align", "Touch Paint", 
                     "Typefaces", "UnicodeChart", "Verticies", "Xfermodes"],
    "Media"    => nil,
    "OS"       => nil,
    "Text"     => nil,
#    "Views"    => ["Animation", "Auto Complete", "Buttons", "Chronometer", 
#                     "Controls", "Custom", "Date Widgets", "Expandable Lists", 
#                     "Focus", "Gallery", "ImageButton", "ImageSwitcher", 
#                     "ImageView", "Layout Animation", "Layouts", "Lists", 
#                     "Progress Bar", "Radio Group", "Rating Bar", "ScrollBars", 
#                     "Seek Bar", "Spinner", "Tabs", "TextSwitcher", "Visibility", 
#                     "WebView"],
    "Views"    => ["Buttons", "Chronometer", "Date Widgets"],
    "Animation"=> ["3D Transition", "Interpolators", "Push", "Shake"],


    "Date Widgets" => ["1. Dialog", "2. Inline"], 
  }

  def self.resolve_click(context, title)
    if @@lists[title]
      RubotoActivity.launch_list context, "$sl_#{title.downcase.gsub(' ', '_')}", "Api Dems", title
    else
      case title
      when "Buttons"     : buttons(context)
      when "Chronometer" : chronometer_demo(context)
      when "1. Dialog"   : date_dialog(context)
      when "2. Inline"   : date_inline(context)
      else
        context.toast "Not Implemented Yet"
      end
    end
  end

  def self.launch_list(context, var, title, list_id)
    context.start_ruboto_activity var do
      setTitle title
      setup_content {list_view :list => @@lists[list_id]}
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

  def self.chronometer_demo(context)
    context.start_ruboto_activity "$chronometer_demo" do
      setTitle "Views/Chronometer"
      setup_content do
        linear_layout :orientation => LinearLayout::VERTICAL, 
                      :gravity => Gravity::CENTER_HORIZONTAL do
          [@c = chronometer(:format => "Initial format: %s"),
          button(:text => "Start"),
          button(:text => "Stop"),
          button(:text => "Reset"),
          button(:text => "Set format string"),
          button(:text => "Clear format string")
          ].each {|i| i.getLayoutParams.width = ViewGroup::LayoutParams::WRAP_CONTENT}
          @c.setPadding(0,30,0,30)
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
        ll = linear_layout :orientation => LinearLayout::VERTICAL do
          nb = button :text => "Normal"
          @sb = button :text => "Small"
          tb = toggle_button
          [nb, @sb, tb].each {|i| i.getLayoutParams.width = ViewGroup::LayoutParams::WRAP_CONTENT}
        end
        lp = @sb.getLayoutParams
        lp.height = ViewGroup::LayoutParams::WRAP_CONTENT
        @sb.setLayoutParams lp
        @sb.setPadding(8,0,8,0)
        ll
      end
    end
  end

  def self.date_dialog(context)
    context.start_ruboto_activity "$date_dialog" do
      setTitle "Views/Date Widgets/1. Dialog"
      setup_content do
        linear_layout :orientation => LinearLayout::VERTICAL do
          @time  = Time.now
          @current_date = @time.strftime("%m-%d-%Y")
          @current_time = @time.strftime("%R")
          @tv = text_view :text => "#{@current_date} #{@current_time}"
          bd = button :text => "change the date"
          bd.getLayoutParams.width = ViewGroup::LayoutParams::WRAP_CONTENT
          bt = button :text => "change the time"
          bt.getLayoutParams.width = ViewGroup::LayoutParams::WRAP_CONTENT
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
        @current_date = "%d-%d-%d" % [month+1, day, year]
        @tv.setText("#{@current_date} #{@current_time}")
      end

      handle_time_set do |view, hour, minute|
        @current_time = "%02d:%02d" % [hour, minute]
        @tv.setText("#{@current_date} #{@current_time}")
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

RubotoActivity.launch_list $activity, "$main_list", "Api Dems", :main
