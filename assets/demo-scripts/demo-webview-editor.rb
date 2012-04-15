require 'ruboto/activity'
require 'ruboto/widget'
require 'ruboto/util/toast'
require 'ruboto/generate'
require 'webrick/htmlutils'
require 'webrick/httputils'

ruboto_import_widget :WebView, "android.webkit"
ruboto_generate(android.webkit.WebViewClient => "org.ruboto.callbacks.RubotoWebViewClient")
ruboto_import_widgets :ListView

$activity.start_ruboto_activity("$script_list") do
  setTitle "Ruboto Scripts"

  def on_create(bundle)
    setContentView(list_view :list => Dir.glob("*.rb").sort, :on_item_click_listener => @handle_item_click)
  end

  @handle_item_click = Proc.new do |adapter_view, view, pos, item_id| 
    edit view.getText
  end
end

def edit(script)
  $script_list.start_ruboto_activity "$webview_editor" do
    @script = script
    
    def page(name)
      script = File.exists?(name) ? WEBrick::HTMLUtils.escape(IO.read(name)) : ""
      pwd = Dir.pwd
      %Q[
        <html>
          <head>
            <link rel="stylesheet" href="file:#{pwd}/codemirror/lib/codemirror.css">
            <script src="file:#{pwd}/codemirror/lib/codemirror.js"></script>
            <script src="file:#{pwd}/codemirror/mode/ruby/ruby.js"></script>
            <style>
              .CodeMirror-scroll {height: auto; overflow-y: hidden; overflow-x: auto; width: 100%;}
            </style>
          </head>
          <body leftmargin='0px' topmargin='0px' marginwidth='0px' marginheight='0px'>
            <form action='save'>
              Name: <input name='name' value='#{name}' />
              <input type='submit' value='Save' /><br/>
              <textarea id='code' name='code' rows='25' cols='100'>#{script}</textarea>
            </form>
            <script>
              CodeMirror.fromTextArea(document.getElementById("code"),
                {mode: "text/x-ruby", tabMode: "indent", matchBrackets: true, lineNumbers: true, indentUnit: 2});
            </script>
          </body>
        </html>
      ]
    end
    
    def on_create(bundle)
      @wv = web_view(:initial_scale => 100, :web_view_client => @wvc)
      @wv.settings.use_wide_view_port = true
      @wv.settings.java_script_enabled = true
      @wv.settings.built_in_zoom_controls = true
      @wv.loadDataWithBaseURL("file:#{Dir.pwd}", page(@script), "text/html", nil, nil);
      self.content_view = @wv 
    end
      
    @wvc = RubotoWebViewClient.new_with_callbacks do
      def on_should_override_url_loading(wv, uri)
        query = uri.split('?')[1].split('&')
        name = WEBrick::HTTPUtils.unescape(query[0][5..-1].gsub('+', ' '))
        script = WEBrick::HTTPUtils.unescape(query[1][5..-1].gsub('+', ' '))
        File.open(name,"w") {|f| f.write script}
        $webview_editor.runOnUiThread(proc{$webview_editor.toast "Saved #{name}"; $webview_editor.finish})
        true
      end
    end
  end
end

