package com.example.laygo.laygo;

import com.example.laygo.laygo.model.Brick;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BrickArrayAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<Brick> bricksToFilter = null;
    private ArrayList<Brick> bricks = null;

    // Maybe change type of values to get the image path as well
    public BrickArrayAdapter(Context context, List<Brick> bricks) {
        super(context, R.layout.list_brick);
        this.context = context;
        this.bricksToFilter = bricksToFilter;
        this.bricks = new ArrayList<Brick>();
        this.bricks.addAll(bricksToFilter);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_brick, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.wordBrick);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageBrick);
        textView.setText(bricks.get(position).getWord());

        // Get image path of image 
        String imagePath = bricks.get(position).getImage();
        if(imagePath != ""){
            Uri path = Uri.parse(imagePath);
            try{
                Bitmap image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), path);
                imageView.setImageBitmap(image);
            } catch(IOException e ){
                // TODO
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
                if (b.getWord().toLowerCase(Locale.getDefault()).contains(charText)) 
                {
                    bricksToFilter.add(b);
                }
            }
        }
        notifyDataSetChanged();
    }
}