#######################################################
#
# demo-irb-server.rb (by Scott Moyer)
# 
# Creates a webrick server to allow you to edit the 
# scripts on the device from your browser. You can also
# send arbitrary code to execute on the device. This is 
# an example of creating a Service.
#
#######################################################

require "ruboto/service"
require "ruboto/util/stack"

with_large_stack do
  require 'webrick'
  require 'stringio'
end

include WEBrick

java_import "android.content.Context"
java_import "android.content.Intent"
java_import "android.app.Notification"
java_import "android.app.PendingIntent"

SERVER_PORT = 8005

$main_binding = self.instance_eval{binding}

#####################################################

def notify(title, text, ticker, icon, note_id=1)
  notification = Notification.new(icon, ticker, java.lang.System.currentTimeMillis)
  intent = Intent.new
  intent.setAction("org.ruboto.intent.action.LAUNCH_SCRIPT")
  intent.addCategory("android.intent.category.DEFAULT")
  intent.putExtra("org.ruboto.extra.SCRIPT_NAME", "demo-irb-server.rb")
  pending = PendingIntent.getActivity($activity, 0, intent, 0)
  notification.setLatestEventInfo($activity.getApplicationContext, title, text, pending)
  $context.getSystemService(Context::NOTIFICATION_SERVICE).notify(note_id, notification)
end

def stop_server
  $server.shutdown if $server
  $context.getSystemService(Context::NOTIFICATION_SERVICE).cancel_all
  $server = nil
  $context.stop_service Intent.new($context, RubotoService.java_class)
  $service = nil
end

def ip_address
  ip = $context.get_system_service(Context::WIFI_SERVICE).connection_info.ip_address
  return "localhost" if ip == 0
  [0, 8, 16, 24].map{|n| ((ip >> n) & 0xff).to_s}.join(".")
end

#####################################################
#
# Eval code and return result (mount /)
#

class EvalServlet < HTTPServlet::AbstractServlet
  def eval_page()
    %Q[
      <html>
        <body>
          <h2>Eval</h2>
          <form action='/' method='post'>
            <textarea id='code' name='code' rows='25' cols='100'></textarea>
            <br/>
            <input type='submit' value='Eval' />
          </form>
          <h2>Other Actions</h2>
          <a href='/scripts'>Files...</a><br/>
          <a href='/stop'>Stop Server</a>
        </body>
      </html>
    ]
  end

  def do_GET(req, resp)
    if req.path == "/"
      resp.content_type = "text/html"
      resp.body = eval_page
    elsif File.exists?(req.path[1..-1])
      resp.content_type = case req.path.split(".")[-1]
      when "jpg" : "image/jpg"
      when "ico" : "image/ico"
      when "html" : "text/html"
      when "css" : "text/css"
      when "js" : "application/javascript"
      else
        puts req.path
        "text/plain"
      end
      resp.body = IO.read(req.path[1..-1])
    end
  end

  def do_POST(req, resp)
    old_out, $stdout = $stdout, StringIO.new
    begin
      result = $main_binding.eval(req.query['code'])
      $stdout, new_out = old_out, $stdout 
      result = ">> #{req.query['code']}\n#{new_out.string}=> #{result.inspect}\n"
    rescue => e
      $stdout = old_out
      result = ">> #{req.query['code']}\n#{e.to_s}\n#{([e.message] + e.backtrace).join("\n")}\n"
    end
    
    puts result

    resp.content_type = "text/plain"
    resp.body = result
  end
end

#####################################################
#
# List scripts (mount /scripts)
#

class ScriptListServlet < HTTPServlet::AbstractServlet
  def list_page
    %Q[
      <html>
        <body>
          <h2>Scripts</h2>
          #{yield}
          <h2>Other Actions</h2>
          <a href='/'>Eval...</a><br/>
          <a href='/script/new'>Create Script...</a><br/>
          <a href='/stop'>Stop Server</a><br/>
        </body>
      </html>
    ]
  end

  def do_GET(req, resp)
    resp.content_type = "text/html"
    resp.body = list_page do 
      Dir.glob("*").sort.map do |i|
        unless File.directory?(i)
          $server.mount("/script/#{i}", ScriptServlet, i)
          
          if i[-3..-1] == ".rb"
            $server.mount_proc("/run/#{i}") do |req, resp|
              $activity.runOnUiThread proc{load i}
              resp.set_redirect(HTTPStatus::TemporaryRedirect, '/scripts')
            end
            "<a href='/script/#{i}'>#{i}</a> (<a href='/run/#{i}'>run</a>)<br/>"
          else 
            "<a href='/#{i}'>#{i}</a><br/>"
          end
        end
      end.join
    end
  end

  def do_POST(req, resp)
    do_GET(req, resp)
  end
end

#####################################################
#
# Edit scripts (mount /script/<file_name>)
#

class ScriptServlet < HTTPServlet::AbstractServlet
  def edit_form(name="untitled.rb", script="")
    %Q[
      <html>
        <body>
          <form action='/script/#{name}' method='post' style='display:inline'>
            Name: <input name='name' value='#{name == "new"  ? "untitled.rb" : name}' />
            <input type='submit' value='Save' /><br/>
            <textarea id='code' name='code' rows='25' cols='100'>#{HTMLUtils.escape(script)}</textarea>
          </form>
          <h2>Other Actions</h2>
          <a href='/run/#{name}'>Run</a><br/>
          <a href='/scripts'>Cancel</a><br/>
          <a href='/stop'>Stop Server</a><br/>
        </body>
      </html>
    ]
  end

  def do_GET(req, resp)
    name = @options[0] || "new"
    resp.content_type = "text/html"
    script = File.exists?(name) ? IO.read(name) : ""
    resp.body = edit_form(name, script)
  end

  def do_POST(req, resp)
    File.open(req.query['name'], "w") {|file| file.write req.query['code']}
    resp.set_redirect(HTTPStatus::TemporaryRedirect, '/scripts')
  end
end

#####################################################

if $server
  stop_server
elsif ip_address
  $server = HTTPServer.new(:Port => SERVER_PORT, :DocumentRoot => Dir.pwd, :AccessLog => [])

  $server.mount("/",           EvalServlet,       nil)
  $server.mount("/scripts",    ScriptListServlet, nil)
  $server.mount("/script/new", ScriptServlet,     nil)
  
  $server.mount_proc('/stop') do |req, resp| 
    resp.body = "Goodbye"
    stop_server
  end

  notify("IRB server running on #{ip_address}:#{SERVER_PORT}", 
    "Rerun the script to stop the server.", 
    "IRB server running on #{ip_address}:#{SERVER_PORT}", 
    Java::android.R::drawable::stat_sys_upload)

  $context.start_ruboto_service("$irb_server") do
    def on_start_command(intent, flags, startId)
      Thread.with_large_stack {$server.start}
      self.class::START_NOT_STICKY
    end
  end
end

