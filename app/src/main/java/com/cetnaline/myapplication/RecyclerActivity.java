package com.cetnaline.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RecyclerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        RecyclerView recyclerView = findViewById(R.id.rclist);
        MultipleItemAdapter adapter = new MultipleItemAdapter(this);
        recyclerView.setAdapter(adapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getListData();
            }
        }).start();

//        adapter.addAll(list);
//        adapter.notifyDataSetChanged();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    private void getListData(){
        HashMap<String,Object> params = new HashMap<>();
        params.put("ImageHeight", 400);
        params.put("ImageWidth", 600);
        params.put("IsHasPaNo", false);
        params.put("PageCount", 10);
        params.put("PageIndex", 1);
        params.put("PostType", "s");

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            try {
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        byte[] postDataBytes = new byte[0];
        try {
            postDataBytes = postData.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            URL url = new URL("https://10.4.99.40:88/apushzfapi_021new/json/reply/GetRegionPostsRequest");
            URLConnection rulConnection = url.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpUrlConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setConnectTimeout(2000);
            httpUrlConnection.setReadTimeout(5000);

            httpUrlConnection.getOutputStream().write(postDataBytes);

            Reader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0;) {
                sb.append((char)c);
            }
            in.close();
            httpUrlConnection.disconnect();


            String responseStr = sb.toString();
            if (TextUtils.isEmpty(responseStr)) {
                responseStr = "{}";
            }
            int statusCode = httpUrlConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == statusCode) {
                JSONObject dataJson = new JSONObject(responseStr);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
