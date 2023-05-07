package com.example.myapplication;

import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.lang.annotation.Annotation;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class ProductListAllAdapter extends RecyclerView.Adapter<ProductListAllAdapter.ViewHolder> {

    private ArrayList<Product> mShopingItemData = new ArrayList<>();
    private ArrayList<Product> mShopingItemDataAll = new ArrayList<>();
    private Context mContext;
    private int lastPosition = -1;
    private NotificationHandler mNotHandler;

    ProductListAllAdapter(Context context, ArrayList<Product> itemsData) {
        this.mShopingItemData = itemsData;
        this.mShopingItemDataAll = itemsData;
        this.mContext = context;
    }

    @Override
    public ProductListAllAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.activity_product_list_all, parent, false));


    }

    @Override
    public void onBindViewHolder(ProductListAllAdapter.ViewHolder holder, int position) {

        Product currentItem = mShopingItemData.get(position);


        holder.textElado.setText("Eladó : " + currentItem.getFelhasznalo());
        holder.textMegnevezes.setText("Megnevezés : " + currentItem.getNev());
        holder.textKategoria.setText("Kategória : " + currentItem.getKategoria());
        holder.textAr.setText("Ár : " + currentItem.getAr());
        // holder.textAr.setText("Ár: " + currentItem.getAr() + " Forint");



        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogConfirmDelete viewDialogConfirmDelete = new ViewDialogConfirmDelete();
                viewDialogConfirmDelete.showDialog(mContext, currentItem._getId());


            }
        });



    }

    @Override
    public int getItemCount() {
        return mShopingItemData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textElado;
        TextView textMegnevezes;
        TextView textKategoria;

        TextView textAr;

        Button buttonDelete;


        public ViewHolder( View itemView) {
            super(itemView);

            textElado = itemView.findViewById(R.id.textElado);
            textMegnevezes = itemView.findViewById(R.id.textMegnevezes);
            textKategoria = itemView.findViewById(R.id.textKategoria);
            textAr = itemView.findViewById(R.id.textAr);

            buttonDelete = itemView.findViewById(R.id.buttonDelete);

        }
    }


    public class ViewDialogConfirmDelete {
        public void showDialog(Context context, String id) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.view_dialog_confirm_purchase);

            Button buttonDelete = dialog.findViewById(R.id.buttonPurchase);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    dialog.dismiss();
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
                    CollectionReference mItems = mFirestore.collection("/products");
                    mItems.document(id).delete();
                   // Toast.makeText(context, "User Deleted successfully!", Toast.LENGTH_SHORT).show();
                    mNotHandler = new NotificationHandler(context);
                    mNotHandler.send("Sikeres vásárlás");
                    dialog.dismiss();
                    context.startActivity(new Intent(context, ProductListAll.class));

                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }







}





