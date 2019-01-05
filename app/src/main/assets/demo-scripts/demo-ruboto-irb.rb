#######################################################
#
# demo-ruboto-irb.rb (by Scott Moyer)
# 
# This demo duplicates the functionality of 
# Ruboto IRB (written in Java) with a ruboto 
# script (written in Ruby).
#
#######################################################

require 'ruboto/activity'
require 'ruboto/widget'
require 'ruboto/util/stack'
require 'ruboto/util/toast'

require 'fileutils'

java_import "android.view.WindowManager"
java_import "android.view.Gravity"
java_import "android.view.KeyEvent"
java_import "android.view.MenuItem"
java_import "android.text.util.Linkify"
java_import "android.app.AlertDialog"
java_import "android.content.DialogInterface"
java_import "android.content.Context"
java_import "android.text.method.ScrollingMovementMethod"

ruboto_import_widgets :TabHost, :LinearLayout, :FrameLayout, :TabWidget, 
  :Button, :EditText, :TextView, :ListView, :ScrollView

require 'stringio'
$main_binding = self.instance_eval{binding}

################################################################################
#
# Java Subclasses
#

class FlingGestureListener < android.view.GestureDetector::SimpleOnGestureListener
  def self.context=(context)
    @@context = context
  end

  def initialize()
    @context = @@context.ruboto_java_instance
    @activity = @@context
    @scroller = android.widget.Scroller.new(@context)
    @x = 0
    super
  end

  def onDown(e)
    return false if @scroller.finished?
    @scroller.forceFinished(true)
    return true
  end

  def onFling(e1, e2, velocityX, velocityY)
    return false if velocityY.abs < android.view.ViewConfiguration.get(@context).getScaledMinimumFlingVelocity
    @x = @activity.edit_script.scroll_x
    max = @activity.edit_script.getLayout.height - @activity.edit_script.height + @activity.edit_script.line_height
    @scroller.fling(0, @activity.edit_script.scroll_y, 0, 0-velocityY, @x, @x, 0, max)
    @activity.edit_script.post(my_run_proc)
    return true
  end

  def my_run_proc
    @run ||= proc do
      if not @scroller.finished? and @scroller.compute_scroll_offset
        @activity.edit_script.scrollTo(@x, @scroller.curr_y)
        @activity.edit_script.post(my_run_proc)
      end
    end
  end
end

class LineNumberEditText < android.widget.EditText
  def initialize(context)
    super
    @show_line_numbers = context.getPreferences(Context::MODE_PRIVATE).getBoolean("LineNumbers", true)
    @paint = android.graphics.Paint.new
    @paint.color = (text_colors.default_color & android.graphics.Color.argb(0xAA, 0xFF, 0xFF, 0xFF))
    @paint.text_size = (text_size * 0.6)
    @paint.setTypeface android.graphics.Typeface::MONOSPACE
    @text_paint = android.text.TextPaint.new(@paint)
    @default_left = padding_left
    @current_magnitude = @line_number_width = 0
    @lc = 1

    self.hint = "Enter source code here."
    self.gravity = Gravity::TOP
    self.horizontally_scrolling = true
    self.raw_input_type = (android.text.InputType::TYPE_TEXT_FLAG_NO_SUGGESTIONS | android.text.InputType::TYPE_TEXT_FLAG_MULTI_LINE)
    self.typeface = android.graphics.Typeface::MONOSPACE
    self.scroll_container = true 
    self.filters = [android.text.InputFilter::LengthFilter.new(1000000)].to_java(android.text.InputFilter)
  end
    
  def toggle_show_line_numbers
    @show_line_numbers = !@show_line_numbers
    prefs_editor = context.getPreferences(Context::MODE_PRIVATE).edit
    prefs_editor.putBoolean("LineNumbers", @show_line_numbers)
    prefs_editor.commit
    build_line_numbers
    invalidate
  end
    
  def min(a, b)
    a > b ? b : a
  end
    
  def max(a, b)
    a > b ? a : b
  end

  def build_line_numbers
    if @layout.nil? 
      @text = android.text.SpannableStringBuilder.new("1")
      @layout = android.text.DynamicLayout.new(@text, @text_paint, 200, 
                                                  android.text.Layout::Alignment::ALIGN_RIGHT, 
                                                  0.0, line_height, false)
      @line_ascent = @layout.get_line_ascent(0)
    end
      
    new_magnitude = (!@show_line_numbers || line_count == 0) ? 0 : ((Math.log10(line_count) + 0.0001).to_i + 1)
    if @current_magnitude != new_magnitude  
      @line_number_width = new_magnitude * @paint.text_size          
      post{set_padding(@default_left + @line_number_width, padding_top, padding_right, padding_bottom)} 
      @current_magnitude = new_magnitude
    end

    if @lc > line_count && line_count > 0
      @text.replace(calculate_line_number_size(line_count), @text.length, "")
    else
      (@lc + 1).upto(line_count){|i| @text.append("\n#{i}")}
    end

    @lc = line_count == 0 ? 1 :  line_count
  end
    
  def calculate_line_number_size(i)
    size = (i * 2) - 1 
    1.upto((Math.log10(i) + 0.0001).to_i){|x| size += i - ((10 ** x) - 1)}
    size
  end
    
  def onDraw(canvas)
    super

    if (@show_line_numbers && line_count > 0)
      build_line_numbers if @layout.nil? or @lc != line_count

      canvas.save

      canvas.translate(scroll_x - 200 + @line_number_width, extended_padding_top - @line_ascent) 
      canvas.clipRect(200 - @line_number_width, scroll_y - extended_padding_top, padding_left + 200,
                      bottom - top - extended_padding_bottom + scroll_y - extended_padding_top + @line_ascent)
      @layout.draw(canvas)
                      
      canvas.restore
    end
  end
end

################################################################################

$irb.start_ruboto_activity do
  attr_reader :ruboto_java_instance

  #
  # UI setup
  #
  
  def on_create(bundle)
    getWindow.setSoftInputMode(
               WindowManager::LayoutParams::SOFT_INPUT_STATE_VISIBLE | 
               WindowManager::LayoutParams::SOFT_INPUT_ADJUST_RESIZE)

    super

    @history = [""]
    @cursor = 0

    @tabs = TabHost.new(@ruboto_java_instance, nil) # Needs to be created this way to avoid bug introduced Android >= 3.0
    @tab_container = linear_layout(:orientation => :vertical, :layout => {:height => :fill_parent}, 
                                   :parent => @tabs, :background_color => android.graphics.Color::BLACK)

    tab_widget(:id => AndroidIds::tabs, :parent => @tab_container)
    frame_layout(:id => AndroidIds::tabcontent, :layout => {:height => :fill_parent}, :parent => @tab_container) do
      linear_layout(:id => 55555, :layout => {:height => :fill_parent}, :orientation => :vertical) do
        @irb_edit = edit_text(:lines => 1, :on_key_listener => (proc{|v,kc,e| my_key_listener(v,kc,e)}))
        @irb_text = text_view(:text => "#{explanation_text}\n\n>> ", 
                        :layout => {:height => :fill_parent}, 
                        :gravity => (Gravity::BOTTOM | Gravity::CLIP_VERTICAL), 
                        :text_color => android.graphics.Color::WHITE,
                        :movement_method => ScrollingMovementMethod.new)
      end

      ll = linear_layout(:id => 55556, :orientation => :vertical) do
        @edit_name   = edit_text :text => "untitled.rb"
      end
      @edit_script = LineNumberEditText.new(@ruboto_java_instance)
      ll.addView @edit_script
      @edit_script.layout_params.height = View.convert_constant(:fill_parent)
      @edit_script.layout_params.width = View.convert_constant(:fill_parent)
      @edit_script.layout_params.weight = 1

      @scripts = list_view(:id => 55557, :list => [], 
                           :on_item_click_listener => (proc{|av, v, p, i| edit @scripts_list[p]}))
    end

    FlingGestureListener.context = self
    @fling_listener = FlingGestureListener.new
    @gestureDetector = android.view.GestureDetector.new(@fling_listener)
    @edit_script.setOnTouchListener do |view, event|
      @gestureDetector.onTouchEvent(event)
    end

    registerForContextMenu(@scripts)
    load_script_list

    @current_tab = "irb"
    @tabs.setup
    @tabs.addTab(@tabs.newTabSpec("irb").setContent(55555).setIndicator("IRB"))
    @tabs.addTab(@tabs.newTabSpec("editor").setContent(55556).setIndicator("Editor"))
    @tabs.addTab(@tabs.newTabSpec("scripts").setContent(55557).setIndicator("Scripts"))
    @tabs.setOnTabChangedListener(proc{|t| tab_changed(t)})
    self.content_view = @tabs
  end
    
  def edit_script
    @edit_script
  end
    
  #
  # Menu Items
  #

  def on_create_options_menu(m)
    super

    mi = m.add("Execute")
    mi.set_icon R::drawable::ic_menu_slideshow
    mi.show_as_action = MenuItem::SHOW_AS_ACTION_IF_ROOM if MenuItem.const_defined?("SHOW_AS_ACTION_IF_ROOM")

    mi = m.add("Save")
    mi.set_icon R::drawable::ic_menu_save
    mi.show_as_action = MenuItem::SHOW_AS_ACTION_IF_ROOM if MenuItem.const_defined?("SHOW_AS_ACTION_IF_ROOM")

    mi = m.add("New")
    mi.set_icon R::drawable::ic_menu_add
    mi.show_as_action = MenuItem::SHOW_AS_ACTION_IF_ROOM if MenuItem.const_defined?("SHOW_AS_ACTION_IF_ROOM")

    mi = m.add("Edit History")
    mi.set_icon R::drawable::ic_menu_recent_history

    mi = m.add("Toggle Line Numbers")
    mi = m.add("Go to Line Number")
    mi = m.add("Clear IRB Output")
    mi = m.add("Copy IRB Output to Editor")
    mi = m.add("Reload Scripts List")
    mi = m.add("Reload demos from assets")
    mi = m.add("About")
    mi.set_icon R::drawable::ic_menu_info_details

    true
  end

  def on_options_item_selected(mi)
    case mi.title.to_s
    when "Save"
      save(@edit_name.getText.toString, @edit_script.getText.toString)
    when "Execute"
      execute @edit_script.getText.toString,
               "[Running editor script (#{@edit_name.getText})]"
      @tabs.setCurrentTabByTag("irb")
    when "New"
      @edit_name.setText "untitled.rb"
      @edit_script.setText ""
      @tabs.setCurrentTabByTag("editor")
    when "Edit History"
      @edit_name.setText "untitled.rb"
      @edit_script.setText @history.join("\n")
      @tabs.setCurrentTabByTag("editor")
    when "Toggle Line Numbers"
       @edit_script.toggle_show_line_numbers
    when "Go to Line Number"
      et = edit_text(:hint => "Line number", :input_type => android.text.InputType::TYPE_CLASS_NUMBER)
      
      goto = proc do 
        @edit_script.requestFocus
        line = et.text.to_s.to_i - 1
        max_line = @edit_script.getLineCount
        line = max_line if line > max_line 
        line = 1 if line < 1 
        @edit_script.setSelection @edit_script.getLayout.getLineStart(line)
      end

      dialog = AlertDialog::Builder.new(@ruboto_java_instance).setTitle("Go to").setView(et).setPositiveButton("Go", goto).create
      dialog.window.soft_input_mode = android.view.WindowManager::LayoutParams::SOFT_INPUT_STATE_ALWAYS_VISIBLE
      dialog.show
    when "Clear IRB Output"
      @irb_text.text = ">> "
    when "Copy IRB Output to Editor"
      @edit_name.text = "untitled.rb"
      @edit_script.text = @irb_text.text.to_s
      @tabs.setCurrentTabByTag("editor")
    when "Reload Scripts List"
      @tabs.setCurrentTabByTag("scripts")
      load_script_list
    when "Reload demos from assets"
      toast recopyDemos("demo-scripts", Dir.pwd).join("\n")
      load_script_list
    when "About"
      AlertDialog::Builder.new(self).
        setTitle("About Ruboto IRB (demo script)").
        setView(scroll_view do
                  tv = text_view :padding => [5,5,5,5], :text => about_text
                  Linkify.addLinks(tv, Linkify::ALL)
                end).
        setPositiveButton("Ok", nil).
        create.
        show
    end

    true
  end

  def on_create_context_menu(menu, view, menu_info)
    mi = menu.add("Edit")
    mi = menu.add("Execute")
    mi = menu.add("Delete")

    true
  end

  def on_context_item_selected(mi)
    pos = mi.menu_info.position

    case mi.title.to_s
    when "Edit"
      edit @scripts_list[pos]
    when "Execute"
      begin
        execute IO.read(@scripts_list[pos]), "[Running #{@scripts_list[pos]}]"
      rescue
        toast "#{@scripts_list[pos]} not found!"
      end
    when "Delete"
      @confirm_delete = @scripts_list[pos]
      AlertDialog::Builder.new(@ruboto_java_instance).
              setMessage("Delete #{@confirm_delete}?").
              setCancelable(false).
              setPositiveButton("Yes", (proc{|d,w| my_dialog_click(d,w)})).
              setNegativeButton("No", (proc{|d,w| my_dialog_click(d,w)})).
              create.
              show
    end

    true
  end

  #
  # Delete confirmation dialog buttons
  #

  def my_dialog_click(dialog, which)
    if @confirm_delete and which == DialogInterface::BUTTON_POSITIVE
      begin 
        File.delete @confirm_delete
        toast "#{@confirm_delete} deleted"
        load_script_list
      rescue
        toast "Deleted failed!"
      end
      @confirm_delete = nil
    end
  end

  #
  # Tab change
  #

  def tab_changed(tab)
    @current_tab = tab
    if tab == "scripts"
        getSystemService(Context::INPUT_METHOD_SERVICE).
           hideSoftInputFromWindow(@tabs.getWindowToken, 0)
    end
  end

  #
  # Line number EditText
  #

  def set_script_padding(l, t, r, b)
    @edit_script.setPadding(l, t, r, b)
  end

  #
  # Key actions for keeping the history of the IRB EditText
  #

  def my_key_listener(view, key_code, event)
    rv = false
    if [KeyEvent::ACTION_DOWN, KeyEvent::ACTION_MULTIPLE].include? event.getAction
      if (@cursor > 0 and key_code == KeyEvent::KEYCODE_DPAD_UP) or 
         (@cursor < (@history.size - 1) and key_code == KeyEvent::KEYCODE_DPAD_DOWN)
        @history[@cursor] = @irb_edit.getText.toString if @cursor == (@history.size - 1)
        @cursor += (key_code == KeyEvent::KEYCODE_DPAD_UP) ? -1 : 1
        @irb_edit.setText @history[@cursor]
        rv = true
      end
    elsif key_code == KeyEvent::KEYCODE_ENTER
      line = @irb_edit.getText.toString
      line["\n"] = ""
      unless line == ""
        @history[-1] = line
        @history << ""
        @cursor = @history.size - 1
        @irb_edit.setText ""
        execute line
      end
      rv = true
    end
    rv
  end

  #
  # Save and restore state of history text
  #

  def on_save_instance_state(bundle)
    super
    @history[@cursor] = @irb_edit.getText.toString if @cursor == (@history.size - 1)
    bundle.putString("history", @history.join("\n"))
    bundle.putInt("cursor", @cursor)
  end

  def on_restore_instance_state(bundle)
    super
    @history = bundle.getString("history").nil? ? [""] : bundle.getString("history").split("\n")
    @history << "" if bundle.getString("history") and bundle.getString("history")[-1] == "\n"
    @cursor = bundle.getInt("cursor").nil? ? 0 : bundle.getInt("cursor")
    @irb_edit.setText @history[@cursor]
  end

  #
  # Support methods
  #

  def load_script_list
    @scripts_list = Dir.glob("*.rb").sort
    @scripts.reload_list(@scripts_list)
  end

  def edit script
    @edit_name.setText script
    @edit_script.setText IO.read(script).gsub("\r", "")
    @tabs.setCurrentTabByTag("editor")
  end

  def execute source, display=nil
    @tabs.setCurrentTabByTag("irb")

    old_out, $stdout = $stdout, StringIO.new
    begin
      @irb_text.append(display || source)
      rv = $main_binding.eval(source)
      $stdout, new_out = old_out, $stdout 
      @irb_text.append "\n#{new_out.string}=> #{rv.inspect}\n>> "
    rescue => e
      $stdout = old_out
      @irb_text.append "\n#{e.to_s}\n#{e.backtrace.join("\n")}\n>> "
    end
  end

  def save(name, source)
    begin
      File.open(name, 'w') {|file| file.write(source)}
      @tabs.setCurrentTabByTag("scripts")
      toast "Saved #{name}"
      load_script_list
      true
    rescue
      toast "Save failed!"
      false
    end
  end

  def recopyDemos(from, to)
    FileUtils.makedirs to
    rv = []
    getAssets.list(from).map(&:to_s).each do |s|
      if getAssets().list("#{from}/#{s}").length == 0
        buf = getAssets.open("#{from}/#{s}", android.content.res.AssetManager::ACCESS_BUFFER)
        contents = []
        b = buf.read
        until b == -1 do
          contents << b.chr
          b = buf.read
        end
        begin
          File.open("#{to}/#{s}", 'w') {|file| file.write(contents.join)}
          rv << "#{s} copied"
        rescue
          rv << "#{s} copy failed"
        end
      else
   					rv =  rv + recopyDemos("#{from}/#{s}", "#{to}/#{s}")
      end
    end
  end

  def explanation_text
"This demo begins to duplicate (i.e., not all features are implemented) the functionality of Ruboto IRB (written in Java) with a ruboto script (written in Ruby). There main difference is that it is easier to trigger the stack overflow (based on the stack limitations imposed by Android). This can be avoided by either reducing stack requirements (e.g., reducing the layout hierarchy) or using the stack utilities (e.g., wrapping \"require 'date'\" in a with_large_stack block). You can not execute any UI interactions in this separate thread (unless you force them to run on the UI thread using runOnUiThread(proc{<<ui_code>>}))"
  end

  def about_text
"Ruboto IRB is a UI for scripting Android using the Ruby language through JRuby.

Source code:
http://github.com/ruboto/ruboto-irb

Join us or give feedback:
http://groups.google.com/group/ruboto

Developers:
Charlie Nutter
Jan Berkel
Pascal Chatterjee (jruby-for-android project)
Scott Moyer
Uwe Kubosch

JRuby Project:
http://jruby.org

Icon:
Ruby Visual Identity Team
http://rubyidentity.org
CC ShareAlike 2.5"
  end
end
