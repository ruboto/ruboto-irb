package net.android.facebook;

import org.ruboto.irb.R;
import android.os.Bundle;
import android.os.Handler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.SessionStore;

public class TestPost extends Activity{
	private Facebook mFacebook;
	private CheckBox mFacebookCb;
	private ProgressDialog mProgress;
	
	private Handler mRunOnUi = new Handler();
	
	private static final String APP_ID = "340864435993007";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.post);
		
		Intent get = getIntent();
		final String x = get.getStringExtra("code");
		final String Sname = get.getStringExtra("Sname");
		
		
		final EditText reviewEdit = (EditText) findViewById(R.id.revieew);
		mFacebookCb				  = (CheckBox) findViewById(R.id.cb_facebook);
		
		mProgress	= new ProgressDialog(this);
		
		mFacebook 	= new Facebook(APP_ID);
		
		SessionStore.restore(mFacebook, this);

		if (mFacebook.isSessionValid()) {
			mFacebookCb.setChecked(true);
				
			String name = SessionStore.getName(this);
			name		= (name.equals("")) ? "Unknown" : name;
				
			mFacebookCb.setText("  Facebook  (" + name + ")");
		}
		
		((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String review = reviewEdit.getText().toString();
				
				review = review + "\nScript name:" +Sname +"\n=====================\n" + x;
				if (mFacebookCb.isChecked()) postToFacebook(review);
			}
		});
	}
	
	private void postToFacebook(String review) {	
		mProgress.setMessage("Posting ...");
		mProgress.show();
		
		AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(mFacebook);
		
		Bundle params = new Bundle();
    		
		params.putString("message", review );
		params.putString("name", "Ruboto");
		params.putString("caption", "ruboto.org");
		params.putString("link", "http://ruboto.org/");
		params.putString("description", "Ruboto, sharing your jruby scripts is fun");
		params.putString("picture", "http://ruboto.org/ruboto.png");
		
		mAsyncFbRunner.request("me/feed", params, "POST", new WallPostListener());
	}

	private final class WallPostListener extends BaseRequestListener {
        public void onComplete(final String response) {
        	mRunOnUi.post(new Runnable() {
        		@Override
        		public void run() {
        			mProgress.cancel();
        			
        			Toast.makeText(TestPost.this, "Posted to Facebook", Toast.LENGTH_SHORT).show();
        		}
        	});
        }
    }
}