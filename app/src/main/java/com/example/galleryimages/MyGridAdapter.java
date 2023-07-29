package com.example.galleryimages;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class MyGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mData;
    public MyGridAdapter(Context context, List<String> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        @SuppressLint("ViewHolder") View v = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
        imageView = v.findViewById(R.id.image);
        Bitmap bitmap = BitmapFactory.decodeFile(mData.get(position));
        imageView.setImageBitmap(bitmap);
        return v;
    }




}