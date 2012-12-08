#######################################################
#
# glsurfaceview.rb (by Scott Moyer)
#
# This demo ports the GLSurfaceView demo from the Android
# API Demos to Ruboto.
#
#######################################################

require 'ruboto/activity'
require 'ruboto/widget'

java_import "javax.microedition.khronos.egl.EGL10"
java_import "javax.microedition.khronos.egl.EGLConfig"
java_import "javax.microedition.khronos.opengles.GL10"

java_import "java.nio.ByteBuffer"
java_import "java.nio.ByteOrder"
java_import "java.nio.IntBuffer"

ruboto_import_widget :GLSurfaceView, "android.opengl"

#######################################################
#
# Cube Class
#
# Needs to get set up before the Activity tries to use it
#

class Cube
  def initialize
    one = 0x10000
    vertices = [
      -one, -one, -one,
       one, -one, -one,
       one, one, -one,
      -one, one, -one,
      -one, -one, one,
       one, -one, one,
       one, one, one,
      -one, one, one,
    ]

    colors = [
        0, 0, 0, one,
      one, 0, 0, one,
      one, one, 0, one,
        0, one, 0, one,
        0, 0, one, one,
      one, 0, one, one,
      one, one, one, one,
        0, one, one, one,
     ]

    indices = [
      0, 4, 5, 0, 5, 1,
      1, 5, 6, 1, 6, 2,
      2, 6, 7, 2, 7, 3,
      3, 7, 4, 3, 4, 0,
      4, 7, 6, 4, 6, 5,
      3, 0, 1, 3, 1, 2
    ]

    vbb = ByteBuffer.allocateDirect(vertices.length*4)
    vbb.order(ByteOrder.nativeOrder)
    @vertex_buffer = vbb.asIntBuffer
    @vertex_buffer.put(vertices.to_java(:int))
    @vertex_buffer.position(0)

    cbb = ByteBuffer.allocateDirect(colors.length*4)
    cbb.order(ByteOrder.nativeOrder)
    @color_buffer = cbb.asIntBuffer
    @color_buffer.put(colors.to_java(:int))
    @color_buffer.position(0)

    @index_buffer = ByteBuffer.allocateDirect(indices.length)
    @index_buffer.put(indices.to_java(:byte))
    @index_buffer.position(0)
  end

  def draw(gl)
    gl.glFrontFace(GL10::GL_CW)
    gl.glVertexPointer(3, GL10::GL_FIXED, 0, @vertex_buffer)
    gl.glColorPointer(4, GL10::GL_FIXED, 0, @color_buffer)
    gl.glDrawElements(GL10::GL_TRIANGLES, 36, GL10::GL_UNSIGNED_BYTE, @index_buffer)
  end
end

#######################################################
#
# RubotoGLSurfaceViewRenderer
#
# The interface android.opengl.GLSurfaceView$Renderer
#

class RubotoGLSurfaceViewRenderer
  def initialize
    @translucent_background = false
    @cube = Cube.new
    @angle = 0.0
    @offset = 1.2
  end
  
  def onDrawFrame(gl)
    gl.glClear(GL10::GL_COLOR_BUFFER_BIT | GL10::GL_DEPTH_BUFFER_BIT)

    gl.glMatrixMode(GL10::GL_MODELVIEW)
    gl.glLoadIdentity
    gl.glTranslatef(0, 0, -3.0)
    gl.glRotatef(@angle, 0, 1, 0)
    gl.glRotatef(@angle*0.25, 1, 0, 0)

    gl.glEnableClientState(GL10::GL_VERTEX_ARRAY)
    gl.glEnableClientState(GL10::GL_COLOR_ARRAY)

    @cube.draw(gl)

    gl.glRotatef(@angle*2.0, 0, 1, 1)
    gl.glTranslatef(0.5, 0.5, 0.5)

    @cube.draw(gl)
    @angle += @offset
  end

  def onSurfaceChanged(gl, width, height)
    gl.glViewport(0, 0, width, height)
    ratio = width.to_f / height.to_f
    gl.glMatrixMode(GL10::GL_PROJECTION)
    gl.glLoadIdentity
    gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10)
  end

  def onSurfaceCreated(gl, config)
    gl.glDisable(GL10::GL_DITHER)

    gl.glHint(GL10::GL_PERSPECTIVE_CORRECTION_HINT, GL10::GL_FASTEST)

    if (@translucent_background)
      gl.glClearColor(0,0,0,0)
    else
      gl.glClearColor(1,1,1,1)
    end
    gl.glEnable(GL10::GL_CULL_FACE)
    gl.glShadeModel(GL10::GL_SMOOTH)
    gl.glEnable(GL10::GL_DEPTH_TEST)
  end
  
  def change_angle
    @offset = -@offset
  end
end
   
#######################################################
#
# Activity
#
# Start a new activity or connect to $activity
#   

class OpenGLDemo
  def on_create(bundle)
    super

    begin
      action_bar.hide
    rescue
      set_title("GLSurfaceView Demo")
    end

    @renderer = RubotoGLSurfaceViewRenderer.new
    @surface_view = g_l_surface_view :renderer => @renderer, 
                                     :on_click_listener => (proc{@renderer.change_angle})

    self.content_view = @surface_view
  end 
    
  def on_resume
    super
    @surface_view.on_resume
  end

  def on_pause
    super
    @surface_view.on_pause
  end        
end

#######################################################
#
# Needed because this is called from inside of Ruboto IRB
#

$irb.start_ruboto_activity :class_name => "OpenGLDemo"

# The other option is the remove the line above and to replace
# the "class OpenGLDemo" line with this:
#   $irb.start_ruboto_activity "$glsurface" do

