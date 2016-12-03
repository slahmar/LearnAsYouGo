package com.example.laygo.laygo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laygo.laygo.dao.BrickDAO;
import com.example.laygo.laygo.dao.QuestionDAO;
import com.example.laygo.laygo.model.Brick;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Adapter used to display a list of bricks
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View rowView = inflater.inflate(R.layout.list_brick, parent, false);
        TextView wordView = (TextView) rowView.findViewById(R.id.wordBrick);
        TextView translationView = (TextView) rowView.findViewById(R.id.translationBrick);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageBrick);
        ImageView delete = (ImageView) rowView.findViewById(R.id.delete);
        wordView.setText(bricksToFilter.get(position).getWord());
        translationView.setText(bricksToFilter.get(position).getTranslation());
        delete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Brick toDelete = bricksToFilter.get(position);
                long brickId = toDelete.getId();
                BrickDAO bdao = new BrickDAO(context);
                bdao.open();
                bdao.deleteBrick(bricksToFilter.get(position));
                bdao.close();
                bricksToFilter.remove(position);
                QuestionDAO qdao = new QuestionDAO(context);
                qdao.open();
                qdao.deleteQuestion(brickId);
                qdao.close();
                BrickListAdapter.this.notifyDataSetChanged();
            }
        });
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

    // Filter the list of bricks with a text
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