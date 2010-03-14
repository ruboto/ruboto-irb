require "/sdcard/jruby/ruboto.rb"

$activity.start_ruboto_activity "$ruboto_demo" do
  setup_content do
    linear_layout(:orientation => LinearLayout::VERTICAL) do
      @et = edit_text
      linear_layout do
        button :text => "Hello, World"
        button :text => "Hello, Ruboto"
        button :text => "List"
      end
      @tv = text_view :text => "Click buttons or menu items:"
    end
  end

  handle_create_options_menu do |menu|
    add_menu("Hello, World") {my_click "Hello, World"}
    add_menu("Hello, Ruboto") {my_click "Hello, Ruboto"}
    add_menu("Exit") {finish}
  end

  handle_click do |view|
    case view.getText
      when "List"
        launch_list
      else
        my_click(view.getText)
    end
  end

  def self.my_click(text)
    toast text
    @tv.append "\n#{text}"
    @et.setText text
  end

  def self.launch_list
    self.start_ruboto_activity("$my_list") do
      setTitle "Pick Something"
      @list = ["Hello, World", "Hello, Ruboto"]
      setup_content {list_view :list => @list}
      handle_item_click {|adapter_view, view, pos, item_id| toast(@list[pos]); finish}
    end
  end
end
