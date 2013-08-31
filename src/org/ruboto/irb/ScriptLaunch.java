package org.ruboto.irb;

import android.content.Intent;

public class ScriptLaunch extends org.ruboto.EntryPointActivity {
  protected Intent futureIntent() {
    return ShortcutBuilder.intentForScript(getIntent().getExtras().getString(ShortcutBuilder.SCRIPT_NAME));
  }
}

