package com.cetnaline.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);

        ImageView img4 = findViewById(R.id.img4);
        img4.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.tts, 512, 384));
    }


    private Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId,options);
        options.inSampleSize = calculateSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId,options);
    }

    private int calculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSamleSize = 1;

        if (height>reqHeight || width >reqWidth) {
            final int halfHeight = height/2;
            final int halfWidth = width/2;

            while ((halfHeight/inSamleSize)>=reqHeight && (halfWidth/inSamleSize)>=reqWidth) {
                inSamleSize*=2;
            }
        }

        return inSamleSize;
    }


    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
            out = new BufferedOutputStream(outputStream);

            int b;

            while ((b=in.read())!=-1) {
                out.write(b);
            }

            out.close();
            in.close();
            return true;
        } catch (IOException e) {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return false;
    }



}

























