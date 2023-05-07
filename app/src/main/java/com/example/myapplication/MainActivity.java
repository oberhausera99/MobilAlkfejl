package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import com.example.myapplication.SignupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    EditText userNameET;
    EditText passwordET;

    private SharedPreferences preferences;

    private FirebaseAuth fba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameET = findViewById(R.id.EditTextUsername);
        passwordET = findViewById(R.id.EditTextPassword);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        fba = FirebaseAuth.getInstance();

        Log.i(LOG_TAG, "onCreate");
    }

    public void login(View view) {
        String userName = userNameET.getText().toString();
        String password = passwordET.getText().toString();
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.text_slide);
        view.startAnimation(anim);
        if(!(userName.isEmpty()) &&!(password.isEmpty())) {
            Log.i(LOG_TAG, "Bejelentkezett: " + userName + ", jelszó: " + password);
            fba.signInWithEmailAndPassword(userName, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "Sikeres létrehozás");
                        listProduct();
                    } else {
                        Log.d(LOG_TAG, "Sikertelen létrehozás");

                    }
                }
            });
        }
    }

    public void listProduct() {
        Intent intent = new Intent(this, ProductList.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void signup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.text_slide);
        view.startAnimation(anim);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", userNameET.getText().toString());
        editor.putString("password", passwordET.getText().toString());
        editor.apply();

        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        super.onResume();
        Log.i(LOG_TAG, "onRestart");
    }
}