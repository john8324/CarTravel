package com.example.john8324.cartravelclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity {

    AssetManager assetManager;
    CarDataTask carDataTask;
    TextView textView;
    WebView webView;
    boolean Fan = false, Music=false;

    MediaPlayer player;



    ImageButton btncamera, btnplay, btnpause, btnfan;


    final static String LOG_TAG = "MainActivity";

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.isConnected() && info.isAvailable()) {
                return true;
            }
        }
        return false;
    }


    final WebViewClient mWebViewClient = new WebViewClient() {
        private boolean loaded = false;

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            return MainActivity.this.tryLoadUrl(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!loaded) {
                loaded = true;
                MainActivity.this.carDataTask.execute();
            }
        }
    };

    public boolean tryLoadUrl(final WebView view, final String url) {
        Log.i(LOG_TAG, "User try to load url:" + url);
        if (!isOnline()) {
            Log.i(LOG_TAG, "Failed, No internet");
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Failed, No internet")
                    .setMessage("Failed, No internet")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.tryLoadUrl(view, url); //try again
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    }).show();
            return false;
        } else {
            view.loadUrl(url);
            return true;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialization
        assetManager = getAssets();
        textView = (TextView) findViewById(R.id.textView);
        webView = (WebView) findViewById(R.id.webView);
        btncamera = (ImageButton) findViewById(R.id.img_camera);
        btnplay = (ImageButton) findViewById(R.id.img_play);
        btnfan = (ImageButton) findViewById(R.id.img_fan);
        btnpause = (ImageButton) findViewById(R.id.img_pause);

        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carDataTask.photo = true;
            }
        });
        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Toast.makeText(MainActivity.this, "Music Play", Toast.LENGTH_SHORT).show();
                    Music = true;

                player.start();

            }
        });
        btnpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Music Paused ", Toast.LENGTH_SHORT).show();
                    Music = false;

                player.pause();
            }
        });
        btnfan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Fan) {
                    Toast.makeText(MainActivity.this, "Fan Started", Toast.LENGTH_SHORT).show();
                    Fan = true;
                }
                else{
                    Toast.makeText(MainActivity.this, "Fan Closed", Toast.LENGTH_SHORT).show();
                    Fan = false;
                }

            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(mWebViewClient);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d(LOG_TAG, consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        webView.addJavascriptInterface(new JsInterface(this), "AndroidWebView");
        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        } else {
            tryLoadUrl(webView, "file:///android_asset/index.html");
        }


        carDataTask = new CarDataTask(assetManager, textView, webView);



        try {
            AssetFileDescriptor afd = getAssets().openFd("test.mp3");
            player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            player.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(LOG_TAG, "onCreate Finish");

    }

    @Override
    protected void onPause() {
        super.onPause();
        carDataTask.cancel(true);
    }

    private class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }
        @JavascriptInterface
        public void something() {
            Log.d("", "js something");
        }
    }

}


