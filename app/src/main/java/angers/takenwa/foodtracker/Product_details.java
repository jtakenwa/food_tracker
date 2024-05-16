package angers.takenwa.foodtracker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class Product_details extends AppCompatActivity {

    private ImageView productImageView;
    private TextView nameTextView;
    private TextView gradeTextView;
    private TextView daysTextView;
    private TextView energyKcalTextView;
    private TextView proteinsTextView;
    private TextView fat100gTextView;
    private TextView saltTextView;
    private TextView sugarsTextView;
    private TextView expirationDtTextView;
    private TextView allerg;
    @SuppressLint("MissingInflatedId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Bundle bundle = getIntent().getExtras();
        Product product = null;
        if (bundle != null) {
            product = new Product();
            //product.setId(bundle.getInt("product_id"));
            product.setProductName(bundle.getString("product_name"));
            product.setGrade(bundle.getString("product_grade"));
            product.setExpirationDate(bundle.getString("expiration_date"));
            product.setDaysUntilExpiry(bundle.getInt("days_until_expiry"));
            product.setEnergy(bundle.getDouble("energy"));
            product.setEnergyKcal(bundle.getDouble("energy_kcal"));
            product.setEnergyUnit(bundle.getString("energy_unit"));
            product.setFat100g(bundle.getDouble("fat_100g"));
            product.setFat(bundle.getDouble("fat"));
            product.setFatUnit(bundle.getString("fat_unit"));
            product.setProteins(bundle.getDouble("proteins"));
            product.setProteinsUnit(bundle.getString("proteins_unit"));
            product.setSalt(bundle.getDouble("salt"));
            product.setSaltUnit(bundle.getString("salt_unit"));
            product.setSugars(bundle.getDouble("sugars"));
            product.setSugarsUnit(bundle.getString("sugars_unit"));
            product.setAllergensTags(bundle.getString("allergens_tags"));
            product.setStatus(bundle.getString("status"));
            product.setStatusVerbose(bundle.getString("status_verbose"));
            product.setImageUri(bundle.getString("image_uri"));
        }

        TextView productNameTextView = findViewById(R.id.name);
        productImageView = findViewById(R.id.image);
        gradeTextView = findViewById(R.id.grade);
        daysTextView = findViewById(R.id.days);
        energyKcalTextView = findViewById(R.id.energy_kcal);
        proteinsTextView = findViewById(R.id.proteins);
        fat100gTextView = findViewById(R.id.fat_100g);
        saltTextView = findViewById(R.id.salt);
        sugarsTextView = findViewById(R.id.sugars);
        expirationDtTextView = findViewById(R.id.expiration_dt);
        allerg = findViewById(R.id.allerg);

        String productName = getIntent().getStringExtra("product_name");
        String imageUrl = getIntent().getStringExtra("image_uri");

        gradeTextView.setText("grade: "+product.getGrade());
        daysTextView.setText(String.valueOf("expire in: "+product.getDaysUntilExpiry()+" days"));
        energyKcalTextView.setText(String.valueOf("energy: "+product.getEnergyKcal()+" g/100g"));
        proteinsTextView.setText(String.valueOf("protein: "+product.getProteins()+" g/100g"));
        fat100gTextView.setText(String.valueOf("fat: "+product.getFat100g()+" g/100g"));
        saltTextView.setText(String.valueOf("salt: "+product.getSalt()+" g/100g"));
        sugarsTextView.setText(String.valueOf("sugar: "+product.getSugars()+" g/100g"));
        expirationDtTextView.setText("expire on:"+product.getExpirationDate());
        allerg.setText("Allergen : "+product.getAllergensTags());



        productNameTextView.setText(productName);

        Picasso.get().load(imageUrl)
                .error(R.drawable.food_bank)
                .into(productImageView);


    }
}