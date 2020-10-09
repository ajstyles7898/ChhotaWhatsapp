package com.example.chhotawhatsapp.MainAcitivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.chhotawhatsapp.R;
import com.example.chhotawhatsapp.UserActivity.UserActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements WelcomeFragment.WelcomeListener {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        setFragment(new WelcomeFragment());

    }

    private void setFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,fragment).commit();
    }

    @Override
    public void changeFragment(Fragment fragment) {
        setFragment(fragment);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();

        if(currentUser != null){
            sendToUser();
        }
    }

    private void sendToUser(){
        Intent userIntent = new Intent(MainActivity.this, UserActivity.class);
        startActivity(userIntent);
        finish();
    }
}
