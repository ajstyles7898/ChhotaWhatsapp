package com.example.chhotawhatsapp.UserActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chhotawhatsapp.MessageActivity.MessageActivity;
import com.example.chhotawhatsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ChatFragment extends Fragment {
    public static final String ARG_POSITION= "argPosition";
    public static final String SHARED_PREFS= "sharedPrefs";
    public static final String KEY_LIST = "keyList";

    private ArrayList<ExampleItem> mExampleList;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SharedPreferences sharedPreferences;
    private ChatListener listener;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        initialise(view);
        buildRecyclerView();
        return view;
    }

    private void initialise(View view){
        mRecyclerView = view.findViewById(R.id.recyclerView);
    }

    public void insertItem(ExampleItem item){
        if(item.getmUid().equals(mAuth.getCurrentUser().getUid())){
            return;
        }

        for(ExampleItem exampleItem: mExampleList){
            if(exampleItem.getmUid().equals(item.getmUid())){
                return;
            }
        }

        mExampleList.add(new ExampleItem(item.getmImageBitMap(), item.getmText1(), item.getmText2(),item.getmUid()));
        mAdapter.notifyDataSetChanged();
        saveItems();
    }

    private void deleteItem(int position){
        mExampleList.remove(position);
        mAdapter.notifyItemRemoved(position);
        saveItems();
    }

    private void buildRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void removeImage(int position) {
                deleteItem(position);
            }

            @Override
            public void onItemClick(int position) {
                listener.sendToMessageActivity(mExampleList.get(position));
                Intent msgIntent = new Intent(getActivity(),MessageActivity.class);
                startActivity(msgIntent);
            }
        });
    }

    private void saveItems(){
        //sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(mExampleList);

        editor.putString(KEY_LIST+mAuth.getCurrentUser().getUid(),json);
        editor.apply();
    }

    private void loadItems(){
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_LIST+mAuth.getCurrentUser().getUid(),null);

        Type type = new TypeToken<ArrayList<ExampleItem>>() {}.getType();

        mExampleList = gson.fromJson(json,type);

        if(mExampleList ==null){
            mExampleList = new ArrayList<>();
        }
    }

    public ArrayList<ExampleItem> getExampleList(){
        return mExampleList;
    }

    public void callFilter(String text){
        mAdapter.getFilter().filter(text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (ChatListener)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    public interface ChatListener{
        void sendToMessageActivity(ExampleItem item);
    }
}
