package com.example.chhotawhatsapp.UserActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chhotawhatsapp.MessageActivity.MessageActivity;
import com.example.chhotawhatsapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UsersFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference mUsersDatabase;

    private UsersListener listener;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_users, container, false);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        initialise(view);
        buildRecyclerView();

        return view;
    }

    private void initialise(View view){
        mRecyclerView = view.findViewById(R.id.users_recyclerview);
    }

    private void buildRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public class UserAdapter extends FirebaseRecyclerAdapter<UserItem,UsersViewHolder>{

        public UserAdapter(Class<UserItem> modelClass, int modelLayout, Class<UsersViewHolder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }
        @Override
        public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent,false);
            UsersViewHolder usersViewHolder = new UsersViewHolder(v);
            return usersViewHolder;
        }
        @Override
        protected void populateViewHolder(UsersViewHolder usersViewHolder, final UserItem userItem, int i) {
            usersViewHolder.setName(userItem.getName());
            usersViewHolder.setAbout(userItem.getAbout());
            usersViewHolder.setImage(userItem.getImage());
            final String userId = getRef(i).getKey();

            usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(userItem.getName())
                            .setMessage("Want to add this to chat list")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    listener.onUserItemClick(new ExampleItem(userItem.getImage(),userItem.getName(),userItem.getAbout(),userId));
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<UserItem,UsersViewHolder> firebaseRecyclerAdapter = new UserAdapter(UserItem.class,R.layout.example_item,UsersViewHolder.class, mUsersDatabase);
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name){
            TextView userNameView = mView.findViewById(R.id.textView);
            userNameView.setText(name);
        }

        public void setAbout(String name){
            TextView userAboutView = mView.findViewById(R.id.textView2);
            userAboutView.setText(name);
        }

        public void setImage(String image){
            Bitmap bitmap = stringToBitmap(image);
            ImageView userImageView = mView.findViewById(R.id.imageView);
            userImageView.setImageBitmap(bitmap);
        }
    }

    private String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        String imageBitmap = Base64.encodeToString(b,Base64.DEFAULT);

        return imageBitmap;
    }

    private static Bitmap stringToBitmap(String string){
        byte[] encodeByte = Base64.decode(string,Base64.DEFAULT);
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(encodeByte,0,encodeByte.length);
        return imageBitmap;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (UsersListener)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface UsersListener{
        void onUserItemClick(ExampleItem item);
    }
}
