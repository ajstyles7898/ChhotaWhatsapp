package com.example.chhotawhatsapp.UserActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chhotawhatsapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import java.io.ByteArrayOutputStream;

public class UserAdapter extends FirebaseRecyclerAdapter<UserItem, UserAdapter.UsersViewHolder> {

    private UsersListener listener;

    public UserAdapter(Class<UserItem> modelClass, int modelLayout, Query ref) {
        super(modelClass, modelLayout, UsersViewHolder.class, ref);
    }
    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent,false);
        UsersViewHolder usersViewHolder = new UsersViewHolder(v);
        return usersViewHolder;
    }
    @Override
    protected void populateViewHolder(UsersViewHolder usersViewHolder, final UserItem userItem, int i) {
        Log.d("TAG","users@: "+userItem.getImage());
        usersViewHolder.setName(userItem.getName());
        usersViewHolder.setAbout(userItem.getAbout());
        usersViewHolder.setImage(userItem.getImage());
        final String userId = getRef(i).getKey();

        usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserItemClick(new ExampleItem(userItem.getImage(),userItem.getName(),userItem.getAbout(),userId));
            }
        });
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

    public void setUserItemClickListener(UsersListener listener){
        this.listener = listener;
    }
    public interface UsersListener{
        void onUserItemClick(ExampleItem item);
    }
}
