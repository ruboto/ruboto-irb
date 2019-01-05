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
require 'ruboto/util/toast'

java_import "android.view.MenuItem"

#
# ruboto_import_widgets imports the UI widgets needed
# by the activities in this script. 
#

ruboto_import_widgets :LinearLayout, :EditText, :TextView, :ListView, :Button

$options = ["Hello", "World", "Mr", "Ruboto"]

#
# $activity is the Activity that launched the 
# script engine. The start_ruboto_activity
# method creates a new RubotoActivity to work with.
#
$irb.start_ruboto_activity do
  #
  # on_create uses methods created through
  # ruboto_import_widgets to build a UI. 
  # All code will be executed in the context  
  # of the activity.
  #
  def on_create(bundle)
    super

    self.title = "Simple Ruboto Demo"

    # Easy way to allow the list activity to call back into this activity
    $main_activity = self

    setContentView(
      linear_layout(:orientation => LinearLayout::VERTICAL) do
        @et = edit_text
        linear_layout do
          $options.each do |i|
            button :text => i,  :on_click_listener => proc{|v| my_click(v.text)}
          end

          button :text => "List", :on_click_listener => proc{|v| launch_list}
        end
        @tv = text_view :text => "Click buttons or menu items:"
      end)
  end

  #
  # Creates menus items
  #

  def on_create_options_menu(m)
    $options.each do |i|
      mi = m.add(i)
      mi.show_as_action = MenuItem::SHOW_AS_ACTION_IF_ROOM if MenuItem.const_defined?("SHOW_AS_ACTION_IF_ROOM")
    end

    m.add("Exit")

    true
  end

  def on_options_item_selected(mi)
    case mi.title.to_s
    when "Exit"
      finish
    else
      my_click(mi.title.to_s)
    end

    true
  end

  #
  # Handles text update for clicks.
  # 
  def my_click(text)
    toast "Hello, #{text}"
    @tv.append "\nHello, #{text}"
    @et.text = "Hello, #{text}"
  end

  #
  # Launches a separate activity for displaying
  # a ListView.
  #
  def launch_list
    self.start_ruboto_activity do
      def on_create(bundle)
        super
        self.title = "Pick One"
        setContentView(list_view :list => $options, 
          :on_item_click_listener => proc{|av, v, pos, i| puts $main_activity.my_click(v.text.to_s); finish})
      end
    end
  end
end

