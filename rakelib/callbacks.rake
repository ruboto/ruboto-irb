# These are single method callbacks that have been removed in case we need them again
task :removed_callbacks do
  [
    %w(android.opengl.GLSurfaceView.EGLConfigChooser),
    %w(android.opengl.GLSurfaceView.GLWrapper),

    %w(android.media.AudioManager.OnAudioFocusChangeListener), #
    %w(android.media.MediaPlayer.OnBufferingUpdateListener MediaPlayerOnBufferingUpdateListener),
    %w(android.media.MediaPlayer.OnCompletionListener MediaPlayerOnCompletionListener),
    %w(android.media.MediaPlayer.OnErrorListener MediaPlayerOnErrorListener),
    %w(android.media.MediaPlayer.OnInfoListener MediaPlayerOnInfoListener),
    %w(android.media.MediaPlayer.OnPreparedListener MediaPlayerOnPreparedListener),
    %w(android.media.MediaPlayer.OnSeekCompleteListener MediaPlayerOnSeekCompleteListener),
    %w(android.media.MediaPlayer.OnVideoSizeChangedListener MediaPlayerOnVideoSizeChangedListener),
    %w(android.media.MediaRecorder.OnErrorListener MediaRecorderOnErrorListener),
    %w(android.media.MediaRecorder.OnInfoListener MediaRecorderOnInfoListener),
    %w(android.media.MediaScannerConnection.OnScanCompletedListener), #
    %w(android.media.SoundPool.OnLoadCompleteListener SoundPoolOnLoadCompleteListener), #

    %w(android.content.DialogInterface.OnCancelListener DialogOnCancelListener),
    %w(android.content.DialogInterface.OnClickListener DialogOnClickListener),
    %w(android.content.DialogInterface.OnDismissListener DialogOnDismissListener),
    %w(android.content.DialogInterface.OnKeyListener DialogOnKeyListener),
    %w(android.content.DialogInterface.OnMultiChoiceClickListener DialogOnMultiChoiceClickListener),
    %w(android.content.DialogInterface.OnShowListener DialogOnShowListener), #
    %w(android.content.IntentSender.OnFinished IntentSenderOnFinished), #
    %w(android.content.SharedPreferences.OnSharedPreferenceChangeListener),
    %w(android.content.SyncStatusObserver), #

    %w(android.location.GpsStatus.Listener GpsStatusListener),
    %w(android.location.GpsStatus.NmeaListener), #

    %w(android.preference.Preference.OnPreferenceChangeListener),
    %w(android.preference.Preference.OnPreferenceClickListener),

    %w(android.view.View.OnTouchListener),
    %w(android.view.View.OnLongClickListener),
    %w(android.view.View.OnFocusChangeListener),
    %w(android.view.View.OnKeyListener),

    %w(android.speech.tts.TextToSpeech.OnInitListener TextToSpeechOnInitListener), #
    %w(android.speech.tts.TextToSpeech.OnUtteranceCompletedListener TextToSpeechOnUtteranceCompletedListener), #

    %w(android.gesture.GestureOverlayView.OnGesturePerformedListener), #

    %w(android.app.KeyguardManager.OnKeyguardExitResult),
    %w(android.app.PendingIntent.OnFinished PendingIntentOnFinished),
    %w(android.app.SearchManager.OnCancelListener SearchOnCancelListener),
    %w(android.app.SearchManager.OnDismissListener SearchOnDismissListener),
    %w(android.app.DatePickerDialog.OnDateSetListener),
    %w(android.app.TimePickerDialog.OnTimeSetListener),

    %w(android.database.sqlite.SQLiteDatabase.CursorFactory SQLiteCursorFactory),

    %w(java.lang.Runnable),
    %w(android.os.Handler.Callback Handler),

    %w(android.widget.AdapterView.OnItemLongClickListener),
    %w(android.widget.TabHost.TabContentFactory),
    %w(android.widget.TabHost.OnTabChangeListener),
    %w(android.widget.TextView.OnEditorActionListener),
    %w(android.widget.DatePicker.OnDateChangedListener),
    %w(android.widget.TimePicker.OnTimeChangedListener),
  ].each do |c, n|
    # Do something
  end
end

# Generate callbacks
task :callbacks do
  [
    %w(android.opengl.GLSurfaceView.EGLContextFactory), #
    %w(android.opengl.GLSurfaceView.EGLWindowSurfaceFactory), #
    %w(android.opengl.GLSurfaceView.Renderer GLSurfaceViewRenderer),
    %w(android.view.SurfaceHolder.Callback SurfaceHolderCallback),


    %w(android.media.AudioRecord.OnRecordPositionUpdateListener),
    %w(android.media.AudioTrack.OnPlaybackPositionUpdateListener),
    %w(android.media.JetPlayer.OnJetEventListener),
    %w(android.media.MediaScannerConnection.MediaScannerConnectionClient), #

    %w(android.location.LocationListener),

    %w(android.view.GestureDetector.OnDoubleTapListener),
    %w(android.view.GestureDetector.OnGestureListener),
    %w(android.view.ScaleGestureDetector.OnScaleGestureListener), #
    %w(android.view.ViewGroup.OnHierarchyChangeListener),

    %w(android.speech.RecognitionListener), #

    %w(android.gesture.GestureOverlayView.OnGestureListener), #
    %w(android.gesture.GestureOverlayView.OnGesturingListener), #

    %w(android.database.sqlite.SQLiteTransactionListener), #5

    %w(android.widget.AdapterView.OnItemSelectedListener),

    %w(android.hardware.SensorEventListener),
  ].each do |c, n|
    puts `ruboto gen interface #{c} --name Ruboto#{n ? n : c.split(".")[-1]} --force include --package org.ruboto.callbacks`
  end

  # Subclasses with methods added after minSDK
  [
    %w(android.telephony.PhoneStateListener),
    %w(android.database.sqlite.SQLiteOpenHelper),
    %w(android.view.GestureDetector.SimpleOnGestureListener),
  ].each do |c, n|
    puts `ruboto gen subclass #{c} --name Ruboto#{n ? n : c.split(".")[-1]} --method_base on --force exclude --package org.ruboto.callbacks`
  end

  # Subclasses added after minSDK
  [
    %w(android.view.ScaleGestureDetector.SimpleOnScaleGestureListener),
  ].each do |c, n|
    puts `ruboto gen subclass #{c} --name Ruboto#{n ? n : c.split(".")[-1]} --method_base on --force include --package org.ruboto.callbacks`
  end

  [
    %w(android.content.ContentProvider),
  ].each do |c, n|
    puts `ruboto gen subclass #{c} --name Ruboto#{n ? n : c.split(".")[-1]} --method_base abstract --force exclude --package org.ruboto.callbacks`
  end
end

# Generate callback subclasses for widgets
task :widgets do
  ruboto_dir = "../ruboto-core/bin/"
  %w(EditText TextView Button ListView ScrollView SeekBar).each do |c, n|
    puts `ruboto gen subclass android.widget.#{c} --name Ruboto#{n ? n : c.split(".")[-1]} --method_base on --force exclude --package org.ruboto.widget`
  end
end
