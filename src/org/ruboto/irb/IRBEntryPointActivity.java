package org.ruboto.irb;

import android.content.Intent;

import org.ruboto.JRubyAdapter;
import org.ruboto.SplashActivity;

public class IRBEntryPointActivity extends android.app.Activity {
  public void onResume() {
    if (JRubyAdapter.isInitialized()) {
      fireRubotoActivity();
    } else {
      showSplash();
    }
    super.onResume();
  }

  protected void fireRubotoActivity() {
  }

  private void showSplash() {
    Intent splashIntent = new Intent(this, SplashActivity.class);
    startActivity(splashIntent);
  }
}
