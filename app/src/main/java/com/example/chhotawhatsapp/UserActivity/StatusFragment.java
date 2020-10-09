package com.example.chhotawhatsapp.UserActivity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.chhotawhatsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class StatusFragment extends Fragment {

    private DatabaseReference database;
    private ImageView ivProPic;

    public StatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_status, container, false);
        initialise(view);
        addPic();

        return view;
    }

    private void initialise(View view){
        ivProPic = view.findViewById(R.id.status_iv_propic);
    }

    private void addPic(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        database.addListenerForSingleValueEvent(new ValueEventListener() {     //using this we can also update main ui
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,String> hashMap = (HashMap<String,String>)snapshot.getValue();
                Bitmap bitmap = stringToBitmap(hashMap.get("image"));
                ivProPic.setImageBitmap(bitmap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

}
