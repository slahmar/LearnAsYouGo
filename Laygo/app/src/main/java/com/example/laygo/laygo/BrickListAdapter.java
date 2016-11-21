package com.example.laygo.laygo;

import com.example.laygo.laygo.model.Brick;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BrickListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    private List<Brick> bricksToFilter = null;
    private ArrayList<Brick> bricks = null;

    public BrickListAdapter(Context context, List<Brick> bricks) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.bricksToFilter = bricks;
        this.bricks = new ArrayList<Brick>();
        this.bricks.addAll(bricksToFilter);
    }

    @Override
    public int getCount() {
        return bricksToFilter.size();
    }

    @Override
    public Object getItem(int position) {
        return bricksToFilter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.list_brick, parent, false);
        TextView wordView = (TextView) rowView.findViewById(R.id.wordBrick);
        TextView translationView = (TextView) rowView.findViewById(R.id.translationBrick);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageBrick);
        wordView.setText(bricksToFilter.get(position).getWord());
        translationView.setText(bricksToFilter.get(position).getTranslation());

        // Get image path of image
        String imagePath = bricksToFilter.get(position).getImage();
        if(imagePath != null && imagePath != ""){
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Bitmap bm = BitmapFactory.decodeFile(imagePath);
                imageView.setImageBitmap(bm);
            }
        }
        return rowView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        bricksToFilter.clear();
        if (charText.length() == 0) {
            bricksToFilter.addAll(bricks);
        } 
        else 
        {
            for (Brick b : bricks) 
            {
                if (b.getWord().toLowerCase(Locale.getDefault()).contains(charText) ||
                        b.getTranslation().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    bricksToFilter.add(b);
                }
            }
        }
        notifyDataSetChanged();
    }
}