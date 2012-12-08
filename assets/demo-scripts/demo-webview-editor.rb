require 'ruboto/activity'
require 'ruboto/widget'
require 'ruboto/util/toast'
require 'webrick/htmlutils'
require 'webrick/httputils'

ruboto_import_widget :WebView, "android.webkit"
ruboto_import_widgets :ListView

class RubotoWebViewClient < android.webkit.WebViewClient
  attr_writer :context

  def shouldOverrideUrlLoading(wv, uri)
    query = uri.split('?')[1].split('&')
    name = WEBrick::HTTPUtils.unescape(query[0][5..-1].gsub('+', ' '))
    script = WEBrick::HTTPUtils.unescape(query[1][5..-1].gsub('+', ' '))
    File.open(name,"w") {|f| f.write script}
    @context.save(name)
    true
  end
end

class ScriptList
  def on_create(bundle)
    super
    setTitle "Ruboto Scripts"
    setContentView(list_view :list => Dir.glob("*.rb").sort, 
                              :on_item_click_listener => (proc{|av, v, p, i| ScriptEditor.edit(self, v.text)}))
  end
end

class ScriptEditor
  def self.edit(context, script)
    @@script = script
    context.start_ruboto_activity :class_name => "ScriptEditor"
  end

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
    super
    @wvc = RubotoWebViewClient.new
    @wvc.context = self

    @wv = web_view(:initial_scale => 100, :web_view_client => @wvc)
    @wv.settings.use_wide_view_port = true
    @wv.settings.java_script_enabled = true
    @wv.settings.built_in_zoom_controls = true
    @wv.loadDataWithBaseURL("file:#{Dir.pwd}", page(@@script), "text/html", nil, nil);
    self.content_view = @wv 
  end

  def save(name)
    runOnUiThread(proc{toast "Saved #{name}"; finish})
  end 
end

$irb.start_ruboto_activity :class_name => "ScriptList"
