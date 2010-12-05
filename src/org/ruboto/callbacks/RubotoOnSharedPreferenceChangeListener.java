package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnSharedPreferenceChangeListener implements android.content.SharedPreferences.OnSharedPreferenceChangeListener {
  private Ruby __ruby__;

  public static final int CB_SHARED_PREFERENCE_CHANGED = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onSharedPreferenceChanged(android.content.SharedPreferences sharedPreferences, java.lang.String key) {
    if (callbackProcs[CB_SHARED_PREFERENCE_CHANGED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SHARED_PREFERENCE_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), sharedPreferences), JavaUtil.convertJavaToRuby(getRuby(), key));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
