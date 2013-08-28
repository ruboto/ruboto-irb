package org.ruboto.irb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.ruboto.JRubyAdapter;
import org.ruboto.SplashActivity;

public class IRBEntryPointActivity extends android.app.Activity {
  public void onCreate(Bundle bundle) {
    if (!JRubyAdapter.isInitialized()) {
      showSplash();
      finish();
    }
    super.onCreate(bundle);
  }

  public void onResume() {
    if (JRubyAdapter.isInitialized()) {
      fireRubotoActivity();
    }
    super.onResume();
  }

  protected void fireRubotoActivity() {
  }

  private void showSplash() {
    Intent splashIntent = new Intent(IRBEntryPointActivity.this, SplashActivity.class);
    startActivity(splashIntent);
  }
}
