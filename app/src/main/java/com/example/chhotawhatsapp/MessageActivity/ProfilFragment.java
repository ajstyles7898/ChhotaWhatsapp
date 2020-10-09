package com.example.chhotawhatsapp.MessageActivity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chhotawhatsapp.R;

import java.io.ByteArrayOutputStream;

public class ProfilFragment extends Fragment {
    private static final String ARG_IMG_BITMAP = "imgBitmap";
    private static final String ARG_NAME= "argName";
    private static final String ARG_ABOUT= "argAbout";

    private String imageBitmap;
    private String name;
    private String about;

    private Toolbar toolbar;
    private ImageView ivPic;
    private TextView tvName;
    private TextView tvAbout;

    public ProfilFragment() {
        // Required empty public constructor
    }

    public static ProfilFragment newInstance(String imageBitmap, String name,String about) {

        Bundle args = new Bundle();
        args.putString(ARG_IMG_BITMAP,imageBitmap);
        args.putString(ARG_NAME,name);
        args.putString(ARG_ABOUT,about);
        ProfilFragment fragment = new ProfilFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageBitmap = getArguments().getString(ARG_IMG_BITMAP);
        name = getArguments().getString(ARG_NAME);
        about = getArguments().getString(ARG_ABOUT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        initialise(view);
        addToolbar();
        setViews();

        return view;
    }

    private void initialise(View view){
        toolbar = view.findViewById(R.id.profile_toolbar);
        ivPic = view.findViewById(R.id.profile_iv_pic);
        tvName = view.findViewById(R.id.profile_tv_name);
        tvAbout = view.findViewById(R.id.profile_tv_about);
    }
    private void addToolbar(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile");
    }

    private void setViews(){
        ivPic.setImageBitmap(stringToBitmap(imageBitmap));
        tvName.setText(name);
        tvAbout.setText(about);
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
