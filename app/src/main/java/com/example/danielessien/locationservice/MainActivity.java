package com.example.danielessien.locationservice;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements LocationListener {

    LocationManager locationManager;
    TextView textView;
    TextView messages;
    WebView webView;
    double latitude = 0;
    double longitude = 0;
    boolean heardFromGPS = false;
    boolean heardFromWebView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        mapViewInit();

        /*webView.setWebViewClient(new WebViewClient() {
            //Called when the WebView has finished loading the page of HTML.
            @Override
            public void onPageFinished(WebView view, String url) {

                heardFromWebView = true;

                Toast toast = Toast.makeText(MainActivity.this, "Latitude "+latitude + " Longitude: "+longitude,
                        Toast.LENGTH_LONG);
                toast.show();
                //Woolworth Building
                //float latitude = latitude;
                //float longitude = -74.008056f;
                float setLatitude = (float)latitude;
                float setLongitude = (float)longitude;

                if(heardFromGPS && heardFromWebView) {
                    int zoom = 15;
                    String javascript = String.format("javascript:mapFunction(%f, %f, %d)",
                            setLatitude, setLongitude, zoom);
                    view.loadUrl(javascript);
                }
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,	//or LocationManager.NETWORK_PROVIDER, or both
                0L,		//minimum time interval (milliseconds) between notifications
                0.0f,		//minimum change in distance (meters) between notifications
                this
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        else if(id == R.id.action_home){
            dashboardInit();
        }
        else if (id == R.id.action_refresh){
            refreshMap();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProviderEnabled(String provider) {
        //messages.append("onProviderEnabled " + provider);
        Toast toast = Toast.makeText(MainActivity.this, provider,
                Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        //messages.append("onProviderDisabled " + provider);
        Toast toast = Toast.makeText(MainActivity.this, provider,
                Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        String s = String.format(
                "Latitude  %20.15g\u00B0\n"   //Unicode degree symbol
                        + "Longitude %20.15g\u00B0\n",
                latitude, longitude
        );

        webView.setWebViewClient(new WebViewClient() {
            //Called when the WebView has finished loading the page of HTML.
            @Override
            public void onPageFinished(WebView view, String url) {

                heardFromGPS = true;

                //Woolworth Building
                //float latitude = latitude;
                //float longitude = -74.008056f;
                float setLatitude = (float)latitude;
                float setLongitude = (float)longitude;

                //if(heardFromGPS && heardFromWebView) {
                    int zoom = 15;
                    String javascript = String.format("javascript:mapFunction(%f, %f, %d)",
                            setLatitude, setLongitude, zoom);
                    view.loadUrl(javascript);
                //}
            }
        });
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //textView.append("Status of provider " + provider + " has changed to ");
        String s;

        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                s = "OUT_OF_SERVICE";
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                s = "TEMPORARILY_UNAVAILABLE";
                break;
            case LocationProvider.AVAILABLE:
                s = "AVAILABLE";
                break;
            default:
                s = "unknown";
                break;
        }

        /*textView.append(s + "\n");
        if (extras != null) {
            messages.append(extras.toString() + "\n");
        }
        messages.append("\n");*/

        Toast toast = Toast.makeText(MainActivity.this, provider,
                Toast.LENGTH_LONG);
        toast.show();
    }

    public void mapViewInit(){
        setContentView(R.layout.map_layout);

        webView = (WebView)findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);


        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        webView.loadUrl("file:///android_asset/map.html");

    }

    public void refreshMap(){
        webView.loadUrl("file:///android_asset/map.html");

    }

    public void dashboardInit(){
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapViewInit();
                webView.loadUrl("file:///android_asset/map.html");
            }
        });
    }
}
