#######################################################
#
# demo-ruboto-irb.rb (by Scott Moyer)
# 
# This demo duplicates the functionality of 
# Ruboto IRB (written in Java) with a ruboto 
# script (written in Ruby).
#
#######################################################

require "ruboto.rb"
confirm_ruboto_version(6, false)

java_import "android.view.WindowManager"
java_import "android.view.Gravity"
java_import "android.view.KeyEvent"
java_import "android.text.util.Linkify"
java_import "android.app.AlertDialog"
java_import "android.content.DialogInterface"
java_import "android.content.Context"
java_import "android.text.method.ScrollingMovementMethod"

ruboto_import "org.ruboto.callbacks.RubotoOnTabChangeListener"
ruboto_import "org.ruboto.callbacks.RubotoOnKeyListener"

ruboto_import_widgets :TabHost, :LinearLayout, :FrameLayout, :TabWidget, 
  :Button, :EditText, :TextView, :ListView, :ScrollView

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

    @tabs = tab_host do
      linear_layout(:orientation => LinearLayout::VERTICAL, :height => :fill_parent) do
        tab_widget(:id => AndroidIds::tabs)
        frame_layout(:id => AndroidIds::tabcontent, :height => :fill_parent) do
          linear_layout(:id => 55555, :height => :fill_parent,
                        :orientation => LinearLayout::VERTICAL) do
            @irb_edit = edit_text :lines => 1, :on_key_listener => @on_key_listener
            @irb_text = text_view :text => "#{explanation_text}\n\n>> ", :height => :fill_parent, 
                            :gravity => (Gravity::BOTTOM | Gravity::CLIP_VERTICAL), 
                            :text_color => 0xffffffff, 
                            :movement_method => ScrollingMovementMethod.new;
          end
          linear_layout(:id => 55556, :orientation => LinearLayout::VERTICAL) do
            @edit_name   = edit_text :text => "untitled.rb"
            @edit_script = edit_text :height => :fill_parent, :hint => "Enter source code here.", 
                                       :gravity => Gravity::TOP, :horizontally_scrolling=> true
          end
          @scripts = list_view :id => 55557, :list => []
        end
      end
    end
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
  # Script list item click
  #

  handle_item_click do |adapter_view, view, pos, item_id|
    edit @scripts_list[pos]
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

    add_menu("About", R::drawable::ic_menu_info_details) do
      AlertDialog::Builder.new(self).
        setTitle("About Ruboto IRB v0.2").
        setView(scroll_view do
                  tv = text_view :padding => [5,5,5,5], :text => about_text
                  Linkify.addLinks(tv, Linkify::ALL)
                end).
        setPositiveButton("Ok", self).
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
              setPositiveButton("Yes", self).
              setNegativeButton("No", self).
              create.
              show
    end

    true
  end

  #
  # Delete confirmation dialog buttons
  #

  handle_click do |dialog, which|
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

  @on_tab_change_listener = RubotoOnTabChangeListener.new.handle_tab_changed do |tab|
    if tab == "scripts"
        getSystemService(Context::INPUT_METHOD_SERVICE).
           hideSoftInputFromWindow(@tabs.getWindowToken, 0)
    end
  end

  #
  # Key actions for keeping the history of the IRB EditText
  #

  @on_key_listener = RubotoOnKeyListener.new.handle_key do |view, key_code, event|
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

  def self.load_script_list
    @scripts_list = Dir.glob("*.rb").sort
    @scripts.reload_list(@scripts_list)
  end

  def self.edit script
    @edit_name.setText script
    @edit_script.setText IO.read(script).gsub("\r", "")
    @tabs.setCurrentTabByTag("editor")
  end

  def self.execute source, display=nil
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

  def self.save name, source
    begin
      File.open(name, 'w') {|file| file.write(source)}
      @tabs.setCurrentTabByTag("scripts")
      toast "Saved #{name}"
      load_script_list
    rescue
      toast "Save failed!"
    end
  end

  def self.explanation_text
"This demo duplicates the functionality of Ruboto IRB (written in Java) with a ruboto script (written in Ruby). There are a few differences:

1) We're not currently copying the demo scripts if the scripts directory doesn't exist. The main reason for this is because the very fact that you're running this demo means that the scripts directory exists.

2) There is a bug that you can trigger by doing a \"require 'date'\". It causes a StackOverflow exception to be caught on the Java side. Most requires seem to work, but this one (actually another script that date requires) causes the exception. I haven't tracked it down yet."
  end

  def self.about_text
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

JRuby Project:
http://jruby.org

Icon:
Ruby Visual Identity Team
http://rubyidentity.org
CC ShareAlike 2.5"
  end
end
