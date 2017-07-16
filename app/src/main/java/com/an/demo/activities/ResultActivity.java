package com.an.demo.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.an.demo.R;
import com.an.demo.services.APIService;

import java.net.URL;

public class ResultActivity extends AppCompatActivity {
    private final APIService apiService = APIService.getInstance();

    private ImageView resultImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultImageView = (ImageView) findViewById(R.id.result_image);

        if (apiService.getResult() != null) {
            LoadResultTask loadResultTask = new LoadResultTask();
            loadResultTask.execute((Void) null);
        }
    }

    private class LoadResultTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            return loadImage();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                resultImageView.setImageBitmap(bitmap);
            }
        }
    }

    private Bitmap loadImage() {
        try {
            URL url = new URL(apiService.getResult().getUrl());
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception ex) {
            Log.e("app", "exception", ex);
        }
        return null;
    }
}
