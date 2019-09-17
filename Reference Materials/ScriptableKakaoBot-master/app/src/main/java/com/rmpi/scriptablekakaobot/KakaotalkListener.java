package com.rmpi.scriptablekakaobot;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.widget.Toast;


import com.faendir.rhino_android.RhinoAndroidHelper;
import org.mozilla.javascript.*;

import java.io.File;
import java.io.FileReader;


public class KakaotalkListener extends NotificationListenerService {
    private static Function responder;
    private static ScriptableObject execScope;
    private static android.content.Context execContext;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        if (!MainActivity.getOn(getApplicationContext())) return;

        //This can be used to get notification from other applications.
        //Toast.makeText(getApplicationContext(),sbn.getPackageName(),Toast.LENGTH_LONG).show();

        if (sbn.getPackageName().equals("com.kakao.talk")) {
            Notification.WearableExtender wExt = new Notification.WearableExtender(sbn.getNotification());
            for (Notification.Action act : wExt.getActions())
                if (act.getRemoteInputs() != null && act.getRemoteInputs().length > 0)
                    if (act.title.toString().toLowerCase().contains("reply") ||
                            act.title.toString().toLowerCase().contains("Reply") ||
                            act.title.toString().toLowerCase().contains("답장")) {
                        execContext = getApplicationContext();
                        callResponder(sbn.getNotification().extras.getString("android.title"), sbn.getNotification().extras.get("android.text"), act);
                    }
        }
    }

    static void initializeScript() {
        try {
            File scriptDir = new File(Environment.getExternalStorageDirectory() + File.separator + "kbot");
            if (!scriptDir.exists())
                scriptDir.mkdirs();
            File script = new File(scriptDir, "response.js");
            if (!script.exists())
                script.createNewFile();
            Context parseContext = RhinoAndroidHelper.prepareContext();
            parseContext.setOptimizationLevel(-1);
            Script script_real = parseContext.compileReader(new FileReader(script), script.getName(), 0, null);
            ScriptableObject scope = parseContext.initStandardObjects();
            execScope = scope;
            script_real.exec(parseContext, scope);
            responder = (Function) scope.get("response", scope);
            Context.exit();
        } catch (Exception e) {
            Log.e("parser", "?", e);
            Process.killProcess(Process.myPid());
            return;
        }
    }

    private void callResponder(String room, Object msg, Notification.Action session) {
        if (responder == null || execScope == null)
            initializeScript();
        Context parseContext = RhinoAndroidHelper.prepareContext();
        parseContext.setOptimizationLevel(-1);
        String sender;
        String _msg;

        if (msg instanceof String) {
            sender = room;
            _msg = (String) msg;
        } else {
            String html = Html.toHtml((SpannableString) msg);
            sender = Html.fromHtml(html.split("<b>")[1].split("</b>")[0]).toString();
            _msg = Html.fromHtml(html.split("</b>")[1].split("</p>")[0].substring(1)).toString();
        }
        try {
            DjangoSpilbotConnector.displaySenderofMessage(getApplicationContext(),sender);
            responder.call(parseContext, execScope, execScope, new Object[] { room, _msg, sender, msg instanceof SpannableString, new SessionCacheReplier(session) });
        } catch (Throwable e) {
            Log.e("parser", "?", e);
        }

        Context.exit();
    }

    public static class SessionCacheReplier {
        private Notification.Action session = null;

        private SessionCacheReplier(Notification.Action session) {
            super();
            this.session = session;
        }

        public void reply(String value) {
            if (session == null) return;
            Intent sendIntent = new Intent();
            Bundle msg = new Bundle();
            for (RemoteInput inputable : session.getRemoteInputs()) msg.putCharSequence(inputable.getResultKey(), value);
            RemoteInput.addResultsToIntent(session.getRemoteInputs(), sendIntent, msg);
            try {
                session.actionIntent.send(execContext, 0, sendIntent);
            } catch (PendingIntent.CanceledException e) {
            }
        }
    }

}
