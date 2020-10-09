package com.example.chhotawhatsapp.MainAcitivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.chhotawhatsapp.R;
import com.example.chhotawhatsapp.UserActivity.UserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;


public class RegisterFragment extends Fragment {

    private TextInputLayout tilDisplayName;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private Button btnCreate;


    private Toolbar toolbar;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference database;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_register, container, false);
        initialise(view);
        addToolbar();

        FirebaseApp.initializeApp(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        setButtons();

        return view;
    }

    private void addToolbar(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Create Account");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initialise(View view){
        toolbar = view.findViewById(R.id.reg_toolbar);

        tilDisplayName = view.findViewById(R.id.reg_til_name);
        tilEmail = view.findViewById(R.id.reg_til_email);
        tilPassword = view.findViewById(R.id.reg_til_password);
        btnCreate = view.findViewById(R.id.reg_btn_create);
    }

    private void setButtons(){
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displayName = tilDisplayName.getEditText().getText().toString();
                String email = tilEmail.getEditText().getText().toString();
                String password = tilPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(displayName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    progressDialog.setTitle("Registering User");
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    registerUser(displayName,email,password);
                }
            }


        });
    }

    private void registerUser(final String displayName, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    final String uid = currentUser.getUid();

                    addBasicInfo(uid,displayName);

                    updateUserIDs(uid);


                }else{
                    progressDialog.hide();
                    Toast.makeText(getActivity(),"Some Error",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addBasicInfo(String uid,String displayName){
        database = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        String defaultImage = bitmapToString(BitmapFactory.decodeResource(this.getResources(),R.drawable.duniya));

        HashMap<String,String> userMap = new HashMap<>();
        userMap.put("name",displayName);
        userMap.put("about","Add about");
        userMap.put("image",defaultImage);

        database.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();

                    Intent mainIntent = new Intent(getActivity(), UserActivity.class);
                    startActivity(mainIntent);
                }else{
                    progressDialog.hide();
                    Toast.makeText(getActivity(),"Some Error for database",Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private void updateUserIDs(final String uid){
        database = FirebaseDatabase.getInstance().getReference().child("userIDs");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,String> ids= (HashMap<String,String>)snapshot.getValue();

                long totalUsers = Integer.valueOf(ids.get("TotalUsers"));
                String newTotalUsers = Long.toString(totalUsers+1);
                ids.replace("TotalUsers",newTotalUsers);
                ids.put(newTotalUsers,uid);

                database.setValue(ids);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(ids!=null){
                        database.setValue(ids);
                        break;
                    }
                }
            }
        }).start();
        //when things cant be updated in previous thread
         */

    }

}
