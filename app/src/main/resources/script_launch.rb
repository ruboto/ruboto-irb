#######################################################
#
# script_launch.rb (by Scott Moyer)
# 
# Reads the script name from the intent and loads it.
#
#######################################################

class ScriptLaunch
  def onCreate(b)
    super

    files_dir = getFilesDir.getAbsolutePath + "/scripts"
    $LOAD_PATH << "/sdcard/jruby" unless $LOAD_PATH.include?("/sdcard/jruby")
    $LOAD_PATH << files_dir unless $LOAD_PATH.include?(files_dir)

    old_irb, $irb = $irb, self
    load getIntent.getExtras.getString(org.ruboto.irb.ShortcutBuilder::SCRIPT_NAME)
    $irb = old_irb
    
    finish
  end
end

