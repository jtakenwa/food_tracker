package angers.takenwa.foodtracker;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private String expirationDate;

    ////************
    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d("MainActivity", "Cancelled scan");
                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                    } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                        Toast.makeText(MainActivity.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("MainActivity", "Scanned");
                    Toast.makeText(MainActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    //addProductToDatabase(result.getContents());
                    //fetchAndAddProductToDatabase(result.getContents());




                    ////////////////////////


                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Entrez la date de péremption (YYYY.MM.JJ)");

// Ajoutez un EditText pour que l'utilisateur puisse saisir la date de péremption
                    final EditText input = new EditText(this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String enteredDate = input.getText().toString();
                            if (isValidDate(enteredDate)) {
                                // La date est valide, poursuivre avec l'ajout du produit à la base de données
                                expirationDate = enteredDate;
                                fetchAndAddProductToDatabase(result.getContents());
                            } else {
                                // La date n'est pas valide, afficher un message et demander à l'utilisateur de réessayer
                                Toast.makeText(getApplicationContext(), "Date invalide. Veuillez entrer une date valide (YYYY.MM.JJ) et non dépassée", Toast.LENGTH_SHORT).show();
                            }
                        }
                        private boolean isValidDate(String date) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                                sdf.setLenient(false);
                                Date parsedDate = sdf.parse(date);

                                // Vérifier si la date est dans le futur
                                Date currentDate = new Date();
                                if (parsedDate.after(currentDate)) {
                                    return true;
                                } else {
                                    return false;
                                }
                            } catch (ParseException e) {
                                return false;
                            }
                        }

                    });

                    builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }

                        private boolean isValidDate(String date) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                                sdf.setLenient(false);
                                Date parsedDate = sdf.parse(date);

                                // Vérifier si la date est dans le futur
                                Date currentDate = new Date();
                                if (parsedDate.after(currentDate)) {
                                    return true;
                                } else {
                                    return false;
                                }
                            } catch (ParseException e) {
                                return false;
                            }
                        }

                    });

                    builder.show();


                    /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Entrez la date de péremption (YYYY.MM.JJ)");

                    // Ajoutez un EditText pour que l'utilisateur puisse saisir la date de péremption
                    final EditText input = new EditText(this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            expirationDate = input.getText().toString();
                            fetchAndAddProductToDatabase(result.getContents());/////////


                        }
                    });

                    builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();   */

                    //////////////******************************************



                    ///////////////////////////////////////////

                }
            });
    private View view;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //creation de channel pour les notification
        createNotificationChannel();

        // verification si la base de donnée existe deja et creation si non
        boolean databaseCreated = AppPreferences.isDatabaseCreated(this);
        if (!databaseCreated) {
            // Créer la base de données
            DB dbHelper = new DB(this);
            dbHelper.getWritableDatabase();
            Toast.makeText(MainActivity.this, "succes", Toast.LENGTH_LONG).show();

            // Mettre à jour l'indicateur de création de base de données
            AppPreferences.setDatabaseCreated(this, true);
        } else {
            Toast.makeText(MainActivity.this, "exist", Toast.LENGTH_LONG).show();
        }

        // notification des produit qui expire dan smoins de 5 jours
        checkExpiringProducts();

        // mise à jour du nombre de jour
        updateDaysUntilExpiry();

        // Appel de la méthode pour récupérer les produits de la base de données
        List<Product> productList = getProductsFromDatabase();





    }


    public void scanBarcodeCustomLayout(View view) {
        ScanOptions options = new ScanOptions();
        options.setCaptureActivity(AnyOrientationCaptureActivity.class);
        options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES);
        options.setPrompt("Scan something");
        options.setOrientationLocked(false);
        options.setBeepEnabled(false);
        barcodeLauncher.launch(options);


    }

    //********** Appel API **********

    private void fetchAndAddProductToDatabase(String code_bare) {
         //code_bare = "3168930010883"; ////////////////
        String apiUrl = "https://world.openfoodfacts.net/api/v2/product/" + code_bare + "?fields=product_name,nutriscore_data,grade,allergens_tags,categories_hierarchy,conservation_conditions,nutriments,countries,customer_service,id,preparation,product_quantity,ingredients_text,misc_tags";
        new FetchProductDataTask().execute(apiUrl);

    }

    public void launchProductListActivity(View view) {
        Intent intent = new Intent(MainActivity.this, Product_list.class);
        startActivity(intent);
    }


    private class FetchProductDataTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            String apiUrl = strings[0];
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return new JSONObject(stringBuilder.toString());
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException | JSONException e) {
                Log.e("FetchProductDataTask", "Error fetching product data", e);
                return null;
            }
        }

        @Override

        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (jsonObject != null) {
                try {
                    JSONObject product = jsonObject.getJSONObject("product");
                    String code_bare = jsonObject.getString("code");
                    String productName = product.getString("product_name");

                    JSONObject nutriscoreData = product.optJSONObject("nutriscore_data");
                    String grade = nutriscoreData != null ? nutriscoreData.optString("grade") : "";

                    JSONObject nutriments = product.getJSONObject("nutriments");
                    double energy = nutriments.optDouble("energy");
                    double energyKcal = nutriments.optDouble("energy-kcal");
                    String energyUnit = nutriments.optString("energy_unit", "");
                    double fat100g = nutriments.optDouble("fat_100g");
                    double fat = nutriments.optDouble("fat");
                    String fatUnit = nutriments.optString("fat_unit", "");
                    double proteins = nutriments.optDouble("proteins");
                    String proteinsUnit = nutriments.optString("proteins_unit", "");
                    double salt = nutriments.optDouble("salt");
                    String saltUnit = nutriments.optString("salt_unit", "");
                    double sugars = nutriments.optDouble("sugars");
                    String sugarsUnit = nutriments.optString("sugars_unit", "");
                    JSONArray allergensArray = product.getJSONArray("allergens_tags");
                    String allergensTags = allergensArray.length() > 0 ? allergensArray.getString(0) : "";
                    int status = jsonObject.getInt("status");
                    String statusVerbose = jsonObject.getString("status_verbose");
                    int daysUntilExpiry = calculateDaysUntilDate(expirationDate);

                    String imageUri = buildImageUrl(code_bare);

                    addProductToDatabase(code_bare, productName, grade,expirationDate,daysUntilExpiry, energy, energyKcal, energyUnit, fat100g, fat, fatUnit, proteins, proteinsUnit, salt, saltUnit, sugars, sugarsUnit, allergensTags, String.valueOf(status), statusVerbose,imageUri);
                } catch (JSONException e) {
                    Log.e("FetchProductDataTask", "Error parsing JSON", e);
                }
            } else {
                Log.e("FetchProductDataTask", "JSON object is null");
            }
        }


        ///// formatage de l'url pour l'image

        public String buildImageUrl(String barcode) {
            // Supprime les caractères non numériques du code-barres
            String cleanBarcode = barcode.replaceAll("[^\\d.]", "");

            // Vérifie que la longueur du code-barres est correcte (13 caractères)
            if (cleanBarcode.length() != 13) {
                return null; // La longueur du code-barres n'est pas valide
            }

            // Divise le code-barres en groupes de trois caractères pour les trois premiers groupes et quatre pour le dernier
            String[] groups = new String[4];
            for (int i = 0; i < 3; i++) {
                groups[i] = cleanBarcode.substring(i * 3, (i + 1) * 3);
            }
            groups[3] = cleanBarcode.substring(9);

            // Construit l'URL de l'image en remplaçant les parties de l'URL par les groupes extraits
            String imageUrl = "https://images.openfoodfacts.org/images/products/{group1}/{group2}/{group3}/{group4}/front_fr.4.400.jpg"
                    .replace("{group1}", groups[0])
                    .replace("{group2}", groups[1])
                    .replace("{group3}", groups[2])
                    .replace("{group4}", groups[3]);

            return imageUrl;
        }


        //// calculer le temps qui reste avant la peremption
        public int calculateDaysUntilDate(String dateString) {
            // Diviser la chaîne de caractères en année, mois et jour
            String[] parts = dateString.split("\\.");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            // Obtenir la date actuelle
            LocalDate currentDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                currentDate = LocalDate.now();
            }

            // Calculer le nombre de jours restants avant la date spécifiée
            LocalDate specifiedDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                specifiedDate = LocalDate.of(year, month, day);
            }
            int daysUntilDate = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                daysUntilDate = (int) currentDate.until(specifiedDate).getDays();
            }

            return daysUntilDate;
        }

    }

    // Ajouter élement à la BD

    public void addProductToDatabase(String code_bare, String productName, String grade,String expirationDate, int daysUntilExpiry, double energy, double energyKcal,
                                     String energyUnit, double fat100g, double fat, String fatUnit,
                                     double proteins, String proteinsUnit, double salt, String saltUnit,
                                     double sugars, String sugarsUnit, String allergensTags,
                                     String status, String statusVerbose,String imageUri) {
        SQLiteDatabase db = new DB(this).getWritableDatabase();

        // Vérifie si un produit avec le même code-barres existe déjà dans la base de données
        Cursor cursor = db.rawQuery("SELECT * FROM products WHERE code_bare=?", new String[]{code_bare});
        if (cursor != null && cursor.moveToFirst()) {
            // Le produit existe déjà, donc met à jour ses informations
            @SuppressLint("Range") int productId = cursor.getInt(cursor.getColumnIndex("id"));
            ContentValues values = new ContentValues();
            values.put("product_name", productName);
            values.put("grade", grade);
            values.put("expiration_date", expirationDate);
            values.put("days_until_expiry", daysUntilExpiry);
            values.put("energy", energy);
            values.put("energy_kcal", energyKcal);
            values.put("energy_unit", energyUnit);
            values.put("fat_100g", fat100g);
            values.put("fat", fat);
            values.put("fat_unit", fatUnit);
            values.put("proteins", proteins);
            values.put("proteins_unit", proteinsUnit);
            values.put("salt", salt);
            values.put("salt_unit", saltUnit);
            values.put("sugars", sugars);
            values.put("sugars_unit", sugarsUnit);
            values.put("allergens_tags", allergensTags);
            values.put("status", status);
            values.put("status_verbose", statusVerbose);
            values.put("image_uri", imageUri);

            db.update("products", values, "id=?", new String[]{String.valueOf(productId)});
            cursor.close();
            //Toast.makeText(MainActivity.this, "upload success", Toast.LENGTH_LONG).show();
        } else {
            // Le produit n'existe pas, donc l'ajoute à la base de données
            ContentValues values = new ContentValues();
            values.put("code_bare", code_bare);
            values.put("product_name", productName);
            values.put("grade", grade);
            values.put("expiration_date", expirationDate);
            values.put("days_until_expiry", daysUntilExpiry);
            values.put("energy", energy);
            values.put("energy_kcal", energyKcal);
            values.put("energy_unit", energyUnit);
            values.put("fat_100g", fat100g);
            values.put("fat", fat);
            values.put("fat_unit", fatUnit);
            values.put("proteins", proteins);
            values.put("proteins_unit", proteinsUnit);
            values.put("salt", salt);
            values.put("salt_unit", saltUnit);
            values.put("sugars", sugars);
            values.put("sugars_unit", sugarsUnit);
            values.put("allergens_tags", allergensTags);
            values.put("status", status);
            values.put("status_verbose", statusVerbose);
            values.put("image_uri", imageUri);

            db.insert("products", null, values);
        }

        //Toast.makeText(MainActivity.this, "add success", Toast.LENGTH_LONG).show();
        //db.close();

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

    //////////// teste pour les produit qui expire dans moins de 5 jours...........
    private void checkExpiringProducts() {
        // Obtenir la date actuelle
        //Toast.makeText(MainActivity.this, "expiringProducts", Toast.LENGTH_LONG).show();
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

                //Toast.makeText(MainActivity.this, daysUntilExpiry, Toast.LENGTH_LONG).show(); /////////

                db.update("products", values, "id=?", new String[]{String.valueOf(productId)});
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
    }

    //calcul du nombre de jour pour utilisation
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int calculateDaysUntilDate(String dateString) {
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
