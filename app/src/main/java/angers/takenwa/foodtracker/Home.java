package angers.takenwa.foodtracker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //creation de channel pour les notification
        createNotificationChannel();

        // verification si la base de donnée existe deja et creation si non
        boolean databaseCreated = AppPreferences.isDatabaseCreated(this);
        if (!databaseCreated) {
            // Créer la base de données
            DB dbHelper = new DB(this);
            dbHelper.getWritableDatabase();
            Toast.makeText(Home.this, "succes", Toast.LENGTH_LONG).show();

            // Mettre à jour l'indicateur de création de base de données
            AppPreferences.setDatabaseCreated(this, true);
        } else {
            Toast.makeText(Home.this, "exist", Toast.LENGTH_LONG).show();
        }

        // notification des produit qui expire dan smoins de 5 jours
        checkExpiringProducts();

        // mise à jour du nombre de jour
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            updateDaysUntilExpiry();
        }

        // Appel de la méthode pour récupérer les produits de la base de données
        List<Product> productList = getProductsFromDatabase();
    }



    private String expirationDate;

    ////************
    private DatePickerDialog datePickerDialog;
    private Button dateButton;


    private View view;
    public void launchProductListActivity(View view) {
        Intent intent = new Intent(Home.this, Product_list.class);
        startActivity(intent);
    }

    public void launchMainActivity(View view) {
        Intent intent = new Intent(Home.this, MainActivity.class);
        startActivity(intent);
    }

    public void launchSettingsActivity(View view) {
        Intent intent = new Intent(Home.this, Settings.class);
        startActivity(intent);
    }

    // afficher notification
    @SuppressLint("MissingPermission")
    public void showExpirationNotification(String productName) {//, String expirationDate
        // Créer une notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "expiration_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // ic_notification est votre icône de notification
                .setContentTitle("Alerte de péremption")
                .setContentText("Le produit " + productName + " arrive à expiration le ") //+ expirationDate)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION);


        // Afficher la notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(123, builder.build()); // L'ID 123 est utilisé pour identifier cette notification, vous pouvez utiliser un autre ID si nécessaire
    }

    // Vider DB

    public void viderBaseDeDonnees(View view) {

        SQLiteDatabase db = new DB(this).getWritableDatabase();

        // Récupérer les noms de toutes les tables de la base de données
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        List<String> tables = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                tables.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        cursor.close();

        // Supprimer toutes les données de chaque table
        for (String table : tables) {
            db.delete(table, null, null);
        }

        db.close();
    }

    // Méthode pour copier les données de la table "groceries" vers la table "products"
    @SuppressLint("Range")


    //////////// teste pour les produit qui expire dans moins de 5 jours...........
    private void checkExpiringProducts() {
        // Obtenir la date actuelle
        //Toast.makeText(Home.this, "expiringProducts", Toast.LENGTH_LONG).show();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Les mois dans Calendar commencent à partir de 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.of(year, month, day);
        }


        // Obtenir une instance de la base de données
        SQLiteDatabase db = new DB(this).getReadableDatabase();

        // Définir la date limite à 5 jours à partir de la date actuelle
        LocalDate limitDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            limitDate = currentDate.plusDays(5);
        }

        // Effectuer une requête pour récupérer les produits dont la date d'expiration est dans moins de 5 jours
        Cursor cursor = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            cursor = db.rawQuery("SELECT product_name, days_until_expiry FROM products WHERE expiration_date <= ?",
                    new String[]{limitDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))});
        }

        List<String> expiringProducts = new ArrayList<>();

        // Parcourir le curseur et ajouter les produits à la liste
        if (cursor != null && cursor.moveToFirst()) {
            int productNameIndex = cursor.getColumnIndex("product_name");
            int daysUntilExpiryIndex = cursor.getColumnIndex("days_until_expiry");
            do {
                String productName = cursor.getString(productNameIndex);
                int daysUntilExpiry = cursor.getInt(daysUntilExpiryIndex);
                expiringProducts.add(productName + " - Expire dans " + daysUntilExpiry + " jours");
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Fermer la connexion à la base de données
        db.close();

        // Si des produits expirent bientôt, afficher une notification
        if (!expiringProducts.isEmpty()) {
            showExpiringProductsNotification5(expiringProducts);
        }
    }

    // notification pour les produit qui expire dans moins de 5 jours
    @SuppressLint("MissingPermission")
    private void showExpiringProductsNotification5(List<String> expiringProducts) {
        // Créer une notification avec la liste des produits expirant bientôt
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "expiration_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Produits expirant bientôt")
                .setContentText("Certains produits de votre liste expirent dans moins de 5 jours")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(TextUtils.join("\n", expiringProducts)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER);

        // Afficher la notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(124, builder.build());
    }



    //****************** mise à jour nombre de jour avant expiration ******************

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateDaysUntilExpiry() {
        SQLiteDatabase db = new DB(this).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, expiration_date FROM products", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int productId = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String expirationDate = cursor.getString(cursor.getColumnIndex("expiration_date"));
                int daysUntilExpiry = calculateDaysUntilDate(expirationDate);

                ContentValues values = new ContentValues();
                values.put("days_until_expiry", daysUntilExpiry);

                //Toast.makeText(Home.this, daysUntilExpiry, Toast.LENGTH_LONG).show(); /////////

                db.update("products", values, "id=?", new String[]{String.valueOf(productId)});
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
    }

    //calcul du nombre de jour pour utilisation
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int calculateDaysUntilDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            // Si la chaîne est nulle ou vide, retournez une valeur par défaut
            return 0; // Ou une autre valeur par défaut appropriée dans votre contexte
        }

        // Convertit la date de chaîne en LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate expirationDate = LocalDate.parse(dateString, formatter);

        // Calcule le nombre de jours restants jusqu'à la date d'expiration
        LocalDate currentDate = LocalDate.now();
        return (int) ChronoUnit.DAYS.between(currentDate, expirationDate);
    }


    // creation du channel pour les notifications
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_name);
            String description = getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("expiration_channel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    //// Liste de produit data base

    @SuppressLint("Range")
    private List<Product> getProductsFromDatabase() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = new DB(this).getReadableDatabase();

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
            cursor.close();
        }

        db.close();
        return productList;
    }


    /// activiter affichage

    //private void launchProductListActivity(View view) {}
}