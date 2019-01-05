#######################################################
#
# demo-camera.rb (by Scott Moyer)
# 
# Fires up a camera. Take a picture by touching 
# the screen. Pictures are stored in the scripts 
# directory.
#
#######################################################

require 'ruboto/activity'
require 'ruboto/util/toast'

java_import "android.hardware.Camera"

class Camera
  def picture_id
    @picture_id ||= 0
    @picture_id += 1
  end
end

class RubotoSurfaceHolderCallback
  attr_reader :camera

  def surfaceCreated(holder)
    @camera = Camera.open # Add (1) for front camera
    @camera.preview_display = holder
    @camera.start_preview
  end

  def surfaceChanged(holder, format, width, height)
  end

  def surfaceDestroyed(holder)
    @camera.stop_preview
    @camera.release
    @camera = nil
  end
end
  
class CameraDemo
  def on_create(bundle)
    super
    @surface_view = android.view.SurfaceView.new(@ruboto_java_instance)
    @surface_view.set_on_click_listener{|v| take_picture}
    @holder_callback = RubotoSurfaceHolderCallback.new
    @surface_view.holder.add_callback @holder_callback
    # Deprecated, but still required for older API version
    @surface_view.holder.set_type android.view.SurfaceHolder::SURFACE_TYPE_PUSH_BUFFERS
    self.content_view = @surface_view
  end

  def take_picture
    camera = @holder_callback.camera
    return unless camera

    picture_file = "#{Dir.pwd}/picture#{camera.picture_id}.jpg"
    camera.take_picture(proc{toast "Picture taken"}, nil) do |data, camera|
      fos = java.io.FileOutputStream.new(picture_file)
      fos.write(data)
      fos.close
    end
    picture_file
  end
end

$irb.start_ruboto_activity "CameraDemo", :theme => R.style::Theme_NoTitleBar_Fullscreen

