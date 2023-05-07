package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.A;

public class ProductList extends AppCompatActivity {
    private static final String LOG_TAG = ProductList.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private FirebaseUser user;

    private FirebaseFirestore mFirestore;

    private FrameLayout redCircle;
    private TextView countTextView;
    private int cartItems = 0;
    private int gridNumber = 1;

    private NotificationHandler mNotHandler;

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<Product> mItemsData;
    private ProductAdapter mAdapter;

    private SharedPreferences preferences;
    private CollectionReference mItems;
    Button buttonAdd;

    private String username;

    private boolean viewRow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
            System.out.println(user.getDisplayName());
            System.out.println(user.getEmail());
            System.out.println(user.getUid());
            System.out.println("Amikor még jó volt" + user.toString());

        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            //finish();
        }

        System.out.println("User a logolás után: " + user.getDisplayName());
/*        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        if(preferences != null) {
            cartItems = preferences.getInt("cartItems", 0);
            gridNumber = preferences.getInt("gridNum", 1);
        }*/

        // recycle view
        mRecyclerView = findViewById(R.id.recyclerView);
        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, gridNumber));
        // Initialize the ArrayList that will contain the data.
        mItemsData = new ArrayList<>();
        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new ProductAdapter(this, mItemsData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        // Get the data.
        mFirestore = FirebaseFirestore.getInstance();


        mItems = mFirestore.collection("/products");

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogAdd viewDialogAdd = new ViewDialogAdd();
                viewDialogAdd.showDialog(ProductList.this);
            }
        });

        mNotHandler = new NotificationHandler(this);
        System.out.println("User a query előtt: " + user.getDisplayName());
        queryData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    public void queryData() {
        mItemsData.clear();
        mItems.orderBy("ar").limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                if (document.exists()) {
                    System.out.println("Létezik");

                    //Product item = document.toObject(Product.class);
                    String elado = document.getString("felhasznalo");
                    String nev = document.getString("nev");
                    String kategoria = document.getString("kategoria");
                    double arD = document.getDouble("ar");
                    int ar = (int) arD;
                    String id = document.getId();
                    Product item = new Product(elado, nev, kategoria, ar, id);
                    System.out.println("Az id" + item._getId());
                    //String ar = document.getString("Ár");
                    //Product item = new Product(nev,elado,kategoria,ar);
                    item.setId(document.getId());

                    //String uid = user.getUid();
                    //String username = user.getEmail();
                    String emailcim = user.getEmail();
                    username = emailcim.split("@")[0];



                    //String username = user.getDisplayName();
                    System.out.println(username);


                        System.out.println("a felhnev: " + elado);
                        System.out.println("A username : " + username);
                        if(elado.equals(username)) {
                            System.out.println(item.getFelhasznalo());
                            mItemsData.add(item);
                        }

                   // mItems.document(item._getId()).update("nev", "valami1");
                    System.out.println(nev + elado + kategoria + arD);
                } else {
                    System.out.println("Nem létezik");
                }


            }


          /*  if (mItemsData.size() == 0) {
                initializeData();
                queryData();
            } */

            // Notify the adapter of the change.
            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.item3) {
         //   Log.d(LOG_TAG, "Logout clicked!");
           // FirebaseAuth.getInstance().signOut();
            finish();
            return true;
        } else if (itemId == R.id.item1) {
            Log.d(LOG_TAG, "Setting clicked!");
            Intent intent = new Intent(this, ProductList.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.item2) {
            Log.d(LOG_TAG, "Setting clicked!");
            Intent intent = new Intent(this, ProductListAll.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    public class ViewDialogAdd {
        public void showDialog(Context context) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog_add_new_user);

            //EditText textName = dialog.findViewById(R.id.textName);
            EditText textMegnevezes = dialog.findViewById(R.id.textMegnevezes);
            EditText textKategoria = dialog.findViewById(R.id.textKategoria);
            EditText textAr = dialog.findViewById(R.id.textAr);


            Button buttonAdd = dialog.findViewById(R.id.buttonAdd);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            buttonAdd.setText("ADD");
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //String username = user.getDisplayName();
                    String felhasznalonev = username;
                    System.out.println("A null: " + felhasznalonev);
                    //felhasznalonev = "minta";
                    //String id = "user" + new Date().getTime();
                    String Elado = felhasznalonev;
                    String Nev = textMegnevezes.getText().toString();
                    String Kategoria = textKategoria.getText().toString();
                    String sAr = textAr.getText().toString();





                  //  if (Nev.isEmpty() || Kategoria.isEmpty() || sAr.isEmpty()) {
                    //    Toast.makeText(context, "Kérjük töltse ki az összes mezőt", Toast.LENGTH_SHORT).show();
                    //} else {
                        System.out.println("A szo hossza: " + Nev.length());
                        CollectionReference dbProducts = mFirestore.collection("products");
                        if(!(Nev.isEmpty()) && !(Kategoria.isEmpty()) && !(sAr).isEmpty()) {
                            int ar = Integer.parseInt(sAr);
                        Product product = new Product(Elado, Nev, Kategoria, ar, ".");
                        mItems.add(product);
                        queryData();
                        Toast.makeText(context, "Hirdetés sikeresen létrehozva", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    } else {
                            Toast.makeText(context, "Kérjük töltse ki az összes mezőt", Toast.LENGTH_SHORT).show();
                        }
                }
            });


            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, user.getEmail());
        FirebaseAuth.getInstance().signOut();
        Log.d(LOG_TAG, "Logout succesful!");
        Log.d(LOG_TAG, user.getEmail());
    }

}
