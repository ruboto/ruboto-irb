package org.ruboto.irb;

import android.os.Bundle;

public class ScriptLaunch extends org.ruboto.EntryPointActivity {
	public void onCreate(Bundle bundle) {
		setRubyClassName(getClass().getSimpleName());
	    super.onCreate(bundle);
	}
}
