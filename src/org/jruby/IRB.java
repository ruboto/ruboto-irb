package org.jruby;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.jruby.exceptions.RaiseException;
import org.jruby.parser.EvalStaticScope;
import org.jruby.runtime.DynamicScope;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.scope.ManyVarsDynamicScope;

public class IRB extends Activity
{
    private Ruby ruby;

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

        DynamicScope newScope = null;
        try {
            ruby = Ruby.newInstance(config);

            ThreadContext context = ruby.getCurrentContext();
            DynamicScope currentScope = context.getCurrentScope();
            newScope = new ManyVarsDynamicScope(new EvalStaticScope(currentScope.getStaticScope()), currentScope);
        } catch (Throwable t) {
            tv.setText("");
            t.printStackTrace(textViewStream);
        }
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
