#######################################################
#
# demo-ruboto.rb (by Scott Moyer)
# 
# A simple look at how to generate and 
# use a RubotoActivity.
#
#######################################################

require 'ruboto/activity'
require 'ruboto/widget'
require 'ruboto/menu'
require 'ruboto/util/toast'
confirm_ruboto_version(10, false)

#
# ruboto_import_widgets imports the UI widgets needed
# by the activities in this script. ListView and Button 
# come in automatically because those classes get extended.
#

ruboto_import_widgets :LinearLayout, :EditText, :TextView, :ListView, :Button

#
# $activity is the Activity that launched the 
# script engine. The start_ruboto_activity
# method creates a new RubotoActivity to work with.
# After launch, the new activity can be accessed 
# through the $ruboto_demo (in this case) global.
# You man not need the global, because the block
# to start_ruboto_activity is executed in the 
# context of the new activity as it initializes. 
#
$activity.start_ruboto_activity "$ruboto_demo" do
  #
  # on_create uses methods created through
  # ruboto_import_widgets to build a UI. All
  # code is executed in the context of the 
  # activity.
  #
  def on_create(bundle)
    setContentView(
      linear_layout(:orientation => LinearLayout::VERTICAL) do
        @et = edit_text
        linear_layout do
          button :text => "Hello, World",  :on_click_listener => proc{|v| my_click(v.getText)}
          button :text => "Hello, Ruboto", :on_click_listener => proc{|v| my_click(v.getText)}
          button :text => "List",          :on_click_listener => proc{|v| launch_list}
        end
        @tv = text_view :text => "Click buttons or menu items:"
      end)
  end

  #
  # All "handle" methods register for the 
  # corresponding callback (in this case 
  # OnCreateOptionsMenu. Creates menus that
  # execute the corresponding block (still
  # in the context of the activity)
  #
  handle_create_options_menu do |menu|
    add_menu("Hello, World") {my_click "Hello, World"}
    add_menu("Hello, Ruboto") {my_click "Hello, Ruboto"}
    add_menu("Exit") {finish}
    true
  end

  #
  # Extra singleton methods for this activity
  # need to be declared with self. This one 
  # handles some of the button and menu clicks.
  # 
  def my_click(text)
    toast text
    @tv.append "\n#{text}"
    @et.setText text
  end

  #
  # Launches a separate activity for displaying
  # a ListView.
  #
  def launch_list
    self.start_ruboto_activity("$my_list") do
      setTitle "Pick Something"
      @list = ["Hello, World", "Hello, Ruboto"]
      def on_create(bundle)
        setContentView(list_view :list => @list, 
          :on_item_click_listener => proc{|av, v, pos, item_id| toast(@list[pos]); finish})
      end
    end
  end
end

