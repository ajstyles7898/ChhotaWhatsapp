package com.example.chhotawhatsapp.UserActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chhotawhatsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    public static final int PICK_IMAGE=1;

    private Toolbar toolbar;

    private ImageView ivPicEdit;
    private ImageView ivNameEdit;
    private ImageView ivAboutEdit;
    private TextView tvName;
    private TextView tvAbout;
    private EditText etName;
    private EditText etAbout;
    private Button btnSaveChanges;

    private String imageBitmap;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        database = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        initialise();
        addToolbar();
        setViews();
        setButtons();
    }

    private void initialise() {
        toolbar = findViewById(R.id.settings_toolbar);
        ivPicEdit = findViewById(R.id.settings_iv_propic);
        ivNameEdit = findViewById(R.id.settings_iv_name_edit);
        ivAboutEdit = findViewById(R.id.settings_iv_about_edit);
        tvName = findViewById(R.id.settings_tv_display_name);
        tvAbout = findViewById(R.id.settings_tv_about);
        etName = findViewById(R.id.settings_et_display_name);
        etAbout = findViewById(R.id.settings_et_about);
        btnSaveChanges = findViewById(R.id.settings_btn_save_changes);
    }

    private void addToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
    }

    private void setViews(){
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,String> userMap = (HashMap<String,String>)snapshot.getValue();
                tvName.setText(userMap.get("name"));
                tvAbout.setText(userMap.get("about"));
                ivPicEdit.setImageBitmap(stringToBitmap(userMap.get("image")));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setButtons(){

        ivPicEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this,"change pic", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Pic"),PICK_IMAGE);
            }
        });

        ivNameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvName.setVisibility(View.INVISIBLE);
                etName.setVisibility(View.VISIBLE);
                etName.setText(tvName.getText().toString());
                etName.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etName,InputMethodManager.SHOW_IMPLICIT);
            }
        });

        ivAboutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAbout.setVisibility(View.INVISIBLE);
                etAbout.setVisibility(View.VISIBLE);
                etAbout.setText(tvAbout.getText().toString());
                etAbout.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etAbout,InputMethodManager.SHOW_IMPLICIT);
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= etName.getText().toString();
                String about= etAbout.getText().toString();

                if(!name.equals("")){
                    tvName.setText(name);
                }

                if(!about.equals("")){
                    tvAbout.setText(about);
                }
                tvName.setVisibility(View.VISIBLE);
                tvAbout.setVisibility(View.VISIBLE);
                etName.setVisibility(View.INVISIBLE);
                etAbout.setVisibility(View.INVISIBLE);

                updateDatabase(name,about,imageBitmap);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE){
            if(resultCode==RESULT_OK){
                Uri imageUri = data.getData();
                Bitmap bitmap = null;
                try { bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri); }
                catch (IOException e) { e.printStackTrace(); }

                ivPicEdit.setImageBitmap(bitmap);
                imageBitmap = bitmapToString(bitmap);
            }
        }
    }

    private String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        String imageBitmap = Base64.encodeToString(b,Base64.DEFAULT);

        return imageBitmap;
    }

    private Bitmap stringToBitmap(String string){
        byte[] encodeByte = Base64.decode(string,Base64.DEFAULT);
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(encodeByte,0,encodeByte.length);
        return imageBitmap;
    }

    private void updateDatabase(final String name, final String about, final String imageBitmapString){
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,String> userMap = (HashMap<String,String>)snapshot.getValue();
                if(!name.equals("")) {
                    userMap.replace("name", name);
                }
                if(!about.equals("")) {
                    userMap.replace("about", about);
                }

                if(imageBitmapString!=null) {
                    userMap.replace("image", imageBitmapString);
                }
                database.setValue(userMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
