package com.example.chhotawhatsapp.MainAcitivity;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.chhotawhatsapp.MainAcitivity.LoginFragment;
import com.example.chhotawhatsapp.MainAcitivity.RegisterFragment;
import com.example.chhotawhatsapp.R;

public class WelcomeFragment extends Fragment {


    private Button btnMoveToReg;
    private Button btnMoveToLogin;

    private WelcomeListener listener;

    public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        btnMoveToReg = view.findViewById(R.id.wel_btn_move_to_register);
        btnMoveToLogin = view.findViewById(R.id.wel_btn_move_to_login);

        btnMoveToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeFragment(new RegisterFragment());
            }
        });

        btnMoveToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeFragment(new LoginFragment());
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (WelcomeListener)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface WelcomeListener{
        void changeFragment(Fragment fragment);
    }

}
