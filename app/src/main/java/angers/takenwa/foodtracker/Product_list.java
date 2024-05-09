package angers.takenwa.foodtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;

public class Product_list extends AppCompatActivity {
    RecyclerView recyclerView;
    private Context mContext; // Déclarez une variable de contexte
    List<Product> productList; // Ne pas appeler getProductsFromDatabase() ici

    ProductRecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        mContext = this;

        recyclerView = findViewById(R.id.news_recycler_view);

        progressIndicator = findViewById(R.id.progress_bar);

        productList = getProductsFromDatabase(); // Appeler getProductsFromDatabase() ici

        setupRecyclerView();

    }

    void setupRecyclerView(){
        recyclerView.setLayoutManager (new LinearLayoutManager( this));
        adapter = new ProductRecyclerAdapter(productList);
        recyclerView. setAdapter (adapter);
    }

    void changeInProgress(boolean show){

        if (show)
            progressIndicator.setVisibility(View.VISIBLE);
        else
            progressIndicator.setVisibility(View.INVISIBLE);
    }

    void updateData(List<Product> data){
        productList.clear();
        productList.addAll(data);
        adapter.notifyDataSetChanged(); // Mettre à jour l'adapter
    }

    @SuppressLint("Range")
    private List<Product> getProductsFromDatabase() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = new DB(mContext).getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM products", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setCodeBare(cursor.getString(cursor.getColumnIndex("code_bare")));
                product.setProductName(cursor.getString(cursor.getColumnIndex("product_name")));
                product.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
                product.setExpirationDate(cursor.getString(cursor.getColumnIndex("expiration_date")));
                product.setDaysUntilExpiry(cursor.getInt(cursor.getColumnIndex("days_until_expiry")));
                product.setEnergy(cursor.getDouble(cursor.getColumnIndex("energy")));
                product.setEnergyKcal(cursor.getDouble(cursor.getColumnIndex("energy_kcal")));
                product.setEnergyUnit(cursor.getString(cursor.getColumnIndex("energy_unit")));
                product.setFat100g(cursor.getDouble(cursor.getColumnIndex("fat_100g")));
                product.setFat(cursor.getDouble(cursor.getColumnIndex("fat")));
                product.setFatUnit(cursor.getString(cursor.getColumnIndex("fat_unit")));
                product.setProteins(cursor.getDouble(cursor.getColumnIndex("proteins")));
                product.setProteinsUnit(cursor.getString(cursor.getColumnIndex("proteins_unit")));
                product.setSalt(cursor.getDouble(cursor.getColumnIndex("salt")));
                product.setSaltUnit(cursor.getString(cursor.getColumnIndex("salt_unit")));
                product.setSugars(cursor.getDouble(cursor.getColumnIndex("sugars")));
                product.setSugarsUnit(cursor.getString(cursor.getColumnIndex("sugars_unit")));
                product.setAllergensTags(cursor.getString(cursor.getColumnIndex("allergens_tags")));
                product.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                product.setStatusVerbose(cursor.getString(cursor.getColumnIndex("status_verbose")));
                product.setImageUri(cursor.getString(cursor.getColumnIndex("image_uri")));

                productList.add(product);
            } while (cursor.moveToNext());
            Toast.makeText(Product_list.this, "création de la liste de produit", Toast.LENGTH_LONG).show();
            cursor.close();
        }

        db.close();
        return productList;
    }

}
