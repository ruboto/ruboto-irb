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
require 'ruboto/generate'

java_import "android.view.SurfaceView"
java_import "android.hardware.Camera"

ruboto_generate("android.view.SurfaceHolder$Callback" => "org.ruboto.callbacks.RubotoSurfaceHolderCallback")

class Camera
  def picture_id
    @picture_id ||= 0
    @picture_id += 1
  end
end

$activity.start_ruboto_activity "$camera_demo", RubotoActivity, R.style::Theme_NoTitleBar_Fullscreen do
  def on_create(bundle)
    @surface_view = SurfaceView.new(self)
    @surface_view.set_on_click_listener(proc{|v| take_picture})
    @surface_view.holder.add_callback @holder_callback
    # Deprecated, but still required for older API version
    @surface_view.holder.set_type android.view.SurfaceHolder::SURFACE_TYPE_PUSH_BUFFERS
    self.content_view = @surface_view
  end

  @holder_callback = RubotoSurfaceHolderCallback.new_with_callbacks do
    def on_surface_created(holder)
      $camera = Camera.open # Add (1) for front camera
      $camera.preview_display = holder
      $camera.start_preview
    end

    def on_surface_destroyed(holder)
      $camera.stop_preview
      $camera.release
      $camera = nil
    end
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

