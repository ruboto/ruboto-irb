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
  def surfaceCreated(holder)
    $camera = Camera.open # Add (1) for front camera
    $camera.preview_display = holder
    $camera.start_preview
  end

  def surfaceChanged(holder, format, width, height)
  end

  def surfaceDestroyed(holder)
    $camera.stop_preview
    $camera.release
    $camera = nil
  end
end
  
$activity.start_ruboto_activity "$camera_demo", RubotoActivity, R.style::Theme_NoTitleBar_Fullscreen do
  def on_create(bundle)
    @surface_view = android.view.SurfaceView.new(self)
    @surface_view.set_on_click_listener{|v| take_picture}
    @surface_view.holder.add_callback RubotoSurfaceHolderCallback.new
    # Deprecated, but still required for older API version
    @surface_view.holder.set_type android.view.SurfaceHolder::SURFACE_TYPE_PUSH_BUFFERS
    self.content_view = @surface_view
  end

  def take_picture
    picture_file = "#{Dir.pwd}/picture#{$camera.picture_id}.jpg"
    $camera.take_picture(proc{toast "Picture taken"}, nil) do |data, camera|
      fos = java.io.FileOutputStream.new(picture_file)
      fos.write(data)
      fos.close
    end
    picture_file
  end
end

