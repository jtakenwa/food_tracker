package angers.takenwa.foodtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Product_list extends AppCompatActivity implements ProductRecyclerAdapter.OnProductClickListener {
    RecyclerView recyclerView;
    private Context mContext; // Déclarez une variable de contexte
    List<Product> productList; // Ne pas appeler getProductsFromDatabase() ici

    ProductRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        mContext = this;

        recyclerView = findViewById(R.id.news_recycler_view);

        productList = getProductsFromDatabaseProduct(); // Appeler getProductsFromDatabase() ici

        setupRecyclerView();

        ProductRecyclerAdapter adapter = new ProductRecyclerAdapter(productList, this);
        recyclerView.setAdapter(adapter);

    }

    void setupRecyclerView(){
        recyclerView.setLayoutManager (new LinearLayoutManager( this));
        adapter = new ProductRecyclerAdapter(productList);
        recyclerView. setAdapter (adapter);
    }




    void updateData(List<Product> data){
        productList.clear();
        productList.addAll(data);
        adapter.notifyDataSetChanged(); // Mettre à jour l'adapter
    }

    @SuppressLint("Range")
    private List<Product> getProductsFromDatabaseProduct() {
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

    public void onProductClick(Product product) {
        Bundle bundle = new Bundle();
        //bundle.putInt("product_id", product.getId());
        bundle.putString("product_name", product.getProductName());
        bundle.putString("product_grade", product.getGrade());
        bundle.putString("expiration_date", product.getExpirationDate());
        bundle.putInt("days_until_expiry", product.getDaysUntilExpiry());
        bundle.putDouble("energy", product.getEnergy());
        bundle.putDouble("energy_kcal", product.getEnergyKcal());
        bundle.putString("energy_unit", product.getEnergyUnit());
        bundle.putDouble("fat_100g", product.getFat100g());
        bundle.putDouble("fat", product.getFat());
        bundle.putString("fat_unit", product.getFatUnit());
        bundle.putDouble("proteins", product.getProteins());
        bundle.putString("proteins_unit", product.getProteinsUnit());
        bundle.putDouble("salt", product.getSalt());
        bundle.putString("salt_unit", product.getSaltUnit());
        bundle.putDouble("sugars", product.getSugars());
        bundle.putString("sugars_unit", product.getSugarsUnit());
        bundle.putString("allergens_tags", product.getAllergensTags());
        bundle.putString("status", product.getStatus());
        bundle.putString("status_verbose", product.getStatusVerbose());
        bundle.putString("image_uri", product.getImageUri());

        Intent intent = new Intent(this, Product_details.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
