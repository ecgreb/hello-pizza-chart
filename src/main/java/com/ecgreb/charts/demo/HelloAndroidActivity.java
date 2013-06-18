package com.ecgreb.charts.demo;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HelloAndroidActivity extends Activity {
    private final String TAG = getClass().getSimpleName();

    public static final String ASSET_PATH = "file:///android_asset/";

    private WebView webView;
    private EditText mushroomsField;
    private EditText onionsField;
    private EditText olivesField;
    private EditText pepperoniField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mushroomsField = (EditText) findViewById(R.id.mushrooms);
        onionsField = (EditText) findViewById(R.id.onions);
        olivesField = (EditText) findViewById(R.id.olives);
        pepperoniField = (EditText) findViewById(R.id.pepperoni);

        initReloadButton();
        initImeActionReload();

        loadChart();
    }

    private void initReloadButton() {
        Button reloadButton = (Button) findViewById(R.id.button);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadChart();
            }
        });
    }

    private void initImeActionReload() {
        pepperoniField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL) {
                    loadChart();
                    return true;
                }

                return false;
            }
        });
    }

    private void loadChart() {
        if (TextUtils.isEmpty(mushroomsField.getText())) {
            mushroomsField.setText("0");
            return;
        }

        if (TextUtils.isEmpty(onionsField.getText())) {
            onionsField.setText("0");
            return;
        }

        if (TextUtils.isEmpty(olivesField.getText())) {
            olivesField.setText("0");
            return;
        }

        if (TextUtils.isEmpty(pepperoniField.getText())) {
            pepperoniField.setText("0");
            return;
        }

        hideSoftKeyboard(mushroomsField);
        hideSoftKeyboard(onionsField);
        hideSoftKeyboard(olivesField);
        hideSoftKeyboard(pepperoniField);

        int mushrooms = Integer.parseInt(mushroomsField.getText().toString());
        int onions = Integer.parseInt(onionsField.getText().toString());
        int olives = Integer.parseInt(olivesField.getText().toString());
        int pepperoni = Integer.parseInt(pepperoniField.getText().toString());

        String content = null;
        try {
            AssetManager assetManager = getAssets();
            InputStream in = assetManager.open("graph-test.html");
            byte[] bytes = readFully(in);
            content = new String(bytes, "UTF-8");
        } catch (IOException e) {
            Log.e(TAG, "An error occurred.", e);
        }

        String formattedContent = String.format(content, mushrooms, onions, olives, pepperoni);
        webView.loadDataWithBaseURL(ASSET_PATH, formattedContent, "text/html", "utf-8", null);
        webView.requestFocusFromTouch();
    }

    private void hideSoftKeyboard(TextView v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private static byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }
}
