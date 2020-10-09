package com.example.chhotawhatsapp.UserActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.chhotawhatsapp.MainAcitivity.MainActivity;
import com.example.chhotawhatsapp.MessageActivity.MessageActivity;
import com.example.chhotawhatsapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.chhotawhatsapp.UserActivity.ChatFragment.ARG_POSITION;

public class UserActivity extends AppCompatActivity  implements ChatFragment.ChatListener, UsersFragment.UsersListener {
    private static final String ARG_EXAMPLE_LIST = "argExampleList";

    private FirebaseAuth mAuth;

    private Toolbar toolbar;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private SectionPagerAdapter sectionPagerAdapter;

    private static ExampleItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chhota WhatsApp");

        viewPager = findViewById(R.id.user_view_pager);
        sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionPagerAdapter);

        tabLayout = findViewById(R.id.user_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.user_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView)searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sectionPagerAdapter.chatFragment.callFilter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.main_btn_logout){
            FirebaseAuth.getInstance().signOut();
            sendToMain();
        }

        if(item.getItemId()==R.id.main_btn_account_setting){
            sendToSettings();
        }

        return true;
    }

    private void sendToMain(){
        Intent mainIntent = new Intent(UserActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void sendToSettings(){
        Intent settingsIntent = new Intent(UserActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    @Override
    public void sendToMessageActivity(ExampleItem item) {
        this.item = item;
    }

    public static ExampleItem getItem(){
        return item;
    }

    @Override
    public void onUserItemClick(ExampleItem item) {
        sectionPagerAdapter.chatFragment.insertItem(item);
    }
}
