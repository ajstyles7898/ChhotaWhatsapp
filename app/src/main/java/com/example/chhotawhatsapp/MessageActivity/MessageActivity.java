package com.example.chhotawhatsapp.MessageActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.example.chhotawhatsapp.R;
import com.example.chhotawhatsapp.UserActivity.ChatFragment;
import com.example.chhotawhatsapp.UserActivity.ExampleItem;
import com.example.chhotawhatsapp.UserActivity.UserActivity;

public class MessageActivity extends AppCompatActivity implements MessageFragment.MessageListener {

    private String name;
    private String about;
    private String imgBitmapString;
    private String otherPersonId;
    private ExampleItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        item = UserActivity.getItem();
        name = item.getmText1();
        about = item.getmText2();

        imgBitmapString = item.getmImageBitMap();
        otherPersonId = item.getmUid();

        MessageFragment messageFragment = MessageFragment.newInstance(name,otherPersonId);
        setFragment(messageFragment);
    }

    private void setFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.msg_fragment_container,fragment).commit();
    }

    @Override
    public void openProfileFragment() {
        setFragment(ProfilFragment.newInstance(imgBitmapString,name,about));
    }
}
