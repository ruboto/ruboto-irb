package org.ruboto.irb;

public class ScriptLaunch extends org.ruboto.RubotoActivity {
  public static final String SDCARD_SCRIPTS_DIR = "/sdcard/jruby";

	public void onCreate(android.os.Bundle arg0) {
    org.ruboto.Script.configDir(SDCARD_SCRIPTS_DIR, getFilesDir().getAbsolutePath() + "/scripts");
    setScriptName(getIntent().getExtras().getString("org.ruboto.extra.SCRIPT_NAME"));
    super.onCreate(arg0);
  }
}
