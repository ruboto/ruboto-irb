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
require 'ruboto/generate'
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
      RubotoActivity.launch_list context, "$sl_#{title.downcase.gsub(' ', '_')}", "Api Demos", title
    else
      case title
      when "Custom Dialog"      then custom_dialog(context)
      when "Custom Title"       then custom_title(context)
      when "Forwarding"         then forwarding(context)
      when "Hello World"        then hello_world(context)
      when "Persistent State"   then persistent_state(context)
      when "Save & Restore State" then save_and_restore_state(context)
      when "Arcs"               then arcs(context)
      when "Morse Code"         then morse_code(context)
      when "Sensors"            then sensors(context)
      when "Buttons"            then buttons(context)
      when "Chronometer"        then chronometer_demo(context)
      when "1. Dialog"          then date_dialog(context)
      when "2. Inline"          then date_inline(context)
      else
        context.toast "Not Implemented Yet"
      end
    end
  end

  def self.launch_list(context, var, title, list_id, extra_text=nil)
    ruboto_import_widgets :LinearLayout, :TextView, :ListView

    context.start_ruboto_activity var do
      setTitle title

      @list_id = list_id
      @extra_text = extra_text

      def on_create(bundle)
        setContentView(
          linear_layout :orientation => :vertical do
            text_view(:text => @extra_text) if @extra_text
            list_view :list => @@lists[@list_id], :on_item_click_listener => @handle_item_click 
          end)
      end

      @handle_item_click = Proc.new do |adapter_view, view, pos, item_id| 
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
    java_import "android.graphics.drawable.GradientDrawable"
    java_import "android.view.Gravity"
    java_import "android.graphics.Color"

    ruboto_import_widgets :LinearLayout, :TextView

    context.start_ruboto_dialog "$custom_dialog" do
      setTitle "App/Activity/Custom Dialog"

      def on_create(bundle)
        getWindow.setLayout(ViewGroup::LayoutParams::FILL_PARENT,
                            ViewGroup::LayoutParams::WRAP_CONTENT)
        cd = GradientDrawable.new
        cd.setColor(Color.argb(240,96,0,0))
        cd.setStroke(3, Color.argb(255,255,128,128))
        cd.setCornerRadius(3)
        getWindow.setBackgroundDrawable(cd)

        setContentView(
          linear_layout(:orientation => :vertical, :padding => [10,0,10,10]) do
            text_view :text => "Example of how you can use a custom Theme.Dialog theme to make an activity that looks like a customized dialog, here with an ugly frame.", 
            :text_size => 14,
            :gravity => Gravity::CENTER_HORIZONTAL
          end)
      end
    end
  end

  #
  # Custom Title
  #

  def self.custom_title(context)
    java_import "android.graphics.Typeface"
    java_import "android.view.Window"
    java_import "android.graphics.Color"

    ruboto_import_widgets :LinearLayout, :TextView, :Button, :EditText, :RelativeLayout

    context.start_ruboto_activity "$custom_title" do
      requestWindowFeature Window::FEATURE_CUSTOM_TITLE

      def on_create(bundle)
        setContentView(
          linear_layout :orientation => :vertical do
            linear_layout do
              @etl = edit_text(:text => "Left is best", :min_ems => 10, :max_ems => 10)
              button :text => "Change left", :on_click_listener => (proc{@tvl.text = @etl.text})
            end
            linear_layout do
              @etr = edit_text(:text => "Right is always right", :min_ems => 10, :max_ems => 10)
              button :text => "Change right", :on_click_listener => (proc{@tvr.text = @etr.text})
            end
          end)

        getWindow.setFeatureInt(Window::FEATURE_CUSTOM_TITLE, 
                                Ruboto::R::layout::empty_relative_layout)

        @rl = findViewById(Ruboto::Id::empty_relative_layout)
        @tvl = text_view :text => "Left is best", :text_color => Color::WHITE,
                         :typeface => [Typeface::DEFAULT, Typeface::BOLD],
                         :parent => @rl, :layout => {:add_rule => :align_parent_left}

        @tvr = text_view :text => "Right is always right", :text_color => Color::WHITE,
                         :typeface => [Typeface::DEFAULT, Typeface::BOLD],
                         :parent => @rl, :layout => {:add_rule => :align_parent_right}
      end
    end
  end

  #
  # Forwarding
  #

  def self.forwarding(context)
    java_import "android.view.Gravity"

    ruboto_import_widgets :LinearLayout, :Button, :TextView

    context.start_ruboto_activity "$forwarding" do
      setTitle "App/Activity/Forwarding"

      def on_create(bundle)
        setContentView(
          linear_layout :orientation => :vertical do
            text_view :text => "Press the button to go forward to the next activity.  This activity will stop, so you will no longer see it when going back."
            linear_layout(:gravity => Gravity::CENTER_HORIZONTAL) do
              button :text => "Go", :width => :wrap_content, :on_click_listener => @handle_click
            end
          end)
      end

      @handle_click = proc do |view|
        context.start_ruboto_activity "$forwarding2" do
          setTitle "App/Activity/Forwarding"
          def on_create (bundle)
            setContentView(
              text_view :text => "Press back button and notice we don't see the previous activity.")
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
    java_import "android.view.Gravity"

    ruboto_import_widgets :TextView

    context.start_ruboto_activity "$hello_world" do
      setTitle "App/Activity/Hello World"

      def on_create(bundle)
        setContentView(text_view :text => "Hello, World!", 
                                    :gravity => (Gravity::CENTER_HORIZONTAL | Gravity::CENTER_VERTICAL))
      end
    end
  end

  #
  # Persistent State
  #

  def self.persistent_state(context)
    java_import "android.view.WindowManager"

    ruboto_import_widgets :LinearLayout, :TextView, :EditText

    context.start_ruboto_activity "$persistent_state" do
      setTitle "App/Activity/Persistent State"
      getWindow.setSoftInputMode(
                 WindowManager::LayoutParams::SOFT_INPUT_STATE_VISIBLE | 
                 WindowManager::LayoutParams::SOFT_INPUT_ADJUST_RESIZE)

      def on_create(bundle)
        setContentView(
          linear_layout :orientation => :vertical, :padding => [4,4,4,4] do
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
        editor = getPreferences(0).edit
        editor.putString("text", @save.getText.toString)
        editor.putInt("selection-start", @save.getSelectionStart)
        editor.putInt("selection-end", @save.getSelectionEnd)
        editor.commit
      end

      def on_resume
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
    java_import "android.view.WindowManager"

    ruboto_import_widgets :LinearLayout, :TextView, :EditText

    context.start_ruboto_activity "$save_and_restore_state" do
      setTitle "App/Activity/Save & Restore State"
      getWindow.setSoftInputMode(
                 WindowManager::LayoutParams::SOFT_INPUT_STATE_VISIBLE | 
                 WindowManager::LayoutParams::SOFT_INPUT_ADJUST_RESIZE)

      def on_create(bundle)
        setContentView(
          linear_layout :orientation => :vertical, :padding => [4,4,4,4] do
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
  end

  #######################################################
  #
  # Graphics
  #

  #
  # Arcs
  #

  def self.arcs(context)
    java_import "android.graphics.Color"
    java_import "android.graphics.Paint"
    java_import "android.graphics.RectF"

    ruboto_generate(android.view.View => "org.ruboto.RubotoView")

    context.start_ruboto_activity "$arcs" do
      setTitle "Graphics/Arcs"

      def on_create(bundle)
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

        setContentView(@ruboto_view)
      end

      def draw(canvas, oval, useCenters, paints, drawBig)
        if drawBig
          canvas.drawRect(@mBigOval, @mFramePaint)
          canvas.drawArc(@mBigOval, @mStart, @mSweep, useCenters, paints)
        end

        canvas.drawRect(oval, @mFramePaint)
        canvas.drawArc(oval, @mStart, @mSweep, useCenters, paints)
      end
      
      def view_on_draw(canvas)
        canvas.drawColor(Color::WHITE)

        0.upto(3) {|i| draw(canvas, @mOvals[i], @mUseCenters[i], @mPaints[i], @mBigIndex == i)}
            
        @mSweep += @sweep_inc
        if (@mSweep > 360) 
          @mSweep -= 360
          @mStart += @start_inc
          @mStart -= 360 if @mStart >= 360 
         @mBigIndex = (@mBigIndex + 1) % @mOvals.length
        end

        @ruboto_view.invalidate
      end

      @ruboto_view = RubotoView.new(self)
      @ruboto_view.initialize_ruboto_callbacks do 
        def on_draw(canvas)
          $arcs.view_on_draw(canvas)
        end
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
    java_import "android.content.Context"

    ruboto_import_widgets :LinearLayout, :EditText, :Button

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
      def on_create(bundle)
        setContentView(
          linear_layout :orientation => :vertical do
            @et = edit_text
            button :text => "Vibrate", :width => :wrap_content, :on_click_listener => @handle_click
          end)
      end
      @handle_click = proc do |view|
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
    java_import "android.graphics.Path"
    java_import "android.hardware.SensorManager"
    java_import "android.hardware.Sensor"
    java_import "android.graphics.Color"
    java_import "android.graphics.Paint"
    java_import "android.graphics.RectF"
    java_import "android.graphics.Bitmap"
    java_import "android.graphics.Canvas"
    java_import "android.content.Context"

    ruboto_generate(android.view.View => "org.ruboto.RubotoView")

    context.start_ruboto_activity "$sensors" do
       setTitle "OS/Sensors"

      def on_create(bundle)
        @manager = getSystemService(Context::SENSOR_SERVICE)
        @sensors = [Sensor::TYPE_ACCELEROMETER, 
                    Sensor::TYPE_MAGNETIC_FIELD, 
                    Sensor::TYPE_ORIENTATION].map {|s| @manager.getDefaultSensor(s)}

        self.content_view = @rv
      end

      def on_resume
        @sensors.each{|s| @manager.registerListener(@sensor_event_listener, s, SensorManager::SENSOR_DELAY_FASTEST)}
      end

      def on_stop
        @sensors.each{|s| @manager.unregisterListener(@sensor_event_listener, s)}
      end

      def view
        @rv
      end

      @rv = RubotoView.new(self).initialize_ruboto_callbacks do
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

        def on_draw(canvas)
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

        def on_size_changed(w, h, oldw, oldh)
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

        def sensor_changed(event)
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
              self.invalidate
            end
        end
      end

      @sensor_event_listener = Object.new
      class << @sensor_event_listener
        def onSensorChanged(event)
          $sensors.view.sensor_changed(event)
        end

        def onAccuracyChanged(sensor, accuracy)
        end
      end
    end
  end

  #######################################################
  #
  # Views
  #

  def self.chronometer_demo(context)
    java_import "android.os.SystemClock"
    java_import "android.view.Gravity"

    ruboto_import_widgets :LinearLayout, :Button, :Chronometer

    context.start_ruboto_activity "$chronometer_demo" do
      setTitle "Views/Chronometer"
      def on_create(bundle)
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
  end

  def self.buttons(context)
    ruboto_import_widgets :LinearLayout, :Button, :ToggleButton

    context.start_ruboto_activity "$buttons" do
      setTitle "Views/Buttons"
      def on_create(bundle)
        setContentView(
          linear_layout :orientation => :vertical do
            button :text => "Normal", :width => :wrap_content
            button :text => "Small", :width => :wrap_content, 
                    :default_style => JavaUtilities.get_proxy_class("android.R$attr")::buttonStyleSmall
            toggle_button :width => :wrap_content
          end)
      end
    end
  end

  def self.date_dialog(context)
    java_import "android.app.TimePickerDialog"
    java_import "android.app.DatePickerDialog"

    ruboto_import_widgets :LinearLayout, :TextView, :Button

    context.start_ruboto_activity "$date_dialog" do
      setTitle "Views/Date Widgets/1. Dialog"

      def on_create(bundle)
        @time  = Time.now

        setContentView(
          linear_layout :orientation => :vertical do
            @tv = text_view :text => @time.strftime("%m-%d-%Y %R")
            button :text => "change the date", :width => :wrap_content, :on_click_listener => proc{date_picker.show}
            button :text => "change the time", :width => :wrap_content, :on_click_listener => proc{time_picker.show}
          end)
      end

      def date_picker
          @date_picker ||= DatePickerDialog.new(self, @date_set_listener, @time.year, @time.month-1, @time.day)
      end

      def time_picker
          @time_picker ||= TimePickerDialog.new(self, @time_set_listener, @time.hour, @time.min, false)
      end

      @date_set_listener = proc do |view, year, month, day|
        @tv.text = ("%d-%d-%d #{@tv.text.split(' ')[1]}" % [month+1, day, year])
      end

      @time_set_listener = proc do |view, hour, minute|
        @tv.text = ("#{@tv.text.split(' ')[0]} %02d:%02d" % [hour, minute])
      end
    end
  end

  def self.date_inline(context)
    ruboto_import_widgets :LinearLayout, :TextView, :TimePicker

    context.start_ruboto_activity "$date_inline" do
      setTitle "Views/Date Widgets/#{title}"
      def on_create(bundle)
        setContentView(
          linear_layout do
            time_picker :on_time_changed_listener => proc{|v, h, m| @tv.setText("%02d:%02d" % [h, m]) if @tv}, 
                        :current_hour => 12, :current_minute=> 15
            @tv = text_view :text => "12:15"
          end)
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

