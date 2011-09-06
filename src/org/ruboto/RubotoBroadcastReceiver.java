package org.ruboto;

import java.io.IOException;

public abstract class RubotoBroadcastReceiver extends android.content.BroadcastReceiver {
    private String scriptName;
    private String remoteVariable = "";
    public Object[] args;



    private Object[] callbackProcs = new Object[0];

    public void setCallbackProc(int id, Object obj) {
        callbackProcs[id] = obj;
    }
	
    public RubotoBroadcastReceiver setRemoteVariable(String var) {
        remoteVariable = ((var == null) ? "" : (var + "."));
        return this;
    }

    public void setScriptName(String name){
        scriptName = name;
    }

    /****************************************************************************************
     * 
     *  Activity Lifecycle: onCreate
     */
	
    @Override
    public void onReceive(android.content.Context context, android.content.Intent intent) {
        args = new Object[2];
        args[0] = context;
        args[1] = intent;

        if (Script.setUpJRuby(context)) {
            Script.defineGlobalVariable("$broadcast_receiver", this);
            Script.defineGlobalVariable("$broadcast_context", context);
            Script.defineGlobalVariable("$broadcast_intent", intent);
            try {
                new Script(scriptName).execute();
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
        	// FIXME(uwe): What to do if the Ruboto Core platform is missing?
        }
    }

    /****************************************************************************************
     * 
     *  Generated Methods
     */



}	


