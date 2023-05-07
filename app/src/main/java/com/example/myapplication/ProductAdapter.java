package com.example.myapplication;

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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<Product> mShopingItemData = new ArrayList<>();
    private ArrayList<Product> mShopingItemDataAll = new ArrayList<>();
    private Context mContext;
    private int lastPosition = -1;

    ProductAdapter(Context context, ArrayList<Product> itemsData) {
        this.mShopingItemData = itemsData;
        this.mShopingItemDataAll = itemsData;
        this.mContext = context;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.activity_product_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {

        Product currentItem = mShopingItemData.get(position);


        holder.textElado.setText("Eladó : " + currentItem.getFelhasznalo());
        holder.textMegnevezes.setText("Megnevezés : " + currentItem.getNev());
        holder.textKategoria.setText("Kategória : " + currentItem.getKategoria());
        holder.textAr.setText("Ár : " + currentItem.getAr());





        holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogUpdate viewDialogUpdate = new ViewDialogUpdate();
                viewDialogUpdate.showDialog(mContext, currentItem._getId(), currentItem.getNev(), currentItem.getKategoria(), String.valueOf(currentItem.getAr()));

            }
        });

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
        Button buttonUpdate;

        public ViewHolder( View itemView) {
            super(itemView);

            textElado = itemView.findViewById(R.id.textElado);
            textMegnevezes = itemView.findViewById(R.id.textMegnevezes);
            textKategoria = itemView.findViewById(R.id.textKategoria);
            textAr = itemView.findViewById(R.id.textAr);

            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
        }
    }

    public class ViewDialogUpdate {
        public void showDialog(Context context, String id, String nev, String kategoria, String ar) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog_add_new_user);

            //EditText textName = dialog.findViewById(R.id.textElado);
            EditText textMegnevezes = dialog.findViewById(R.id.textMegnevezes);
            EditText textKategoria = dialog.findViewById(R.id.textKategoria);
            EditText textAr = dialog.findViewById(R.id.textAr);

            //textName.setText(name);
            textMegnevezes.setText(nev);
            textKategoria.setText(kategoria);
            textAr.setText(ar);


            Button buttonUpdate = dialog.findViewById(R.id.buttonAdd);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            buttonUpdate.setText("Módosít");

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonUpdate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    String newMegnevezes = textMegnevezes.getText().toString();
                    String newKategoria = textKategoria.getText().toString();
                    String newAr = textAr.getText().toString();
                    int dNewAr = Integer.parseInt(newAr);


                    if (nev.isEmpty() || kategoria.isEmpty()) {
                        Toast.makeText(context, "Please Enter All data...", Toast.LENGTH_SHORT).show();
                    } else {

                        if (newMegnevezes.equals(nev) && newKategoria.equals(kategoria) && newAr.equals(ar)) {
                            Toast.makeText(context, "Nem változtak az adatok", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(context, "A hirdetés sikeresen frissítve!", Toast.LENGTH_SHORT).show();
                            FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
                            CollectionReference mItems = mFirestore.collection("/products");
                            mItems.document(id).update("nev", newMegnevezes);
                            mItems.document(id).update("kategoria", newKategoria);
                            mItems.document(id).update("ar", dNewAr);




                            dialog.dismiss();
                            context.startActivity(new Intent(context, ProductList.class));



                        }


                    }
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }



    }





    public class ViewDialogConfirmDelete {
        public void showDialog(Context context, String id) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.view_dialog_confirm_delete);

            Button buttonDelete = dialog.findViewById(R.id.buttonDelete);
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
                    Toast.makeText(context, "Hirdetés törölve!", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, ProductList.class));
                    dialog.dismiss();

                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }







}
