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
      finish();
    }
    super.onResume();
  }

  protected void fireRubotoActivity() {
  }

  private void showSplash() {
      Intent splashIntent = new Intent(this, SplashActivity.class);
      splashIntent.putExtra(Intent.EXTRA_INTENT, futureIntent());
      startActivity(splashIntent);
  }

  protected Intent futureIntent() {
      if (!getIntent().getAction().equals(Intent.ACTION_VIEW)) {
          return new Intent(getIntent()).setAction(Intent.ACTION_VIEW);
      } else {
          return getIntent();
      }
  }
}
