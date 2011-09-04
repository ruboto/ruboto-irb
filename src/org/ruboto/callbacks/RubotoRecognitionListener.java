package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoRecognitionListener implements android.speech.RecognitionListener {

  public static final int CB_BEGINNING_OF_SPEECH = 0;
  public static final int CB_BUFFER_RECEIVED = 1;
  public static final int CB_END_OF_SPEECH = 2;
  public static final int CB_ERROR = 3;
  public static final int CB_EVENT = 4;
  public static final int CB_PARTIAL_RESULTS = 5;
  public static final int CB_READY_FOR_SPEECH = 6;
  public static final int CB_RESULTS = 7;
  public static final int CB_RMS_CHANGED = 8;

    private Object[] callbackProcs = new Object[9];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onBeginningOfSpeech() {
    if (callbackProcs[CB_BEGINNING_OF_SPEECH] != null) {
      Script.callMethod(callbackProcs[CB_BEGINNING_OF_SPEECH], "call" );
    }
  }

  public void onBufferReceived(byte[] buffer) {
    if (callbackProcs[CB_BUFFER_RECEIVED] != null) {
      Script.callMethod(callbackProcs[CB_BUFFER_RECEIVED], "call" , buffer);
    }
  }

  public void onEndOfSpeech() {
    if (callbackProcs[CB_END_OF_SPEECH] != null) {
      Script.callMethod(callbackProcs[CB_END_OF_SPEECH], "call" );
    }
  }

  public void onError(int error) {
    if (callbackProcs[CB_ERROR] != null) {
      Script.callMethod(callbackProcs[CB_ERROR], "call" , error);
    }
  }

  public void onEvent(int eventType, android.os.Bundle params) {
    if (callbackProcs[CB_EVENT] != null) {
      Script.callMethod(callbackProcs[CB_EVENT], "call" , new Object[]{eventType, params});
    }
  }

  public void onPartialResults(android.os.Bundle partialResults) {
    if (callbackProcs[CB_PARTIAL_RESULTS] != null) {
      Script.callMethod(callbackProcs[CB_PARTIAL_RESULTS], "call" , partialResults);
    }
  }

  public void onReadyForSpeech(android.os.Bundle params) {
    if (callbackProcs[CB_READY_FOR_SPEECH] != null) {
      Script.callMethod(callbackProcs[CB_READY_FOR_SPEECH], "call" , params);
    }
  }

  public void onResults(android.os.Bundle results) {
    if (callbackProcs[CB_RESULTS] != null) {
      Script.callMethod(callbackProcs[CB_RESULTS], "call" , results);
    }
  }

  public void onRmsChanged(float rmsdB) {
    if (callbackProcs[CB_RMS_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_RMS_CHANGED], "call" , rmsdB);
    }
  }

}
