package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.NotificationManager;
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

public class ProductListAll extends AppCompatActivity {
    private static final String LOG_TAG = ProductListAll.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private FirebaseUser user;

    private NotificationHandler mNotHandler;

    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.android.example.notifyme.ACTION_UPDATE_NOTIFICATION";

    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";



    private FirebaseFirestore mFirestore;

    private FrameLayout redCircle;
    private TextView countTextView;
    private int cartItems = 0;
    private int gridNumber = 1;


    private RecyclerView mRecyclerView;
    private ArrayList<Product> mItemsData;
    private ProductListAllAdapter mAdapter;

    private NotificationManager mNotifyManager;

    private SharedPreferences preferences;
    private CollectionReference mItems;
    Button buttonAdd;

    private boolean viewRow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_all);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }


        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, gridNumber));

        mItemsData = new ArrayList<>();

        mAdapter = new ProductListAllAdapter(this, mItemsData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mNotHandler = new NotificationHandler(this);





        mFirestore = FirebaseFirestore.getInstance();




        mItems = mFirestore.collection("/products");



        queryData();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.item3) {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.item1) {

            Intent intent = new Intent(this, ProductList.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.item2) {

            Intent intent = new Intent(this, ProductListAll.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void queryData() {
        mItemsData.clear();
        mItems.orderBy("ar").limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                if (document.exists()) {

                    //Product item = document.toObject(Product.class);
                    String elado = document.getString("felhasznalo");
                    String nev = document.getString("nev");
                    String kategoria = document.getString("kategoria");
                    double arD = document.getDouble("ar");
                    int ar = (int) arD;
                    String id = document.getId();
                    Product item = new Product(elado, nev, kategoria, ar, id);

                    //String ar = document.getString("Ár");
                    //Product item = new Product(nev,elado,kategoria,ar);
                    item.setId(document.getId());
                    String emailcim = user.getEmail();
                    String username = emailcim.split("@")[0];
                    System.out.println(username);


                    if(!(elado.equals(username))) {
                        System.out.println(item.getFelhasznalo());
                        System.out.println(nev);
                        mItemsData.add(item);
                    }

                    // mItems.document(item._getId()).update("nev", "valami1");
                    System.out.println(nev + elado + kategoria + arD);
                } else {
                    System.out.println("Nem létezik");
                }


            }


            mAdapter.notifyDataSetChanged();
        });
    }





}
