package spil3141.spilbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    //######################################################
    Button btn;
    public EditText editText;
    public TextView textView;
    final AppCompatActivity thisActivaity = this;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    //######################################################


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn_send);
        editText = findViewById(R.id.editText);
        textView  = (TextView) findViewById(R.id.textView);
        editText.setHint("empty");

        btn.setOnClickListener(new View.OnClickListener() {
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
                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

//                    String q = "How are you today my love, :) ? ";
                    String q = editText.getText().toString();
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
