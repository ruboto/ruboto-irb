#######################################################
#
# demo-android-api.rb (by Scott Moyer)
# 
# Attempt to use Ruboto to implement as many of the 
# standard Android API Demos as possible.
#
#######################################################

require 'ruboto/activity'
require 'ruboto/widget'
require 'ruboto/util/toast'

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
      RubotoActivity.launch_list(context, "Api Demos", title)
    else
      case title
      when "Custom Dialog"        then context.start_ruboto_dialog("CustomDialog")
      when "Custom Title"         then context.start_ruboto_activity("CustomTitle")
      when "Forwarding"           then context.start_ruboto_activity("Forwarding")
      when "Hello World"          then context.start_ruboto_activity("HelloWorld")
      when "Persistent State"     then context.start_ruboto_activity("PersistentState")
      when "Save & Restore State" then context.start_ruboto_activity("SaveAndRestoreState")
      when "Arcs"                 then context.start_ruboto_activity("Arcs")
      when "Morse Code"           then context.start_ruboto_activity("MorseCode")
      when "Sensors"              then context.start_ruboto_activity("Sensors")
      when "Buttons"              then context.start_ruboto_activity("Buttons")
      when "Chronometer"          then context.start_ruboto_activity("ChronometerDemo")
      when "1. Dialog"            then context.start_ruboto_activity("DateDialog")
      when "2. Inline"            then context.start_ruboto_activity("DateInline")
      else
        context.toast "Not Implemented Yet"
      end
    end
  end

  def self.launch_list(context, title, list_id, extra_text=nil)
    ruboto_import_widgets :LinearLayout, :TextView, :ListView

    context.start_ruboto_activity do
      @@list_id = list_id
      @@extra_text = extra_text

      def on_create(bundle)
        super
        setTitle title
        setContentView(
          linear_layout(:orientation => :vertical) do
            text_view(:text => @@extra_text) if @@extra_text
            list_view(:list => @@lists[@@list_id], 
                      :on_item_click_listener => (proc{|av, v, p, i| RubotoActivity.resolve_click self, v.getText}))
          end)
      end
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

class CustomDialog
  def on_create(bundle)
    ruboto_import_widgets :LinearLayout, :TextView

    super

    set_title "App/Activity/Custom Dialog"
    getWindow.setLayout(ViewGroup::LayoutParams::FILL_PARENT,
                        ViewGroup::LayoutParams::WRAP_CONTENT)

    cd = android.graphics.drawable.GradientDrawable.new
    cd.color = android.graphics.Color.argb(240,96,0,0)
    cd.set_stroke(3, android.graphics.Color.argb(255,255,128,128))
    cd.corner_radius = 3
    window.background_drawable = cd

    set_content_view(
      linear_layout(:orientation => :vertical, :padding => [10,0,10,10]) do
        text_view :text => "Example of how you can use a custom Theme.Dialog theme to make an activity that looks like a customized dialog, here with an ugly frame.", 
        :text_size => 14,
        :gravity => android.view.Gravity::CENTER_HORIZONTAL
      end)
  end
end

#
# Custom Title
#

class CustomTitle
  def on_create(bundle)
    ruboto_import_widgets :LinearLayout, :TextView, :Button, :EditText, :RelativeLayout
    requestWindowFeature android.view.Window::FEATURE_CUSTOM_TITLE unless @ruboto_java_instance.respond_to?(:action_bar)

    super

    setContentView(
      linear_layout(:orientation => :vertical) do
        linear_layout do
          @etl = edit_text(:text => "Left is best", :min_ems => 10, :max_ems => 10)
          button :text => "Change left", :on_click_listener => (proc{@tvl.text = @etl.text})
        end
        linear_layout do
          @etr = edit_text(:text => "Right is always right", :min_ems => 10, :max_ems => 10)
          button :text => "Change right", :on_click_listener => (proc{@tvr.text = @etr.text})
        end
      end)

    if @ruboto_java_instance.respond_to?(:action_bar)
      action_bar.set_display_options(android.app.ActionBar::DISPLAY_SHOW_CUSTOM, 
                                     android.app.ActionBar::DISPLAY_SHOW_CUSTOM | android.app.ActionBar::DISPLAY_USE_LOGO)
      action_bar.set_custom_view Ruboto::R::layout::empty_relative_layout 
      @rl = action_bar.custom_view
    else
      getWindow.setFeatureInt(android.view.Window::FEATURE_CUSTOM_TITLE, Ruboto::R::layout::empty_relative_layout)
      @rl = findViewById(Ruboto::Id::empty_relative_layout)
    end

    @tvl = text_view :text => "Left is best", 
                     :text_color => android.graphics.Color::WHITE,
                     :typeface => [android.graphics.Typeface::DEFAULT, android.graphics.Typeface::BOLD],
                     :parent => @rl, 
                     :layout => {:add_rule => :align_parent_left}

    @tvr = text_view :text => "Right is always right", 
                     :text_color => android.graphics.Color::WHITE,
                     :typeface => [android.graphics.Typeface::DEFAULT, android.graphics.Typeface::BOLD],
                     :parent => @rl, 
                     :layout => {:add_rule => :align_parent_right}
  end
end

#
# Forwarding
#

class Forwarding
  def on_create(bundle)
    ruboto_import_widgets :LinearLayout, :Button, :TextView
    super
    setTitle "App/Activity/Forwarding"

    setContentView(
      linear_layout(:orientation => :vertical) do
        text_view :text => "Press the button to go forward to the next activity.  This activity will stop, so you will no longer see it when going back."
        linear_layout(:gravity => android.view.Gravity::CENTER_HORIZONTAL) do
        button :text => "Go", :width => :wrap_content, :on_click_listener => (proc{|v| my_click(v)})
      end
    end)
  end

  def my_click(view)
    self.start_ruboto_activity "$forwarding2" do
      def on_create (bundle)
        super
        setTitle "App/Activity/Forwarding"
        setContentView(
          text_view :text => "Press back button and notice we don't see the previous activity.")
      end
    end
    finish
  end
end

#
# Hello, World
#

class HelloWorld
  def on_create(bundle)
    ruboto_import_widgets :TextView
    super
    setTitle "App/Activity/Hello World"
    setContentView(text_view :text => "Hello, World!", 
                             :gravity => (android.view.Gravity::CENTER_HORIZONTAL | 
                                          android.view.Gravity::CENTER_VERTICAL))
  end
end

#
# Persistent State
#

class PersistentState
  def on_create(bundle)
    ruboto_import_widgets :LinearLayout, :TextView, :EditText
    super
    setTitle "App/Activity/Persistent State"
    getWindow.setSoftInputMode(
               android.view.WindowManager::LayoutParams::SOFT_INPUT_STATE_VISIBLE | 
               android.view.WindowManager::LayoutParams::SOFT_INPUT_ADJUST_RESIZE)
    setContentView(
      linear_layout(:orientation => :vertical, :padding => [4,4,4,4]) do
        text_view :text => "Demonstration of persistent activity state with getPreferences(0).edit() and getPreferences(0).", :padding => [0,0,0,4]
        text_view :text => "This text field saves its state:", :padding => [0,0,0,4]
        @save = edit_text(:text => "Initial text.", :backgroundColor => 0x7700ff00, 
                          :padding => [0,4,0,4], :layout => {:weight= => 1.0})

        text_view :text => "This text field does not save its state:", :padding => [0,8,0,4]
        edit_text :text => "Initial text.", :backgroundColor => 0x77ff0000, 
                          :padding => [0,4,0,4], :layout => {:weight= => 1.0}
      end)
  end

  def on_pause
    super
    editor = getPreferences(0).edit
    editor.putString("text", @save.getText.toString)
    editor.putInt("selection-start", @save.getSelectionStart)
    editor.putInt("selection-end", @save.getSelectionEnd)
    editor.commit
  end

  def on_resume
    super
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

#
# Save and Restore State
#

class SaveAndRestoreState
  def on_create(bundle)
    ruboto_import_widgets :LinearLayout, :TextView, :EditText
    super
    setTitle "App/Activity/Save & Restore State"
    getWindow.setSoftInputMode(
               android.view.WindowManager::LayoutParams::SOFT_INPUT_STATE_VISIBLE | 
               android.view.WindowManager::LayoutParams::SOFT_INPUT_ADJUST_RESIZE)
    setContentView(
      linear_layout(:orientation => :vertical, :padding => [4,4,4,4]) do
        text_view :text => "Demonstration of saving and restoring activity state in onSaveInstanceState() and onCreate().", :padding => [0,0,0,4]
        text_view :text => "This text field saves its state:", :padding => [0,0,0,4]
        @save = edit_text(:text => "Initial text.", :backgroundColor => 0x7700ff00, :padding => [0,4,0,4], 
                          :freezes_text => true, :id => 55555, :layout => {:weight= => 1.0})

        text_view :text => "This text field does not save its state:", :padding => [0,8,0,4]
        edit_text :text => "Initial text.", :backgroundColor => 0x77ff0000,
                             :padding => [0,4,0,4], :layout => {:weight= => 1.0}
      end)
  end
end

#######################################################
#
# Graphics
#

#
# Arcs
#

class ArcsView < android.view.View
  java_import "android.graphics.Color"
  java_import "android.graphics.Paint"
  java_import "android.graphics.RectF"

  def initialize(c)
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
    @mPaints[0].setColor(Color::RED)
    @mUseCenters[0] = false
            
    @mPaints[1] = Paint.new(@mPaints[0])
    @mPaints[1].setColor(Color::GREEN)
    @mUseCenters[1] = true
           
    @mPaints[2] = Paint.new(@mPaints[0])
    @mPaints[2].setStyle(Paint::Style::STROKE)
    @mPaints[2].setStrokeWidth(4)
    @mPaints[2].setColor(Color::BLUE)
    @mUseCenters[2] = false

    @mPaints[3] = Paint.new(@mPaints[2])
    @mPaints[3].setColor(Color::GRAY)
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

    super
  end

  def onDraw(canvas)
    canvas.drawColor(Color::WHITE)

    0.upto(3) {|i| draw_object(canvas, @mOvals[i], @mUseCenters[i], @mPaints[i], @mBigIndex == i)}
            
    @mSweep += @sweep_inc
    if (@mSweep > 360) 
      @mSweep -= 360
      @mStart += @start_inc
      @mStart -= 360 if @mStart >= 360 
      @mBigIndex = (@mBigIndex + 1) % @mOvals.length
    end

    invalidate
  end

  def draw_object(canvas, oval, useCenters, paints, drawBig)
    if drawBig
      canvas.drawRect(@mBigOval, @mFramePaint)
      canvas.drawArc(@mBigOval, @mStart, @mSweep, useCenters, paints)
    end

    canvas.drawRect(oval, @mFramePaint)
    canvas.drawArc(oval, @mStart, @mSweep, useCenters, paints)
  end
end

class Arcs
  def on_create(bundle)
    super
    setTitle "Graphics/Arcs"
    setContentView(ArcsView.new(@ruboto_java_instance))
  end
end

#######################################################
#
# OS
#

#
# Morse Code
#

class MorseCode
  java_import "android.content.Context"

  @@base  = 100
  @@durations = {"." => [@@base, @@base],  "-" => [@@base* 3, @@base], 
                 "|" => [0, @@base * 2],  " " => [0, @@base * 6]}
  @@codes = {"A" => ".-",   "B" => "-...", "C" => "-.-.", "D" => "-..",
             "E" => ".",    "F" => "..-.", "G" => "--.",  "H" => "....",
             "I" => "..",   "J" => ".---", "K" => "-.-",  "L" => ".-..",
             "M" => "--",   "N" => "-.",   "O" => "---",  "P" => ".--.",
             "Q" => "--.-", "R" => ".-.",  "S" => "...",  "T" => "-",
             "U" => "..-",  "V" => "..-",  "W" => ".--",  "X" => "-..-",
             "Y" => "-.--", "Z" => "--..", "0" => "-----","1" => ".----",
             "2" => "..---","3" => "...--","4" => "....-","5" => ".....",
             "6" => "-....","7" => "--...","8" => "---..","9" => "----."}

  def on_create(bundle)
    ruboto_import_widgets :LinearLayout, :EditText, :Button
    super
    setTitle "OS/Morse Code"
    setContentView(
      linear_layout(:orientation => :vertical) do
        @et = edit_text
        button :text => "Vibrate", :width => :wrap_content, 
                 :on_click_listener => (proc{|v| handle_click(v)})
      end)
  end

  def handle_click(view)
    getSystemService(Context::VIBRATOR_SERVICE).vibrate(
      @et.getText.to_s.upcase.split('').
      map {|i| @@codes[i] || " "}.join('|').split('').
      map {|i| @@durations[i]}.flatten.unshift(0).to_java(:long), -1)
  end
end

#
# Sensors
#

class SensorsView < android.view.View
  include android.hardware.SensorEventListener

  java_import "android.graphics.Path"
  java_import "android.graphics.Color"
  java_import "android.graphics.Paint"
  java_import "android.graphics.RectF"
  java_import "android.graphics.Bitmap"
  java_import "android.graphics.Canvas"
  java_import "android.hardware.Sensor"
  java_import "android.hardware.SensorManager"

  def initialize(c)
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

    super
  end

  def onDraw(canvas)
    if @bitmap
      if (@last_x >= @max_x)
        @last_x = 0
        oneG = SensorManager::STANDARD_GRAVITY * @scale[0]
        @paint.setColor(Color.argb(0xFF, 0xAA, 0xAA, 0xAA))
        @canvas.drawColor(Color::WHITE)
        @canvas.drawLine(0, @y_offset,        @max_x, @y_offset,        @paint)
        @canvas.drawLine(0, @y_offset + oneG, @max_x, @y_offset + oneG, @paint)
        @canvas.drawLine(0, @y_offset - oneG, @max_x, @y_offset - oneG, @paint)
      end

      canvas.drawBitmap(@bitmap, 0, 0, nil)

      0.upto(2) do |i|
        canvas.save(Canvas::MATRIX_SAVE_FLAG)
        canvas.translate(@circle_centers[i][0], @circle_centers[i][1])
        canvas.save(Canvas::MATRIX_SAVE_FLAG)
        @paint.setColor(Color.argb(0xFF, 0xC0, 0xC0, 0xC0))
        canvas.scale(@circle_size, @circle_size)
        canvas.drawOval(@rect, @paint)
        canvas.restore
        canvas.scale(@circle_size - 5, @circle_size - 5)
        @paint.setColor(Color.argb(0xFF, 0xFF, 0x70, 0x10))
        canvas.rotate(0.0 - @orientation_values[i])
        canvas.drawPath(@path, @paint)
        canvas.restore
      end
    end
  end

  def onSizeChanged(w, h, oldw, oldh)
    @bitmap   = Bitmap.createBitmap(w, h, Bitmap::Config::RGB_565)
    @y_offset = h * 0.5
    @scale    = [(h * -0.5 * (1.0 / (SensorManager::STANDARD_GRAVITY * 2))),
                 (h * -0.5 * (1.0 / (SensorManager::MAGNETIC_FIELD_EARTH_MAX)))]
    @width    = w
    @height   = h
    @max_x    = w + ((@width < @height) ? 0 : 50)
    @last_x   = @max_x

    @canvas.setBitmap(@bitmap)
    @canvas.drawColor(Color::WHITE)

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

  def onSensorChanged(event)
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
      invalidate
    end
  end

  def onAccuracyChanged(sensor, accuracy)
  end
end

class Sensors
  java_import "android.hardware.SensorManager"
  java_import "android.hardware.Sensor"
  java_import "android.content.Context"

  def on_create(bundle)
    super
    setTitle "OS/Sensors"

    @manager = getSystemService(Context::SENSOR_SERVICE)
    @sensors = [Sensor::TYPE_ACCELEROMETER, 
                Sensor::TYPE_MAGNETIC_FIELD, 
                Sensor::TYPE_ORIENTATION].map {|s| @manager.getDefaultSensor(s)}

    @sensors_view = SensorsView.new(@ruboto_java_instance)
    self.content_view = @sensors_view
  end

  def on_resume
    super
    @sensors.each{|s| @manager.registerListener(@sensors_view, s, SensorManager::SENSOR_DELAY_FASTEST)}
  end

  def on_stop
    super
    @sensors.each{|s| @manager.unregisterListener(@sensors_view, s)}
  end
end

#######################################################
#
# Views
#

class ChronometerDemo
  java_import "android.os.SystemClock"
  java_import "android.view.Gravity"

  def on_create(bundle)
    ruboto_import_widgets :LinearLayout, :Button, :Chronometer
    super
    setTitle "Views/Chronometer"
    setContentView(
      linear_layout(:orientation => :vertical, :gravity => Gravity::CENTER_HORIZONTAL) do
        @c = chronometer :format => "Initial format: %s", :width => :wrap_content, :padding => [0,30,0,30] 
        button :text => "Start", :width => :wrap_content, :on_click_listener => proc{@c.start}
        button :text => "Stop", :width => :wrap_content, :on_click_listener => proc{@c.stop}
        button :text => "Reset", :width => :wrap_content, :on_click_listener => proc{@c.setBase SystemClock.elapsedRealtime}
        button :text => "Set format string", :width => :wrap_content, :on_click_listener => proc{@c.setFormat("Formatted time (%s)")}
        button :text => "Clear format string", :width => :wrap_content, :on_click_listener => proc{@c.setFormat(nil)}
      end)
  end
end

class Buttons
  def on_create(bundle)
    ruboto_import_widgets :LinearLayout, :Button, :ToggleButton
    super
    setTitle "Views/Buttons"
    setContentView(
      linear_layout(:orientation => :vertical) do
        button :text => "Normal", :width => :wrap_content
        button :text => "Small", :width => :wrap_content, 
               :default_style => JavaUtilities.get_proxy_class("android.R$attr")::buttonStyleSmall
        toggle_button :width => :wrap_content
      end)
  end
end

class DateDialog
  java_import "android.app.TimePickerDialog"
  java_import "android.app.DatePickerDialog"

  def on_create(bundle)
    ruboto_import_widgets :LinearLayout, :TextView, :Button
    super
    setTitle "Views/Date Widgets/1. Dialog"

    @time  = Time.now

    setContentView(
      linear_layout(:orientation => :vertical) do
        @tv = text_view :text => @time.strftime("%m-%d-%Y %R")
        button :text => "change the date", :width => :wrap_content, :on_click_listener => proc{date_picker.show}
        button :text => "change the time", :width => :wrap_content, :on_click_listener => proc{time_picker.show}
      end)
  end

  def date_picker
    @date_set_listener ||= proc{|v, y, m, d| @tv.text = ("%d-%d-%d #{@tv.text.split(' ')[1]}" % [m+1, d, y])}
    @date_picker ||= DatePickerDialog.new(@ruboto_java_instance, @date_set_listener, @time.year, @time.month-1, @time.day)
  end

  def time_picker
    @time_set_listener ||= proc{|v, h, m| @tv.text = ("#{@tv.text.split(' ')[0]} %02d:%02d" % [h, m])}
    @time_picker ||= TimePickerDialog.new(@ruboto_java_instance, @time_set_listener, @time.hour, @time.min, false)
  end
end

class DateInline
  def on_create(bundle)
    ruboto_import_widgets :LinearLayout, :TextView, :TimePicker
    super
    setTitle "Views/Date Widgets/#{title}"
    setContentView(
      linear_layout do
        time_picker :on_time_changed_listener => proc{|v, h, m| @tv.setText("%02d:%02d" % [h, m]) if @tv}, 
                    :current_hour => 12, :current_minute=> 15
        @tv = text_view :text => "12:15"
      end)
  end
end

#######################################################
#
# Start
#

RubotoActivity.launch_list($irb, "Api Demos", :main,
  "This is a Ruboto demo that attempts to duplicate a few of the standard Android API Demo using Ruboto.")

