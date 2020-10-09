package com.example.chhotawhatsapp.MainAcitivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private Toolbar toolbar;

    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private Button btnLogin;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        initialise(view);
        addToolbar();
        progressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        setButtons();

        return view;
    }

    private void addToolbar(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Login");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initialise(View view){
        toolbar = view.findViewById(R.id.login_toolbar);
        tilEmail = view.findViewById(R.id.login_til_email);
        tilPassword = view.findViewById(R.id.login_til_password);
        btnLogin = view.findViewById(R.id.login_btn_login);
    }

    private void setButtons(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tilEmail.getEditText().getText().toString();
                String password = tilPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    progressDialog.setTitle("loginistering User");
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    loginUser(email,password);
                }
            }
        });
    }

    private void loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();

                    Intent mainIntent = new Intent(getActivity(), UserActivity.class);
                    startActivity(mainIntent);
                }else{
                    progressDialog.hide();
                    Toast.makeText(getActivity(),"Wrong credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
