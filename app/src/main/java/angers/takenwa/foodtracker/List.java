package angers.takenwa.foodtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import angers.takenwa.foodtracker.databinding.ActivityListBinding;
import angers.takenwa.foodtracker.databinding.ActivityMainBinding;

public class List extends AppCompatActivity {

    ActivityListBinding binding;
    ListAdapter listAdapter;
    ArrayList<ListData> dataArrayList=new ArrayList<>();
    ListData listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_list);
        binding= ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int[] imageList={R.drawable.pizza, R.drawable.egg, R.drawable.burger, R.drawable.fries, R.drawable.soda, R.drawable.beer};
        String[] nameList={"Pizza", "Eggs", "Burger", "Fries", "Soda", "Beer"};
        String[] dateList={"01.01.2025","01.01.2025","01.01.2025","01.01.2025","01.01.2025"};

        for (int i = 0; i < imageList.length; i++) {
            listData=new ListData(nameList[i], dateList[i], imageList[i]);
            dataArrayList.add(listData);
        }

        listAdapter=new ListAdapter(List.this, dataArrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Intent intent=new Intent(List.this, Details.class);
                intent.putExtra("name", nameList[i]);
                intent.putExtra("date", dateList[i]);
                intent.putExtra("image", imageList[i]);
                startActivity(intent);
            }
        });

    }
}