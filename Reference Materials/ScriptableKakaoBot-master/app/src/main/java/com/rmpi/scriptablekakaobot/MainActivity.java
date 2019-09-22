package com.rmpi.scriptablekakaobot;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private static String PREFS_KEY = "bot";
    private static String ON_KEY = "on";

    private boolean granted = true;
    public static AppCompatActivity thisActivaity;
    public static Context thisContext;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static Button send_btn;
    public static TextView textView;
    public static EditText editText;
    // UI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        grantPermission();
        //KakaotalkListener.initializeScript();
        setContentView(R.layout.activity_main);
        Switch onOffSwitch = (Switch) findViewById(R.id.switch1);
        onOffSwitch.setChecked(getOn(this));
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean b) {
                putOn(getApplicationContext(), b);
            }
        });

        send_btn = (Button) findViewById(R.id.send_btn);
        textView =(TextView) findViewById(R.id.textView2);
        editText =(EditText) findViewById(R.id.editText);
        thisContext = getApplicationContext();
        thisActivaity = this;
    }

    //####################################################
    public static void Send_To_Server(String room, String msg, String sender, boolean isGroup, KakaotalkListener.SessionCacheReplier replier){
        //Check if Internet Permission is granted
        if (ContextCompat.checkSelfPermission(thisContext, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivaity,
                    Manifest.permission.INTERNET)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(thisActivaity,
                        new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(thisContext);

            String q = msg;
//                String q = editText.getText().toString();

            String url = "http://spil3141.iptime.org/?q=";
            try{
                url += URLEncoder.encode(q, "UTF-8");
            }catch(Exception e){
                Toast.makeText(thisContext,"Error Encoding the URL",Toast.LENGTH_LONG).show();
            }

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            textView.setText("Response is: "+ response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    textView.setText("That didn't work!");
//                            myWebView.loadUrl("http://www.google.com");
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }
    //####################################################


    public void onSettingsClick(View v) {
        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
    }

//    public void onReloadClick(View v) {
////        KakaotalkListener.initializeScript();
//    }

    // Util

    private void grantPermission() {
        if (Build.VERSION.SDK_INT >= 23)
            if (!(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                granted = false;
                requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
                Thread permChecker = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long currTime = System.currentTimeMillis();
                        while(!granted) if (System.currentTimeMillis() - currTime > 10000) MainActivity.this.finish();
                    }
                });
                permChecker.start();

                try {
                    permChecker.join();
                } catch (InterruptedException e) {
                    finish();
                }
            }

    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == 1)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                granted = true;
    }

    static boolean getOn(Context ctx) {
        return ctx.getSharedPreferences(PREFS_KEY, MODE_PRIVATE).getBoolean(ON_KEY, false);
    }

    private static void putOn(Context ctx, boolean value) {
        ctx.getSharedPreferences(PREFS_KEY, MODE_PRIVATE).edit().putBoolean(ON_KEY, value).apply();
    }
}
