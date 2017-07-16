package com.an.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;

/**
 * Created by knguy on 7/16/2017.
 */

public class FileAdapter extends ArrayAdapter<File> {

    private GalleryItemViewHolder viewHolder;

    public FileAdapter(@NonNull Context context, @NonNull File[] objects) {
        super(context, R.layout.activity_gallery_item, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.activity_gallery_item, parent, false);

            viewHolder = new GalleryItemViewHolder();
            viewHolder.itemView = (TextView) convertView.findViewById(R.id.file_path_text);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GalleryItemViewHolder) convertView.getTag();
        }

        File item = getItem(position);
        if (item != null) {
            viewHolder.itemView.setText(item.getName());
        }

        return convertView;
    }

    private class GalleryItemViewHolder {
        private TextView itemView;
    }
}
