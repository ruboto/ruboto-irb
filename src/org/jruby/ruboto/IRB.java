package org.jruby.ruboto;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;
import org.jruby.exceptions.RaiseException;
import org.jruby.parser.EvalStaticScope;
import org.jruby.runtime.DynamicScope;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.scope.ManyVarsDynamicScope;
import org.jruby.javasupport.JavaUtil;

public class IRB extends Activity
{
    private Ruby ruby;
    private static final String TAG = "IRB";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final TextView tv = (TextView)findViewById(R.id.text);        
        tv.setMovementMethod(new android.text.method.ScrollingMovementMethod());
        
        RubyInstanceConfig config = new RubyInstanceConfig();
        config.setCompileMode(RubyInstanceConfig.CompileMode.OFF);
        final PrintStream textViewStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int arg0) throws IOException {
                tv.append(Character.toString((char)arg0));
            }
        });
        config.setOutput(textViewStream);
        config.setLoader(getClass().getClassLoader());
        //uncomment for debugging
        //config.processArguments(new String[] {"-d"});

        ruby = Ruby.newInstance(config);
        // make Activity available in IRB
        ruby.defineGlobalConstant("Activity", JavaUtil.convertJavaToRuby(ruby, this));
        
        Log.d(TAG, "initialized JRuby instance");
        
        ThreadContext context = ruby.getCurrentContext();
        DynamicScope currentScope = context.getCurrentScope();
        final DynamicScope scope = new ManyVarsDynamicScope(new EvalStaticScope(currentScope.getStaticScope()), currentScope);
        final HistoryEditText et = (HistoryEditText)findViewById(R.id.edit);        

        et.setLineListener(new HistoryEditText.LineListener() {                  
            public void onNewLine(String rubyCode) {                                    
                tv.append(rubyCode + "\n");
                try {
                    String inspected = ruby.evalScriptlet(rubyCode, scope).inspect().asJavaString();
                    tv.append("=> " + inspected + "\n");
                } catch (RaiseException re) {
                    Log.w(TAG, "exception", re);                
                    re.printStackTrace(textViewStream);
                }
                tv.append(">> ");
                et.setText("");               
            }
        });            
        tv.setText(">> ");
    }
    

    /** EditText with history (key down, key up) */
    public static class HistoryEditText extends EditText implements
      android.view.View.OnKeyListener,
      TextView.OnEditorActionListener
    { 
      public interface LineListener {
        void onNewLine(String s);
      }     

      private int cursor = -1;
      private List<String> history = new ArrayList<String>();
      private LineListener listener;
      
      public HistoryEditText(Context ctxt) {
        super(ctxt);
        initListeners();
      }
      
      public HistoryEditText(Context ctxt,  android.util.AttributeSet attrs) {
        super(ctxt, attrs);    
        initListeners();
      }
      
      public HistoryEditText(Context ctxt,  android.util.AttributeSet attrs, int defStyle) {
        super(ctxt, attrs, defStyle);
        initListeners();
      }
      
      private void initListeners() {
        setOnKeyListener(this);
        setOnEditorActionListener(this);
      }
      
      public boolean onEditorAction(TextView arg0, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_NULL) {
          String line = getText().toString();
          if (line.length() == 0) return true;           
          history.add(line);        
          cursor = history.size();
                      
          if (listener != null) {
            listener.onNewLine(line);
            return true;
          }
        }
        return false;
      }
      
      public void setLineListener(LineListener l) { this.listener = l; }
      
      public void setCursorPosition(int pos) {
        Selection.setSelection(getText(), pos);
      }
      
      public boolean onKey(View view, int keyCode, KeyEvent evt) {        
        if (evt.getAction() == KeyEvent.ACTION_DOWN || evt.getAction() == KeyEvent.ACTION_MULTIPLE) {

          if (cursor >= 0 && (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN)) {                        
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP ) {
              cursor -= 1;
            } else {
              cursor += 1;
            }
            
            if (cursor < 0)
              cursor = 0;
            else if (cursor >= history.size()) {
              cursor = history.size() - 1;
            }
            setText(history.get(cursor));
            return true;
          } 
          /*
          else if (evt.isAltPressed()) {            
            if (keyCode == KeyEvent.KEYCODE_A) {
                setCursorPosition(0);
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_Z) {            
                setCursorPosition(getText().length());
                return true;
            }         
          }
          */
        }
        return false;
      }
    }    
}
