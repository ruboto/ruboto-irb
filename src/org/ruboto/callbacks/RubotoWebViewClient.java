package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoWebViewClient extends android.webkit.WebViewClient {

  public static final int CB_DO_UPDATE_VISITED_HISTORY = 0;
  public static final int CB_FORM_RESUBMISSION = 1;
  public static final int CB_LOAD_RESOURCE = 2;
  public static final int CB_PAGE_FINISHED = 3;
  public static final int CB_PAGE_STARTED = 4;
  public static final int CB_RECEIVED_ERROR = 5;
  public static final int CB_RECEIVED_HTTP_AUTH_REQUEST = 6;
  public static final int CB_SCALE_CHANGED = 7;
  public static final int CB_UNHANDLED_KEY_EVENT = 8;
  public static final int CB_SHOULD_OVERRIDE_KEY_EVENT = 9;
  public static final int CB_SHOULD_OVERRIDE_URL_LOADING = 10;

    private Object[] callbackProcs = new Object[11];

  public  RubotoWebViewClient() {
    super();
  }

  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void doUpdateVisitedHistory(android.webkit.WebView view, java.lang.String url, boolean isReload) {
    if (callbackProcs[CB_DO_UPDATE_VISITED_HISTORY] != null) {
      super.doUpdateVisitedHistory(view, url, isReload);
      Script.callMethod(callbackProcs[CB_DO_UPDATE_VISITED_HISTORY], "call" , new Object[]{view, url, isReload});
    } else {
      super.doUpdateVisitedHistory(view, url, isReload);
    }
  }

  public void onFormResubmission(android.webkit.WebView view, android.os.Message dontResend, android.os.Message resend) {
    if (callbackProcs[CB_FORM_RESUBMISSION] != null) {
      super.onFormResubmission(view, dontResend, resend);
      Script.callMethod(callbackProcs[CB_FORM_RESUBMISSION], "call" , new Object[]{view, dontResend, resend});
    } else {
      super.onFormResubmission(view, dontResend, resend);
    }
  }

  public void onLoadResource(android.webkit.WebView view, java.lang.String url) {
    if (callbackProcs[CB_LOAD_RESOURCE] != null) {
      super.onLoadResource(view, url);
      Script.callMethod(callbackProcs[CB_LOAD_RESOURCE], "call" , new Object[]{view, url});
    } else {
      super.onLoadResource(view, url);
    }
  }

  public void onPageFinished(android.webkit.WebView view, java.lang.String url) {
    if (callbackProcs[CB_PAGE_FINISHED] != null) {
      super.onPageFinished(view, url);
      Script.callMethod(callbackProcs[CB_PAGE_FINISHED], "call" , new Object[]{view, url});
    } else {
      super.onPageFinished(view, url);
    }
  }

  public void onPageStarted(android.webkit.WebView view, java.lang.String url, android.graphics.Bitmap favicon) {
    if (callbackProcs[CB_PAGE_STARTED] != null) {
      super.onPageStarted(view, url, favicon);
      Script.callMethod(callbackProcs[CB_PAGE_STARTED], "call" , new Object[]{view, url, favicon});
    } else {
      super.onPageStarted(view, url, favicon);
    }
  }

  public void onReceivedError(android.webkit.WebView view, int errorCode, java.lang.String description, java.lang.String failingUrl) {
    if (callbackProcs[CB_RECEIVED_ERROR] != null) {
      super.onReceivedError(view, errorCode, description, failingUrl);
      Script.callMethod(callbackProcs[CB_RECEIVED_ERROR], "call" , new Object[]{view, errorCode, description, failingUrl});
    } else {
      super.onReceivedError(view, errorCode, description, failingUrl);
    }
  }

  public void onReceivedHttpAuthRequest(android.webkit.WebView view, android.webkit.HttpAuthHandler handler, java.lang.String host, java.lang.String realm) {
    if (callbackProcs[CB_RECEIVED_HTTP_AUTH_REQUEST] != null) {
      super.onReceivedHttpAuthRequest(view, handler, host, realm);
      Script.callMethod(callbackProcs[CB_RECEIVED_HTTP_AUTH_REQUEST], "call" , new Object[]{view, handler, host, realm});
    } else {
      super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }
  }

  public void onScaleChanged(android.webkit.WebView view, float oldScale, float newScale) {
    if (callbackProcs[CB_SCALE_CHANGED] != null) {
      super.onScaleChanged(view, oldScale, newScale);
      Script.callMethod(callbackProcs[CB_SCALE_CHANGED], "call" , new Object[]{view, oldScale, newScale});
    } else {
      super.onScaleChanged(view, oldScale, newScale);
    }
  }

  public void onUnhandledKeyEvent(android.webkit.WebView view, android.view.KeyEvent event) {
    if (callbackProcs[CB_UNHANDLED_KEY_EVENT] != null) {
      super.onUnhandledKeyEvent(view, event);
      Script.callMethod(callbackProcs[CB_UNHANDLED_KEY_EVENT], "call" , new Object[]{view, event});
    } else {
      super.onUnhandledKeyEvent(view, event);
    }
  }

  public boolean shouldOverrideKeyEvent(android.webkit.WebView view, android.view.KeyEvent event) {
    if (callbackProcs[CB_SHOULD_OVERRIDE_KEY_EVENT] != null) {
      super.shouldOverrideKeyEvent(view, event);
      return (Boolean) Script.callMethod(callbackProcs[CB_SHOULD_OVERRIDE_KEY_EVENT], "call" , new Object[]{view, event}, Boolean.class);
    } else {
      return super.shouldOverrideKeyEvent(view, event);
    }
  }

  public boolean shouldOverrideUrlLoading(android.webkit.WebView view, java.lang.String url) {
    if (callbackProcs[CB_SHOULD_OVERRIDE_URL_LOADING] != null) {
      super.shouldOverrideUrlLoading(view, url);
      return (Boolean) Script.callMethod(callbackProcs[CB_SHOULD_OVERRIDE_URL_LOADING], "call" , new Object[]{view, url}, Boolean.class);
    } else {
      return super.shouldOverrideUrlLoading(view, url);
    }
  }
}
