#######################################################
#
# demo-irb-server.rb (by Scott Moyer)
# 
# Creates a webrick server to allow you to edit the 
# scripts on the device from your browser. You can also
# send arbitrary code to execute on the device. 
#
#######################################################

require 'ruboto/activity'
require 'ruboto/widget'
require "ruboto/util/stack"
require 'ruboto/service'

SERVER_PORT = 8005

$main_binding = self.instance_eval{binding}

$default_exception_handler = proc do |t, e|
                               android.util.Log.e "Ruboto", e.to_s
                               e.stack_trace.each{|i| android.util.Log.e "Ruboto", i.to_s}
                             end

#####################################################

ruboto_import_widgets :LinearLayout, :TextView, :ToggleButton

$activity.start_ruboto_activity("$irb_activity") do
  def on_create(bundle)
    $ui_thread = java.lang.Thread.currentThread
    $ui_thread.setUncaughtExceptionHandler($default_exception_handler)

    setContentView(
      linear_layout(:orientation => :vertical) do
        @start_button = toggle_button :enabled => false, :checked => ($server && $server.running?),
                          :layout => {:width= => :wrap_content}, :on_click_listener => (proc{$server.toggle})
        @status_text = text_view :text => "Initializing..."
      end
    )
  end

  def set_status(status_text, button_enabled=@start_button.enabled?)
    @start_button.enabled = button_enabled
    @status_text.text = status_text
  end

  def start
    @start_button.performClick unless $server.running?
  end

  def stop
    @start_button.performClick if $server.running?
  end
end

#####################################################

Thread.with_large_stack do
  java.lang.Thread.currentThread.setUncaughtExceptionHandler($default_exception_handler)

  require 'webrick/httpserver.rb' 
  require 'stringio'
  
  class Server
    def self.wifi_connected?
      ip_address != "localhost"
    end

    def self.ip_address
      ip = $context.get_system_service($context.class::WIFI_SERVICE).connection_info.ip_address
      return "localhost" if ip == 0
      [0, 8, 16, 24].map{|n| ((ip >> n) & 0xff).to_s}.join(".")
    end
  
    def initialize(port)
      @port = port
    end
    
    def running?
      not @server.nil?
    end
    
    def __start
      title = "IRB server running on #{Server.ip_address}:#{SERVER_PORT}"
      text = "Rerun the script to stop the server."
      ticker = "IRB server running on #{Server.ip_address}:#{SERVER_PORT}"
      icon = android.R::drawable::stat_sys_upload

      notification = android.app.Notification.new(icon, ticker, java.lang.System.currentTimeMillis)
      intent = android.content.Intent.new
      intent.setAction("org.ruboto.intent.action.LAUNCH_SCRIPT")
      intent.addCategory("android.intent.category.DEFAULT")
      intent.putExtra("org.ruboto.extra.SCRIPT_NAME", "demo-irb-server.rb")
      pending = android.app.PendingIntent.getActivity($irb_activity, 0, intent, 0)
      notification.setLatestEventInfo($irb_activity.getApplicationContext, title, text, pending)
      $irb_service.startForeground(1, notification)

      @server.start
      @after_stop.call if @after_stop
    end

    def start
      unless @server
        @before_start.call if @before_start

        Thread.with_large_stack do 
          java.lang.Thread.currentThread.setUncaughtExceptionHandler($default_exception_handler)
          
          @server = WEBrick::HTTPServer.new(:Port => @port, :DocumentRoot => Dir.pwd, :AccessLog => [])
          @server.mount("/", EvalServlet, nil)

          @after_start.call if @after_start
          $irb_activity.start_ruboto_service("$irb_service") do
            def on_start_command(intent, flags, startId)
              Thread.with_large_stack do 
                java.lang.Thread.currentThread.setUncaughtExceptionHandler($default_exception_handler)
                $server.__start
              end        
              
              self.class::START_NOT_STICKY
            end
          end
        end
      end
    end

    def stop
      if @server
        @before_stop.call if @before_stop

        @server.shutdown
        @server = nil
        $irb_activity.stop_service android.content.Intent.new($irb_activity, RubotoService.java_class)
        $irb_service = nil
      end
    end
    
    def toggle
      running? ? stop : start
    end

    def before_start &block
      @before_start = block
    end
    
    def after_start &block
      @after_start = block
    end

    def before_stop &block
      @before_stop = block
    end
    
    def after_stop &block
      @after_stop = block
    end
  end
  
  class EvalServlet < WEBrick::HTTPServlet::AbstractServlet
    def initialize(arg1, arg2)
      @@history ||= []
      @@string_io ||= StringIO.new
      $stdout = @@string_io
  
      @@exception_handler ||= proc do |t, e|
        puts e.to_s
        e.stack_trace.each{|i| puts i.to_s}
        $irb_activity.synchronized{$irb_activity.notify}
      end
      $ui_thread.setUncaughtExceptionHandler(@@exception_handler)
  
      super(arg1, arg2)
    end
  
    def eval_page(title="Ruboto IRB", body=eval_form)
      body = body.join("<hr/>") if body.is_a?(Array)
      %Q[
        <html>
          <head>
            <link rel="shortcut icon" href="#" />
            <link rel="stylesheet" href="/codemirror/lib/codemirror.css">
            <script src="/codemirror/lib/codemirror.js"></script>
            <script src="/codemirror/mode/ruby/ruby.js"></script>
            <style>
              .CodeMirror {border: 1px solid black;}
              .CodeMirror-scroll {
                height: auto;
                min-height: 200px;
                overflow-y: hidden;
                overflow-x: auto;
                width: 100%;
              }
              .cm-s-default span.cm-arrow { color: red; }
            </style>
          </head>
          <body>
            <a href="/">Home (Eval)</a> | 
            <a href="/history">History</a> | 
            <a href='/scripts'>Scripts</a> | 
            <a href='/stop'>Stop Server</a>
            <h2>#{title}</h2>
            #{body}
            <script>
              for (var i=0, e=document.getElementsByName("code"); i<e.length; i++) {
                CodeMirror.fromTextArea(e.item(i), {
                  mode: "text/x-ruby",
                  tabMode: "indent",
                  matchBrackets: true,
                  lineNumbers: true,
                  indentUnit: 2
                });
              }
            </script>
          </body>
        </html>
      ]
    end
  
    def eval_form(code="", rows=25)
      %Q[
        <form action='/' method='post'>
          <input type='submit' value='Eval' />
          <input name='ui-thread' type='checkbox' /> Run on UI Thread
          <textarea id='code' name='code' rows='#{rows}' cols='100'>#{WEBrick::HTMLUtils.escape(code)}</textarea>
        </form><p/>
      ]
    end
  
    def edit_form(name="untitled.rb", script="")
      %Q[
        <form action='/script/' method='post' style='display:inline'>
          Name: <input name='name' value='#{name == "new"  ? "untitled.rb" : name}' />
          <input type='submit' value='Save' /><br/>
          <textarea id='code' name='code' rows='25' cols='100'>#{WEBrick::HTMLUtils.escape(script)}</textarea>
        </form>
      ]
    end
  
    def do_GET(req, resp)
      case req.path
      when "/"
        resp.body = eval_page
      when "/stop"
        resp.body = "Goodbye"
        Thread.new{sleep 2; $irb_activity.runOnUiThread{$irb_activity.stop}}
      when "/history"
        resp.body = eval_page("Ruboto IRB: History", @@history.map{|i| eval_form(i, 10)})
      when "/script/new"
        resp.body = eval_page("Ruboto IRB: New Script", edit_form("new"))
      when "/scripts"
        resp.body = scripts
      else
        if req.path[0..12] == "/script/edit/" && File.exists?(req.path[13..-1])
          resp.body = eval_page("Ruboto IRB: Edit #{req.path[13..-1]}", edit_form(req.path[13..-1], IO.read(req.path[13..-1])))
        elsif req.path[0..12] == "/script/eval/" && File.exists?(req.path[13..-1])
          resp.body = eval_page("Ruboto IRB: Eval #{req.path[13..-1]}", eval_form(IO.read(req.path[13..-1])))
        elsif req.path[0..7] == "/delete/" && File.exists?(req.path[8..-1])
          File.delete(req.path[8..-1])
          resp.set_redirect(WEBrick::HTTPStatus::TemporaryRedirect, '/scripts')
          raise WEBrick::HTTPStatus::TemporaryRedirect
        elsif File.exists?(req.path[1..-1])
          resp.content_type = case req.path.split(".")[-1]
          when "jpg" then "image/jpg"
          when "ico" then "image/ico"
          when "html" then "text/html"
          when "css" then "text/css"
          when "js" then "application/javascript"
          else
            puts req.path
            raise WEBrick::HTTPStatus::NotFound
          end
          resp.body = IO.read(req.path[1..-1])
          raise WEBrick::HTTPStatus::OK
        else
          raise WEBrick::HTTPStatus::NotFound
        end
      end
  
      resp.content_type = "text/html"
      raise WEBrick::HTTPStatus::OK
    end
  
    def scripts
      body = []

      Dir.glob("*").sort.each do |i|
        unless File.directory?(i)
          if i[-3..-1] == ".rb"
            body << "<td>#{i}</td><td><a href='/script/edit/#{i}'>Edit</a> | <a href='/script/eval/#{i}'>Eval</a> | <a href='/delete/#{i}' onclick='return confirm(\"Delete #{i}?\")'>Delete</a></td>"
          else 
            body << "<td><a href='/#{i}'>#{i}</a></td><td><a href='/delete/#{i}' onclick='return confirm(\"Delete #{i}?\")'>Delete</a></td>"
          end
        end
      end
      body = "<table><tr>" + body.join("</tr><tr>") + "</tr></table><br/><br/><a href='/script/new'>Create a new script</a>"
      eval_page("Ruboto IRB: Scripts", body)
    end

    def do_POST(req, resp)
      resp.content_type = "text/html"

      if req.path == "/"
        resp.body = evaluate(req.query['code'], req.query['ui-thread'] == "on")
        raise WEBrick::HTTPStatus::OK
      elsif req.path == "/script/"
        File.open(req.query['name'], "w") {|file| file.write req.query['code']}
        resp.set_redirect(WEBrick::HTTPStatus::TemporaryRedirect, '/scripts')
        raise WEBrick::HTTPStatus::TemporaryRedirect
      elsif req.path == "/scripts"
        resp.body = scripts
        raise WEBrick::HTTPStatus::OK
      end
  
      raise WEBrick::HTTPStatus::NotFound
    end
  
    def evaluate(code, ui_thread)  
      p = Proc.new do
        java.lang.Thread.currentThread.setUncaughtExceptionHandler(@@exception_handler)
  
        if @@string_io.length > 0
          s = @@string_io.string.clone
          @@string_io.truncate(0)
          @@string_io.rewind
          puts "vvvvvvvvvv Leftover Output vvvvvvvvvv\n#{s}\n^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n\n\n"
        end
  
        puts code.length < 100 ? ">> #{code}\n" : ">> {code not echoed due to size}\n"
  
        begin
          puts "=> #{eval(code, $main_binding, "eval", 1).inspect}\n"
        rescue => e
          puts e.message
          puts e.backtrace.join("\n")
        end
       
        $irb_activity.synchronized{$irb_activity.notify}
      end
  
      $irb_activity.synchronized do
        if ui_thread
          $irb_activity.runOnUiThread(p)
        else
          Thread.with_large_stack &p
        end
        $irb_activity.wait
      end
  
      @@history -= [code] 
      @@history.push code
  
      rv = eval_page("Ruboto IRB: Eval Results", "<textarea rows='25' cols='100' disabled='disabled'>#{@@string_io.string}</textarea>")
      @@string_io.truncate(0)
      @@string_io.rewind
      rv
    end
  end

#####################################################

  unless $server
    $server = Server.new(SERVER_PORT)
  
    $server.before_start do
      $irb_activity.runOnUiThread(proc{$irb_activity.set_status("Server starting...", false)})
    end
  
    $server.after_start do
      status = "Server started!\n\nConnection instructions:\n\nTo connect through adb:\n'adb forward tcp:#{SERVER_PORT} tcp:#{SERVER_PORT}'\nthen browse #to\n'http://localhost:8005'"
      status += "\n\nOr use 'http://#{Server.ip_address}:#{SERVER_PORT}'" if Server.wifi_connected?
      $irb_activity.runOnUiThread(proc{$irb_activity.set_status(status, true)})
    end
  
    $server.before_stop do
      $irb_activity.runOnUiThread(proc{$irb_activity.set_status("Server shutting down...", false)})
    end

    $server.after_stop do
      $irb_activity.runOnUiThread(proc{$irb_activity.set_status("Press to start the server", true)})
      $irb_activity.getSystemService(android.content.Context::NOTIFICATION_SERVICE).cancel_all
    end
  end
  
  $irb_activity.runOnUiThread(proc{$irb_activity.set_status($server.running? ? "Server already running" : "Press to start a server", true)})
  $irb_activity.runOnUiThread(proc{$irb_activity.start})
end

