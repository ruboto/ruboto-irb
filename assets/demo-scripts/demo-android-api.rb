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

ruboto_import_widgets :LinearLayout, :TextView, :RelativeLayout,
  :TableLayout, :TableRow,
  :Chronometer, :DatePicker, :TimePicker, :EditText, :ToggleButton

java_import "android.os.SystemClock"
java_import "android.view.Window"
java_import "android.view.WindowManager"
java_import "android.view.Gravity"
java_import "android.content.Context"
java_import "android.util.AttributeSet"
java_import "android.graphics.drawable.GradientDrawable"
java_import "android.graphics.Color"
java_import "android.graphics.Paint"
java_import "android.graphics.RectF"
java_import "android.graphics.Canvas"
java_import "android.graphics.Bitmap"
java_import "android.graphics.Path"
java_import "android.hardware.SensorManager"
java_import "android.hardware.Sensor"
java_import "android.app.TimePickerDialog"
java_import "android.app.DatePickerDialog"
java_import "android.graphics.Typeface"
java_import "android.content.res.ColorStateList"
java_import "org.jruby.ruboto.RubotoView"

module Ruboto
  java_import "org.jruby.ruboto.irb.R"
  Id = JavaUtilities.get_proxy_class("org.jruby.ruboto.irb.R$id")
end

class RubotoActivity
  @@lists = {
#    :main      => %w(App Content Graphics Media OS Text Views),
    :main       => %w(App Graphics OS Views),
#    "App"       => ["Activity", "Alarm", "Dialog" "Intents", 
#                     "Launcher Shortcuts", "Menus", "Notification", 
#                     "Preferences", "Search", "Service", "Voice Recognition"],
    "App"       => ["Activity"],
    "Activity"  => ["Custom Dialog", "Custom Title", "Forwarding", "Hello World", 
                    "Persistent State", "Save & Restore State"],
    "Content"   => %w(Assests Resources),
    "Graphics"  => ["Arcs"],
#    "Graphics"  => ["AlphaBitmap", "AnimatedDrawables", "Arcs", "BitmapDecode", 
#                     "BitmapMesh", "BitmapPixles", "CameraPreview", "Clipping", 
#                     "ColorMatrix", "Compass", "Density", "Drawable", 
#                     "FingerPaint", "Layers", "MeasureText", "OpenGL ES",
#                     "PathEffects", "PathFillTypes", "Patterns", "Pictures",
#                     "Points", "PolyToPoly", "PurgeableBitmap", "Regions",
#                     "RoundRects", "ScaleToFit", "SensorTest", 
#                     "SurfaceView Overlay", "Sweep", "Text Align", "Touch Paint", 
#                     "Typefaces", "UnicodeChart", "Verticies", "Xfermodes"],
    "Media"     => nil,
    "OS"        => ["Morse Code", "Sensors"],
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
      when "Custom Dialog"         : custom_dialog(context)
      when "Custom Title"          : custom_title(context)
      when "Forwarding"            : forwarding(context)
      when "Hello World"           : hello_world(context)
      when "Persistent State"      : persistent_state(context)
      when "Save & Restore State"  : save_and_restore_state(context)
      when "Arcs"                  : arcs(context)
      when "Morse Code"            : morse_code(context)
      when "Sensors"               : sensors(context)
      when "Buttons"               : buttons(context)
      when "Chronometer"           : chronometer_demo(context)
      when "1. Dialog"             : date_dialog(context)
      when "2. Inline"             : date_inline(context)
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

  #######################################################
  #
  # App -> Activity
  #

  #
  # Custom Dialog
  #

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

        linear_layout(:orientation => LinearLayout::VERTICAL, :padding => [10,0,10,10]) do
          text_view :text => "Example of how you can use a custom Theme.Dialog theme to make an activity that looks like a customized dialog, here with an ugly frame.", 
            :text_size => 14,
            :gravity => Gravity::CENTER_HORIZONTAL
        end
      end
    end
  end

  #
  # Custom Dialog
  #

  def self.custom_title(context)
    context.start_ruboto_activity "$custom_title" do
      requestWindowFeature Window::FEATURE_CUSTOM_TITLE

      setup_content do
        linear_layout :orientation => LinearLayout::VERTICAL do
          linear_layout do
            @etl = edit_text(:text => "Left is best", :min_ems => 10, :max_ems => 10)
            button :text => "Change left"
          end
          linear_layout do
            @etr = edit_text(:text => "Right is always right", :min_ems => 10, :max_ems => 10)
            button :text => "Change right"
          end
        end
      end

      handle_create do
        getWindow.setFeatureInt(Window::FEATURE_CUSTOM_TITLE, 
                                Ruboto::R::layout::empty_relative_layout)

        @rl = findViewById(Ruboto::Id::empty_relative_layout)
        @tvl = text_view :text => "Left is best", 
                         :text_color => ColorStateList.valueOf(0xFFFFFFFF),
                         :typeface => [Typeface::DEFAULT, Typeface::BOLD]
        @rl.addView @tvl
        @tvl.getLayoutParams.addRule RelativeLayout::ALIGN_PARENT_LEFT

        @tvr = text_view :text => "Right is always right", 
                         :text_color => ColorStateList.valueOf(0xFFFFFFFF),
                         :typeface => [Typeface::DEFAULT, Typeface::BOLD]
        @rl.addView @tvr
        @tvr.getLayoutParams.addRule RelativeLayout::ALIGN_PARENT_RIGHT
      end

      handle_click do |view|
        view.getText == "Change left" ? @tvl.setText(@etl.getText) : @tvr.setText(@etr.getText)
      end
    end
  end

  #
  # Forwarding
  #

  def self.forwarding(context)
    context.start_ruboto_activity "$forwarding" do
      setTitle "App/Activity/Forwarding"

      setup_content do
        linear_layout :orientation => LinearLayout::VERTICAL do
          text_view :text => "Press the button to go forward to the next activity.  This activity will stop, so you will no longer see it when going back."
          linear_layout(:gravity => Gravity::CENTER_HORIZONTAL) do
            button :text => "Go", :width => :wrap_content
          end
        end
      end

      handle_click do |view|
        context.start_ruboto_activity "$forwarding2" do
          setTitle "App/Activity/Forwarding"
          setup_content do
            text_view :text => "Press back button and notice we don't see the previous activity."
          end
        end
        finish
      end
    end
  end

  #
  # Hello, World
  #

  def self.hello_world(context)
    context.start_ruboto_activity "$hello_world" do
      setTitle "App/Activity/Hello World"

      setup_content do
          text_view :text => "Hello, World!", 
            :gravity => (Gravity::CENTER_HORIZONTAL | Gravity::CENTER_VERTICAL)
      end
    end
  end

  #
  # Persistent State
  #

  def self.persistent_state(context)
    context.start_ruboto_activity "$persistent_state" do
      setTitle "App/Activity/Persistent State"
      getWindow.setSoftInputMode(
                 WindowManager::LayoutParams::SOFT_INPUT_STATE_VISIBLE | 
                 WindowManager::LayoutParams::SOFT_INPUT_ADJUST_RESIZE)

      setup_content do
        linear_layout :orientation => LinearLayout::VERTICAL, :padding => [4,4,4,4] do
          text_view :text => "Demonstration of persistent activity state with getPreferences(0).edit() and getPreferences(0).", :padding => [0,0,0,4]
          text_view :text => "This text field saves its state:", :padding => [0,0,0,4]
          @save = edit_text(:text => "Initial text.", :backgroundColor => 0x7700ff00, 
                            :padding => [0,4,0,4])
          @save.getLayoutParams.weight = 1.0

          text_view :text => "This text field does not save its state:", :padding => [0,8,0,4]
          et = edit_text :text => "Initial text.", :backgroundColor => 0x77ff0000, 
                            :padding => [0,4,0,4]
          et.getLayoutParams.weight = 1.0
        end
      end

      handle_pause do
        editor = getPreferences(0).edit
        editor.putString("text", @save.getText.toString)
        editor.putInt("selection-start", @save.getSelectionStart)
        editor.putInt("selection-end", @save.getSelectionEnd)
        editor.commit
      end

      handle_resume do
        prefs = getPreferences(0) 
        restoredText = prefs.getString("text", nil)
        if (restoredText != nil)
            @save.setText(restoredText, TextView::BufferType::EDITABLE)

            selectionStart = prefs.getInt("selection-start", -1)
            selectionEnd = prefs.getInt("selection-end", -1)
            if (selectionStart != -1 && selectionEnd != -1)
                @save.setSelection(selectionStart, selectionEnd)
            end
        end
      end
    end
  end

  #
  # Save and Restore State
  #

  def self.save_and_restore_state(context)
    context.start_ruboto_activity "$save_and_restore_state" do
      setTitle "App/Activity/Save & Restore State"
      getWindow.setSoftInputMode(
                 WindowManager::LayoutParams::SOFT_INPUT_STATE_VISIBLE | 
                 WindowManager::LayoutParams::SOFT_INPUT_ADJUST_RESIZE)

      setup_content do
        linear_layout :orientation => LinearLayout::VERTICAL, :padding => [4,4,4,4] do
          text_view :text => "Demonstration of saving and restoring activity state in onSaveInstanceState() and onCreate().", :padding => [0,0,0,4]
          text_view :text => "This text field saves its state:", :padding => [0,0,0,4]
          @save = edit_text(:text => "Initial text.", :backgroundColor => 0x7700ff00, 
                            :padding => [0,4,0,4], :freezes_text => true, :id => 55555)
          @save.getLayoutParams.weight = 1.0

          text_view :text => "This text field does not save its state:", :padding => [0,8,0,4]
          et = edit_text :text => "Initial text.", :backgroundColor => 0x77ff0000,
                         :padding => [0,4,0,4]
          et.getLayoutParams.weight = 1.0
        end
      end
    end
  end

  #######################################################
  #
  # Graphics
  #

  #
  # Arcs
  #

  def self.arcs(context)
    context.start_ruboto_activity "$arcs" do
      setTitle "Graphics/Arcs"

      setup_content do
        @sweep_inc = 2
        @start_inc = 15
        @mStart = 0.0
        @mSweep = 0.0
        @mBigIndex = 0

        @mPaints = []
        @mUseCenters = []
        @mOvals = []
    
        @mPaints[0] = Paint.new
        @mPaints[0].setAntiAlias(true)
        @mPaints[0].setStyle(Paint::Style::FILL)
        @mPaints[0].setColor(0x88FF0000)
        @mUseCenters[0] = false
            
        @mPaints[1] = Paint.new(@mPaints[0])
        @mPaints[1].setColor(0x8800FF00)
        @mUseCenters[1] = true
           
        @mPaints[2] = Paint.new(@mPaints[0])
        @mPaints[2].setStyle(Paint::Style::STROKE)
        @mPaints[2].setStrokeWidth(4)
        @mPaints[2].setColor(0x880000FF)
        @mUseCenters[2] = false

        @mPaints[3] = Paint.new(@mPaints[2])
        @mPaints[3].setColor(0x88888888)
        @mUseCenters[3] = true
            
        @mBigOval  = RectF.new( 40,  10, 280, 250)
        @mOvals[0] = RectF.new( 10, 270,  70, 330)
        @mOvals[1] = RectF.new( 90, 270, 150, 330)
        @mOvals[2] = RectF.new(170, 270, 230, 330)
        @mOvals[3] = RectF.new(250, 270, 310, 330)
            
        @mFramePaint = Paint.new
        @mFramePaint.setAntiAlias(true)
        @mFramePaint.setStyle(Paint::Style::STROKE)
        @mFramePaint.setStrokeWidth(0)

        RubotoView.new(self)
      end

      def draw(canvas, oval, useCenters, paints, drawBig)
        if drawBig
          canvas.drawRect(@mBigOval, @mFramePaint)
          canvas.drawArc(@mBigOval, @mStart, @mSweep, useCenters, paints)
        end

        canvas.drawRect(oval, @mFramePaint)
        canvas.drawArc(oval, @mStart, @mSweep, useCenters, paints)
      end

      handle_draw do |view, canvas|
        canvas.drawColor(Color::WHITE)

        0.upto(3) {|i| draw(canvas, @mOvals[i], @mUseCenters[i], @mPaints[i], @mBigIndex == i)}
            
        @mSweep += @sweep_inc
        if (@mSweep > 360) 
          @mSweep -= 360
          @mStart += @start_inc
          @mStart -= 360 if @mStart >= 360 
          @mBigIndex = (@mBigIndex + 1) % @mOvals.length
        end

        view.invalidate
      end
    end
  end

  #######################################################
  #
  # OS
  #

  #
  # Morse Code
  #

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

  #
  # Sensors
  #

  def self.sensors(context)
    context.start_ruboto_activity "$sensors" do
      setTitle "OS/Sensors"

      setup_content do
        @rv = RubotoView.new(self)
      end

      handle_create do |bundle|
        @manager = getSystemService(Context::SENSOR_SERVICE)
        @sensors = [Sensor::TYPE_ACCELEROMETER, 
                    Sensor::TYPE_MAGNETIC_FIELD, 
                    Sensor::TYPE_ORIENTATION].map {|s| @manager.getDefaultSensor(s)}

        @canvas = Canvas.new
        @last_values = []
        @orientation_values = [0.0, 0.0, 0.0]
        @speed = 1.0
        
        @line_colors = [Color.argb(192, 255, 64,  64),
                        Color.argb(192, 64,  128, 64),
                        Color.argb(192, 64,  64,  255),
                        Color.argb(192, 64,  255, 255),
                        Color.argb(192, 128, 64,  128),
                        Color.argb(192, 255, 255, 64)]

        @paint = Paint.new
        @paint.setFlags(Paint::ANTI_ALIAS_FLAG);

        @rect = RectF.new(-0.5, -0.5, 0.5, 0.5)

        @path = Path.new
        @path.arcTo(@rect, 0, 180)
      end

      handle_resume do 
        @sensors.each{|s| @manager.registerListener(self, s, SensorManager::SENSOR_DELAY_FASTEST)}
      end

      handle_stop do 
        @sensors.each{|s| @manager.unregisterListener(self, s)}
      end

      handle_draw do |view, canvas|
        if @bitmap
          if (@last_x >= @max_x)
            @last_x = 0
            oneG = SensorManager::STANDARD_GRAVITY * @scale[0]
            @paint.setColor(0xFFAAAAAA)
            @canvas.drawColor(0xFFFFFFFF)
            @canvas.drawLine(0, @y_offset,        @max_x, @y_offset,        @paint)
            @canvas.drawLine(0, @y_offset + oneG, @max_x, @y_offset + oneG, @paint)
            @canvas.drawLine(0, @y_offset - oneG, @max_x, @y_offset - oneG, @paint)
          end

          canvas.drawBitmap(@bitmap, 0, 0, nil)

          0.upto(2) do |i|
            canvas.save(Canvas::MATRIX_SAVE_FLAG)
            canvas.translate(@circle_centers[i][0], @circle_centers[i][1])
            canvas.save(Canvas::MATRIX_SAVE_FLAG)
            @paint.setColor(0xFFC0C0C0)
            canvas.scale(@circle_size, @circle_size)
            canvas.drawOval(@rect, @paint)
            canvas.restore
            canvas.scale(@circle_size - 5, @circle_size - 5)
            @paint.setColor(0xFFff7010)
            canvas.rotate(0.0 - @orientation_values[i])
            canvas.drawPath(@path, @paint)
            canvas.restore
          end
        end
      end

      handle_size_changed do |view, w, h, oldw, oldh|
        @bitmap   = Bitmap.createBitmap(w, h, Bitmap::Config::RGB_565)
        @y_offset = h * 0.5
        @scale    = [(h * -0.5 * (1.0 / (SensorManager::STANDARD_GRAVITY * 2))),
                      (h * -0.5 * (1.0 / (SensorManager::MAGNETIC_FIELD_EARTH_MAX)))]
        @width    = w
        @height   = h
        @max_x    = w + ((@width < @height) ? 0 : 50)
        @last_x   = @max_x

        @canvas.setBitmap(@bitmap)
        @canvas.drawColor(0xFFFFFFFF)

        @circle_space = ((@width < @height) ? @width : @height) * 0.333333
        @circle_size = @circle_space - 32
        @circle_centers = []
        x = @circle_space * 0.5
        y = @circle_space * 0.5
        0.upto(2) do |i|
          if (@width < @height)
            @circle_centers << [x, y + 4.0]
            x += @circle_space
          else
            @circle_centers << [@width - (x + 4.0), y]
            y += @circle_space
          end
        end
      end

      handle_sensor_changed do |event|
        if @bitmap
          if (event.sensor.getType == Sensor::TYPE_ORIENTATION)
            @orientation_values = [event.values[0], event.values[1], event.values[2]]
          else
            j = (event.sensor.getType == Sensor::TYPE_MAGNETIC_FIELD) ? 1 : 0
            0.upto(2) do |i|
              k = i + j * 3
              v = @y_offset + event.values[i] * @scale[j]
              @paint.setColor(@line_colors[k])
              @canvas.drawLine(@last_x, @last_values[k], @last_x + @speed, v, @paint)
              @last_values[k] = v
            end
            @last_x += @speed if (event.sensor.getType == Sensor::TYPE_MAGNETIC_FIELD)
          end
          @rv.invalidate
        end
      end
    end
  end

  #######################################################
  #
  # Views
  #

  def self.chronometer_demo(context)
    context.start_ruboto_activity "$chronometer_demo" do
      setTitle "Views/Chronometer"
      setup_content do
        linear_layout(:orientation => LinearLayout::VERTICAL, 
                      :gravity => Gravity::CENTER_HORIZONTAL) do
          @c = chronometer :format => "Initial format: %s", 
                           :width => :wrap_content, 
                           :padding => [0,30,0,30] 
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
          button :text => "Normal", :width => :wrap_content
          button :text => "Small", :width => :wrap_content, :passing => [8,0,8,0]
          toggle_button :width => :wrap_content
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
          time_picker :on_time_changed_listener => self, 
                      :current_hour => 12, 
                      :current_minute=> 15
          @tv = text_view :text => "12:15"
        end
      end

      handle_time_changed do |view, hour, minute|
        @tv.setText("%02d:%02d" % [hour, minute]) if @tv
      end
    end
  end
end

#######################################################
#
# Start
#

RubotoActivity.launch_list $activity, "$main_list", "Api Demos", :main,
  "This is a Ruboto demo that attempts to duplicate the standard Android API Demo using Ruboto. It is in the early stages (more samples will be completed in the future)."
