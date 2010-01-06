package org.jruby;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
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
        //comment out for debugging
        //config.processArguments(new String[] {"-d"});

        ruby = Ruby.newInstance(config);
        // makes Activity available in IRB
        ruby.defineGlobalConstant("Activity", JavaUtil.convertJavaToRuby(ruby, this));
        
        Log.d(TAG, "initialized JRuby instance");
        
        ThreadContext context = ruby.getCurrentContext();
        DynamicScope currentScope = context.getCurrentScope();
        DynamicScope newScope = new ManyVarsDynamicScope(new EvalStaticScope(currentScope.getStaticScope()), currentScope);

        final DynamicScope scope = newScope;

        final EditText et = (EditText)findViewById(R.id.edit);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_NULL) {
                    Editable rubyCode = et.getText();
                    if (rubyCode.length() == 0) return true;
                    
                    tv.append(rubyCode + "\n");
                    try {
                        String inspected = ruby.evalScriptlet(rubyCode.toString(), scope).inspect().asJavaString();
                        tv.append("=> " + inspected + "\n");
                    } catch (RaiseException re) {
                        re.printStackTrace(textViewStream);
                    }
                    tv.append(">> ");
                    et.setText("");
                    return true;
                }
                return false;
            }
        });
        tv.setText(">> ");
    }
}
