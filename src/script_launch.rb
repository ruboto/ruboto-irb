#######################################################
#
# script_launch.rb (by Scott Moyer)
# 
# Reads the script name from the intent and loads it.
#
#######################################################

file_dir = $activity.getFilesDir.getAbsolutePath + "/scripts"

$LOAD_PATH << file_dir unless $LOAD_PATH.include?(file_dir)
$LOAD_PATH << "/sdcard/jruby" unless $LOAD_PATH.include?("/sdcard/jruby")

load $activity.getIntent.getExtras.getString("org.ruboto.extra.SCRIPT_NAME")
