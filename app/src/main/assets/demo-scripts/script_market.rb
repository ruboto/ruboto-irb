#######################################################
#
# script_market.rb (by Scott Moyer)
# 
# Uses ruboto.rb to create an interface for installing 
# scripts from various location
#
#######################################################

require 'ruboto/activity'
require 'ruboto/widget'
require 'ruboto/util/toast'
require 'ruboto/util/stack'

ruboto_import_widgets :TableLayout, :TableRow, :TextView, :EditText, :ScrollView, :ListView

# Use Java classes until we compile Ruby
#require "net/http"
java_import "org.apache.http.client.methods.HttpGet"
java_import "org.apache.http.impl.client.BasicResponseHandler"
java_import "org.apache.http.impl.client.DefaultHttpClient"

java_import "android.content.res.AssetManager"
java_import "android.content.Context"
java_import "android.os.Environment"

if Environment.getExternalStorageState == Environment::MEDIA_MOUNTED
  $SCRIPT_DIR = "/sdcard/jruby"
else
  $SCRIPT_DIR = $irb.getFilesDir.getAbsolutePath + "/scripts"
end

class Activity
  def get_remote_page url
    with_large_stack{DefaultHttpClient.new.execute(HttpGet.new(url), BasicResponseHandler.new)}
  end

  def save_script name, contents
    File.open("#{$SCRIPT_DIR}/#{name}","w") {|f| f.write contents}
    true
  end
end

$irb.start_ruboto_activity do
  def on_create(b)
    super
    setTitle "Script Market - Select a source"
    @default_list = ["Application asset demo scripts", "Ruboto github scripts"]
    load_preferences
    @list = @default_list + @options
    @list_view = list_view :list => @list, :on_item_click_listener => (proc{|av, v, p, i| item_clicked(p)})
    registerForContextMenu(@list_view)
    set_content_view(@list_view)
  end
  
  def on_create_options_menu(m)
    m.add("Add Github Source")
    m.add("Add Web Source")
    m.add("About")
    true
  end

  def on_options_item_selected(mi)
    case mi.title.to_s
    when "About" then start_ruboto_activity("AboutActivity")
    when "Add Github Source" then edit_github_source
    when "Add Web Source" then edit_web_source
    end

    true
  end

  def on_create_context_menu(m, v, mi)
    m.add("Edit")
    m.add("Delete")
    true
  end

  def on_context_item_selected(mi)
    pos = mi.menu_info.position

    case mi.title.to_s
    when "Edit"
      pos < 2 ? toast("Can't edit default entries") : edit(@list[pos])
      true
    when "Delete"
      pos < 2 ? toast("Can't delete default entries") : delete_source(@list[pos])
      true
    else
     false
    end
  end

  def item_clicked(pos)
    case (@list[pos]) 
    when @list[0] 
      pick_asset
    when @list[1] 
      pick_remote_script "github.com", 
        "ruboto/ruboto-irb/tree/master/assets/demo-scripts/?raw=true",
        "ruboto/ruboto-irb/raw/master/assets/demo-scripts/%s"
    else
      data = @option_data[@list[pos]]
      if data[0] == "github"
        user, project, branch, dir = data[2..-1]
        site = "github.com"
        list_path = "#{user}/#{project}/tree/#{branch}/#{dir}/?raw=true"
        get_path = "#{user}/#{project}/raw/#{branch}/#{dir}/%s"
      else
        site, list_path, get_path = data[2..-1]
      end
      pick_remote_script site, list_path, get_path
    end
  end

  def pick_asset
    launch_script_list(getAssets.list('demo-scripts').map(&:to_s)) do |s|
      buf = getAssets.open("demo-scripts/#{s}", AssetManager::ACCESS_BUFFER)
      contents = []
      b = buf.read
      until b == -1 do; contents << b.chr; b = buf.read; end
      self.toast_result save_script(s, contents.join), "#{s} copied", "#{s} copy failed"
    end
  end 

  def launch_script_list(list, &block)
    start_ruboto_activity do
      @@block = block
      @@list = list

      def on_create(b)
        super
        setTitle "Pick a Script"
        set_content_view(
          list_view(:list => @@list, :on_item_click_listener => (proc{|av, v, p, i| item_clicked(p)}))
        )
      end
  
      def item_clicked(pos)
        @@block.call(@@list[pos])
        finish
      end
    end
  end

  def pick_remote_script(site, list_path, get_path)
    launch_script_list(get_remote_page("http://#{site}/#{list_path}").scan(/>([^<]*.rb)<\//).flatten) do |s|
      self.toast_result(
        save_script(s, get_remote_page("http://#{site}/#{get_path}" % s)),
        "#{s} downloaded", "#{s} download failed")
    end
  end 

  def load_preferences
    e = getPreferences(Context::MODE_PRIVATE)
    @options = e.getString("ScriptSourceOptions", "").split("|")
    @option_data = {}
    @options.each {|k| @option_data[k] = e.getString("ScriptSourceOption-#{k}", "").split("|")} 
    @list = @default_list + @options
  end

  def reload_list
    load_preferences
    @list_view.reload_list(@list)
  end

  def edit(name)
    item_data = @option_data[name]
    if item_data[0] == "github"
      edit_github_source *(item_data[1..-1])
    else
      edit_web_source *(item_data[1..-1])
    end
  end

  def save_source(*args)
    e = getPreferences(Context::MODE_PRIVATE).edit
    unless @list.include? args[1]
      @options << args[1] 
      e.putString("ScriptSourceOptions", @options.join("|"))
    end
    e.putString("ScriptSourceOption-#{args[1]}", args.join("|"))
    e.commit
    reload_list
  end

  def delete_source name
    @options = @options - [name]
    e = getPreferences(Context::MODE_PRIVATE).edit
    e.putString("ScriptSourceOptions", @options.join("|"))
    e.remove("ScriptSourceOption-#{name}")
    e.commit
    reload_list
  end

  def edit_github_source(name="", user="", project="", branch="", dir="")
    caller = self
    start_ruboto_activity do
      @@name = name
      @@user = user
      @@project = project
      @@branch = branch
      @@dir = dir
      @@caller = caller

      def on_create(b)
        super
        setTitle "Github Source"

        tl = table_layout do
          table_row do
            text_view :text => "Name:"
            @name = edit_text :text => @@name, :hint => "displayed in list of sources"
          end
          table_row do
            text_view :text => "User:"
            @user = edit_text :text => @@user, :hint => "e.g., ruboto"
          end
          table_row do
            text_view :text => "Project:"
            @project = edit_text :text => @@project, :hint => "ruboto-irb"
          end
          table_row do
            text_view :text => "Branch:"
            @branch = edit_text :text => @@branch, :hint => "e.g., master"
          end
          table_row do
            text_view :text => "Directory:"
            @dir = edit_text :text => @@dir, :hint => "e.g., assets/demo-scripts"
          end
        end
        tl.setColumnStretchable(1, true)

        set_content_view(tl)
      end

      def on_create_options_menu(m)
        m.add("Save")
        true
      end

      def on_options_item_selected(mi)
        @@caller.save_source("github", @name.getText.toString, @user.getText.toString, 
            @project.getText.toString, @branch.getText.toString, @dir.getText.toString)
        finish
        true
      end
    end
  end

  def edit_web_source(name="", site="", list_path="", get_path="")
    caller = self
    start_ruboto_activity do
      @@name = name
      @@site = site
      @@list_path = list_path
      @@get_path = get_path
      @@caller = caller

      def on_create(b)
        super
        setTitle "Generic Web Source"

        tl = table_layout do
          table_row do
            text_view :text => "Name:"
            @name = edit_text :text => @@name, :hint => "displayed in list of sources"
          end
          table_row do
            text_view :text => "Site:"
            @site = edit_text :text => @@site, :hint => "domain name"
          end
          table_row do
            text_view :text => "List path:"
            @list_path = edit_text :text => @@list_path, :hint => "page for scripts list"
          end
          table_row do
            text_view :text => "Script path:"
            @get_path = edit_text :text => @@get_path, :hint => "page for script (%s=script)"
          end
        end
        tl.setColumnStretchable(1, true)
        set_content_view(tl)
      end

      def on_create_options_menu(m)
        m.add("Save")
        true
      end

      def on_options_item_selected(mi)
        @@caller.save_source("web", @name.getText.toString, @site.getText.toString, 
          @list_path.getText.toString, @get_path.getText.toString)
        finish
        true
      end
    end
  end
end

class AboutActivity
  def on_create(b)
    super
    setTitle "About Script Market"
    set_content_view(
        scroll_view do
          text_view :vertical_scroll_bar_enabled => true, :text => "Version: 1.1

Author: Scott Moyer

Description: Script Market allows you to find and install scripts from various locations. Two locations are provided by default:

1) Assets: The assets/demo-scripts directory in Ruboto IRB application. These are scripts demonstrating how to use Ruboto.

2) Ruboto Github: The assets/demo-scripts in the main branch of the ruboto-irb project on Github. These are many of the same scripts in the application assets directory, but the github project directory will contain changes and additions subsequent to the application release.

In addition to the default script sources, you can add your own. Right now there are two types:

1) Github: Specifying the user name, project, branch, and directory

2) Generic: Specifying site (no http://), list directory (any page that contains script names wrapped in some HTML tag) and script directory (use %s to represent the name of the script selected from the list)

For example, the default Github scripts could also be obtained by specifying:

User: ruboto
Project: ruboto-irb
Branch: master
Directory: assets/demo-scripts

The same results could be obtained by creating a web source specifying:

Site: github.com
List path: ruboto/ruboto-irb/tree/master/assets/demo-scripts/?raw=true
Get path: ruboto/ruboto-irb/raw/master/assets/demo-scripts/%s
"
        end)
  end
end
