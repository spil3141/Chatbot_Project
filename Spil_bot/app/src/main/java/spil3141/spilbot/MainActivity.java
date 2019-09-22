package spil3141.spilbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    Button btn;
    Button btn_2;
//    WebView myWebView;
    //######################################################
    final AppCompatActivity thisActivaity = this;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    //######################################################


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        btn_2 = findViewById(R.id.btn_2);
//        myWebView  = (WebView) findViewById(R.id.webview);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivaity,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(thisActivaity,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Permission is granted",Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if Internet Permission is granted
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET)
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
                    final TextView textView = (TextView) findViewById(R.id.textView);


                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    String q = "How are you today my love, :) ? ";
                    String url = "http://spil3141.iptime.org/?q=";
                    try{
                        url += URLEncoder.encode(q, "UTF-8");
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(),"Error Encoding the URL",Toast.LENGTH_LONG).show();
                    }

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    textView.setText("Response is: "+ response);
//                                    myWebView.loadData(response, "text/html", "UTF-8");
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
        });

    }

}
