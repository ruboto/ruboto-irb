#######################################################
#
# demo-ruboto-irb.rb (by Scott Moyer)
# 
# This demo duplicates the functionality of 
# Ruboto IRB (written in Java) with a ruboto 
# script (written in Ruby).
#
#######################################################

require 'ruboto'
require 'ruboto/generate'
confirm_ruboto_version(10, false)

java_import "android.view.WindowManager"
java_import "android.view.Gravity"
java_import "android.view.KeyEvent"
java_import "android.text.util.Linkify"
java_import "android.app.AlertDialog"
java_import "android.content.DialogInterface"
java_import "android.content.Context"
java_import "android.text.method.ScrollingMovementMethod"

ruboto_import_widgets :TabHost, :LinearLayout, :FrameLayout, :TabWidget, 
  :Button, :EditText, :TextView, :ListView, :ScrollView
ruboto_generate_widget(android.widget.EditText => "org.ruboto.widget.RubotoEditText")

require 'stringio'
$main_binding = self.instance_eval{binding}

$activity.start_ruboto_activity("$ruboto_irb") do
  getWindow.setSoftInputMode(
             WindowManager::LayoutParams::SOFT_INPUT_STATE_VISIBLE | 
             WindowManager::LayoutParams::SOFT_INPUT_ADJUST_RESIZE)

  #
  # UI setup
  #

  setup_content do
    @history = [""]
    @cursor = 0

    @tabs = TabHost.new(self, nil) # Needs to be created this way to avoid bug introduced Android >= 3.0
    @tab_container = linear_layout(:orientation => :vertical, :height => :fill_parent, :parent => @tabs)

    tab_widget(:id => AndroidIds::tabs, :parent => @tab_container)
    frame_layout(:id => AndroidIds::tabcontent, :height => :fill_parent, :parent => @tab_container) do
      linear_layout(:id => 55555, :height => :fill_parent, :orientation => :vertical) do
        @irb_edit = edit_text :lines => 1, :on_key_listener => @on_key_listener
        @irb_text = text_view :text => "#{explanation_text}\n\n>> ", :height => :fill_parent, 
                        :gravity => (Gravity::BOTTOM | Gravity::CLIP_VERTICAL), 
                        :text_color => android.graphics.Color::WHITE,
                        :movement_method => ScrollingMovementMethod.new;
      end
      linear_layout(:id => 55556, :orientation => :vertical) do
        @edit_name   = edit_text :text => "untitled.rb"
        @edit_script = ruboto_edit_text(:hint => "Enter source code here.", #:text => script_code,
                                          :gravity => Gravity::TOP, :horizontally_scrolling=> true,
                                          :raw_input_type => (android.text.InputType::TYPE_TEXT_FLAG_NO_SUGGESTIONS | 
                                                              android.text.InputType::TYPE_TEXT_FLAG_MULTI_LINE),
                                          :typeface => android.graphics.Typeface::MONOSPACE, :scroll_container => true, 
                                          :filters => [android.text.InputFilter::LengthFilter.new(1000000)].to_java(android.text.InputFilter),
                                          :layout => ({:height= => :fill_parent, :width= => :fill_parent, :weight= => 1}))
      end
      @scripts = list_view :id => 55557, :list => [], 
                            :on_item_click_listener =>   proc{|av, v, p, i| edit @scripts_list[p]}
    end

    @edit_script.initialize_ruboto_callbacks &@line_number_draw

    registerForContextMenu(@scripts)
    load_script_list
    @tabs.setup
    @tabs.addTab(@tabs.newTabSpec("irb").setContent(55555).setIndicator("IRB"))
    @tabs.addTab(@tabs.newTabSpec("editor").setContent(55556).setIndicator("Editor"))
    @tabs.addTab(@tabs.newTabSpec("scripts").setContent(55557).setIndicator("Scripts"))
    @tabs.setOnTabChangedListener(@on_tab_change_listener)
    @tabs
  end

  #
  # Menu Items
  #

  handle_create_options_menu do |menu|
    add_menu("Save", R::drawable::ic_menu_save) do 
      save(@edit_name.getText.toString, @edit_script.getText.toString)
    end

    add_menu("Execute", Ruboto::R::drawable::ic_menu_play) do
      execute @edit_script.getText.toString,
               "[Running editor script (#{@edit_name.getText})]"
      @tabs.setCurrentTabByTag("irb")
    end

    add_menu("New", R::drawable::ic_menu_add) do 
      @edit_name.setText "untitled.rb"
      @edit_script.setText ""
      @tabs.setCurrentTabByTag("editor")
    end

    add_menu("Edit history", R::drawable::ic_menu_recent_history) do
      @edit_name.setText "untitled.rb"
      @edit_script.setText @history.join("\n")
      @tabs.setCurrentTabByTag("editor")
    end

    add_menu("Toggle Line Numbers") do
       @edit_script.toggle_show_line_numbers
    end
    
    add_menu("About", R::drawable::ic_menu_info_details) do
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

    add_menu("Reload scripts list") do 
      @tabs.setCurrentTabByTag("scripts")
      load_script_list
    end

    add_menu("Reload demos from assets") do 
      getAssets.list('demo-scripts').map(&:to_s).each do |s|
        buf = getAssets.open("demo-scripts/#{s}", AssetManager::ACCESS_BUFFER)
        contents = []
        b = buf.read
        until b == -1 do
          contents << b.chr
          b = buf.read
        end
        toast_result save(s, contents.join), "#{s} copied", "#{s} copy failed"
      end
    end

    true
  end

  #
  # Script list context menu items
  #

  handle_create_context_menu do |menu, view, menu_info|
    add_context_menu("Edit") {|pos| edit @scripts_list[pos]}

    add_context_menu("Execute") do |pos| 
      begin
        execute IO.read(@scripts_list[pos]), "[Running #{@scripts_list[pos]}]"
      rescue
        toast "#{@scripts_list[pos]} not found!"
      end
    end

    add_context_menu("Delete") do |pos| 
      @confirm_delete = @scripts_list[pos]
      AlertDialog::Builder.new(self).
              setMessage("Delete #{@confirm_delete}?").
              setCancelable(false).
              setPositiveButton("Yes", @dialog_click).
              setNegativeButton("No", @dialog_click).
              create.
              show
    end

    true
  end

  #
  # Delete confirmation dialog buttons
  #

  @dialog_click = proc do |dialog, which|
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

  @on_tab_change_listener = proc do |tab|
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

  @line_number_draw = Proc.new do 
    @show_line_numbers = context.getPreferences(Context::MODE_PRIVATE).getBoolean("LineNumbers", true)
    @line_rect = android.graphics.Rect.new
    @paint = android.graphics.Paint.new
    @paint.color = (text_colors.default_color & android.graphics.Color.argb(0xAA, 0xFF, 0xFF, 0xFF))
    @paint.text_size = (text_size * 0.6)  
    @current_left = @default_left = padding_left
    
    def toggle_show_line_numbers
      @show_line_numbers = !@show_line_numbers
      prefs_editor = context.getPreferences(Context::MODE_PRIVATE).edit
      prefs_editor.putBoolean("LineNumbers", @show_line_numbers)
      prefs_editor.commit
      invalidate
    end
    
    def min(a, b)
      a > b ? b : a
    end
    
    def max(a, b)
      a > b ? a : b
    end

    def calculate_padding
      new_padding = @default_left + ((!@show_line_numbers || line_count == 0) ? 0 :
                                    ((Math.log10(line_count).to_i + 1) * @paint.text_size))

      unless new_padding == @current_left
        @current_left = new_padding
        set_padding(new_padding, padding_top, padding_right, padding_bottom)
      end
    end

    def on_draw(canvas)
      if (@show_line_numbers)
        topLineNumber = max(1, ((scroll_y - extended_padding_top) / line_height))
        bottomLineNumber = min(line_count, topLineNumber + (height / line_height).to_i)
        
        canvas.save

        canvas.clipRect(0, 
                        extended_padding_top + scroll_y, 
                        padding_left + scroll_x,
                        bottom - top - extended_padding_bottom + scroll_y)
                        
        topLineNumber.upto(bottomLineNumber) do |i|
          getLineBounds(i - 1, @line_rect)
          canvas.drawText(i.to_s, @default_left + scroll_x, @line_rect.bottom - 8, @paint)
        end

        canvas.restore
      end
      calculate_padding
    end
  end

  #
  # Key actions for keeping the history of the IRB EditText
  #

  @on_key_listener = proc do |view, key_code, event|
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

  handle_save_instance_state do |bundle|
    @history[@cursor] = @irb_edit.getText.toString if @cursor == (@history.size - 1)
    bundle.putString("history", @history.join("\n"))
    bundle.putInt("cursor", @cursor)
  end

  handle_restore_instance_state do |bundle|
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

  def save name, source
    begin
      File.open(name, 'w') {|file| file.write(source)}
      @tabs.setCurrentTabByTag("scripts")
      toast "Saved #{name}"
      load_script_list
    rescue
      toast "Save failed!"
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

