require "/sdcard/jruby/ruboto.rb"

$activity.start_ruboto_activity "$ruboto_demo" do
  setup_content do
    linear_layout(:orientation => :vertical) do
      @et = edit_text
      linear_layout(:orientation => :horizontal) do
        button :text => "Hello, World"
        button :text => "Hello, Ruboto"
        button :text => "List"
      end
      @tv = text_view :text => "Click buttons or menu items:"
    end
  end

  when_creating_options_menu do |menu|
    add_menu("Hello, World") {my_click "Hello, World"}
    add_menu("Hello, Ruboto") {my_click "Hello, Ruboto"}
    add_menu("Exit") {finish}
  end

  when_on_clicked do |view|
    view.getText == "List" ? launch_list : my_click(view.getText)
  end

  def self.my_click(text)
      toast text
      @tv.setText "#{@tv.getText}\n#{text}"
      @et.setText text
  end

  def self.launch_list
    self.start_ruboto_activity("$my_list") do
      setTitle "Pick Something"
      setup_content {list_view :list => ["Hello, World", "Hello, Ruboto"]}
      when_item_clicked {|view, pos| toast(@list[pos]); finish}
    end
  end
end
