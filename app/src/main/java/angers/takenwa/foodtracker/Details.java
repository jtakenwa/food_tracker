package angers.takenwa.foodtracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import angers.takenwa.foodtracker.databinding.ActivityDetailsBinding;
import angers.takenwa.foodtracker.databinding.ActivityListBinding;

public class Details extends AppCompatActivity {

    ActivityDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_details);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        binding=ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent=this.getIntent();

        if (intent!=null){
            String name=intent.getStringExtra("name");
            String date=intent.getStringExtra("date");
            int image=intent.getIntExtra("image", R.drawable.beer);

            binding.detailName.setText(name);
            binding.detailDate.setText(date);
            binding.detailImage.setImageResource(image);
        }
    }
}