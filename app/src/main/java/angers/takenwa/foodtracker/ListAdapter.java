package angers.takenwa.foodtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<ListData> {
    public ListAdapter(@NonNull Context context, ArrayList<ListData> dataArrayList) {
        super(context, R.layout.list_item, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ListData listData=getItem(position);
        if (view==null) {
            view= LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ImageView listImage=view.findViewById(R.id.listImage);
        TextView listName=view.findViewById(R.id.listName);
        TextView listDate=view.findViewById(R.id.listDate);

        listImage.setImageResource(listData.image);
        listName.setText(listData.name);
        listDate.setText(listData.date);

        return view;
    }
}
