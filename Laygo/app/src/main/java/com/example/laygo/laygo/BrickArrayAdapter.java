import com.mkyong.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BrickArrayAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<Brick> bricksToFilter = null;
    private ArrayList<Brick> bricks = null;

    // Maybe change type of values to get the image path as well
    public BrickArrayAdapter(Context context, List<Brick> bricks) {
        super(context, R.layout.list_brick, values);
        this.context = context;
        this.bricksToFilter = bricksToFilter;
        this.bricks = new ArrayList<WorldPopulation>();
        this.bricks.addAll(bricksToFilter);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_mobile, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.wordBrick);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageBric);
        textView.setText(bricks[position].getWord());

        // Get image path of image 
        String imagePath = "";
        imageView.setImageResource(imagePath);
        
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