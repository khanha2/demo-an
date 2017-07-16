package com.an.demo.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.an.demo.R;
import com.an.demo.adapters.FileAdapter;
import com.an.demo.services.APIService;
import com.an.demo.services.FilesService;

import java.io.File;

public class GalleryActivity extends AppCompatActivity {

    private ListView imagesListView;

    private File[] mFiles;

    private String currentFilePathSelected;

    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFilePathSelected != null) {
                    imagesListView.setVisibility(View.GONE);
                    floatingActionButton.setVisibility(View.GONE);
                    ProcessFileTask processFileTask = new ProcessFileTask(currentFilePathSelected);
                    processFileTask.execute((Void) null);
                } else {
                    Snackbar.make(view, "Please choose a file to process.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        mFiles = FilesService.getInstance().getImageFiles(this);
        FileAdapter adapter = new FileAdapter(this, mFiles);

        imagesListView = ((ListView) findViewById(R.id.images_listview));
        imagesListView.setAdapter(adapter);
        imagesListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        imagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                currentFilePathSelected = mFiles[position].getAbsolutePath();
            }
        });
    }


    private void processFile(String filePath) {
        Snackbar.make(floatingActionButton,
                "Processing image file: " + filePath,
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        try {
            APIService apiService = APIService.getInstance();
            apiService.sendRequest(filePath);
            if (apiService.getResult() == null) {
                Snackbar.make(floatingActionButton,
                        "Error has occured when processing. Try to process demo image.",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                apiService.sendDemoRequest();
            }
            if (apiService.getResult() != null) {
                showResult();
            }
        } catch (Exception e) {
            Snackbar.make(floatingActionButton, e.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            // show error
        }
    }

    private void showResult() {
        Intent intent = new Intent(this, ResultActivity.class);
        startActivity(intent);
    }

    private class ProcessFileTask extends AsyncTask<Void, Void, Boolean> {

        private final String mFilePath;

        public ProcessFileTask(String filePath) {
            mFilePath = filePath;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            processFile(mFilePath);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            imagesListView.setVisibility(View.VISIBLE);
            floatingActionButton.setVisibility(View.VISIBLE);
        }
    }
}
