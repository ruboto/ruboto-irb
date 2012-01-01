package org.jruby.ruboto.tests;

import org.jruby.ruboto.*;
import android.test.ActivityInstrumentationTestCase;
import org.ruboto.irb.*;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class org.jruby.IRBTest \
 * org.jruby.tests/android.test.InstrumentationTestRunner
 */
public class IRBTest extends ActivityInstrumentationTestCase<IRB> {

    public IRBTest() {
        super("org.jruby.ruboto", IRB.class);
    }

}
