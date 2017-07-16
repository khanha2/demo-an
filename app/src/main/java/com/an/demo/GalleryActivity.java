package com.an.demo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;

public class GalleryActivity extends AppCompatActivity {
    private ListView imagesListView;

    private File[] mFiles;

    private String currentFilePathSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFilePathSelected != null) {
                    Snackbar.make(view, currentFilePathSelected, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        mFiles = FilesManager.getInstance().getImageFiles(this);
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
}
