package com.example.chhotawhatsapp.MessageActivity;


import android.content.Context;
import android.content.res.Configuration;
import android.icu.text.Edits;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chhotawhatsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MessageFragment extends Fragment {
    private static final String ARG_NAME = "argName";
    private static final String ARG_PERSON_ID= "argPersonId";

    private String name;
    private String otherPersonId;

    private Toolbar toolbar;

    private EditText etMessage;
    private ImageView ivSend;

    private ArrayList<ExampleMessageItem> mExampleMessageList;
    private RecyclerView mRecyclerView;
    private ExampleMessageAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private FirebaseAuth mAuth;
    private DatabaseReference database;

    private MessageListener litener;

    public MessageFragment() {
        // Required empty public constructor
    }

    public static MessageFragment newInstance(String name,String otherPersonId) {
        Bundle args = new Bundle();

        args.putString(ARG_NAME,name);
        args.putString(ARG_PERSON_ID,otherPersonId);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments().getString(ARG_NAME);
        otherPersonId = getArguments().getString(ARG_PERSON_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_message, container, false);

        initialise(view);

        /*Configuration configuration = getActivity().getResources().getConfiguration();
        int screenWidth = configuration.screenWidthDp;
        etMessage.setHint(""+screenWidth);*/ //To get width of the screen

        setHasOptionsMenu(true);
        addToolbar();

        mAuth = FirebaseAuth.getInstance();
        createExampleList();
        buildRecyclerView();
        setButtons();

        return view;
    }

    private void initialise(View view){
        toolbar = view.findViewById(R.id.msgf_toolbar);
        mRecyclerView = view.findViewById(R.id.msg_recyclerview);
        etMessage = view.findViewById(R.id.msg_et_enter_message);
        ivSend = view.findViewById(R.id.msg_iv_send);
    }

    private void addToolbar(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(name);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.message_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.msg_btn_profile){
            litener.openProfileFragment();
        }
        return super.onOptionsItemSelected(item);
    }

    public void createExampleList() {
        mExampleMessageList = new ArrayList<>();
    }

    private void buildRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setStackFromEnd(true);
        //mLayoutManager.setReverseLayout(true);
        mAdapter = new ExampleMessageAdapter(mExampleMessageList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExampleMessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {}
        });

        loadMessageItems();
    }

    private void setButtons(){
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString();
                addToDatabase(message);
                etMessage.setText("");
            }
        });
    }

    private void addToDatabase(String message){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        LocalTime now = LocalTime.now();

        HashMap<String,String> messageMap = new HashMap<>();
        messageMap.put("sender",""+uid);
        messageMap.put("time",now.format(DateTimeFormatter.ofPattern("HH:mm"))); //H for 24 hour system and h for 12 hour
        messageMap.put("text",message);

        database = FirebaseDatabase.getInstance().getReference().child("chat").child(uid).child(otherPersonId).push();
        String pushId = database.getKey();
        database.setValue(messageMap);

        database = FirebaseDatabase.getInstance().getReference().child("chat").child(otherPersonId).child(uid).child(pushId);
        database.setValue(messageMap);
    }

    private void loadMessageItems(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        database = FirebaseDatabase.getInstance().getReference().child("chat").child(uid).child(otherPersonId);
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { //adds all childs one by one when loadmessageitems is called first time.
                HashMap<String,String> messageMap = (HashMap<String,String>)snapshot.getValue();
                mExampleMessageList.add(new ExampleMessageItem(messageMap.get("text"),messageMap.get("time"),messageMap.get("sender")));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        litener = (MessageListener)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        litener = null;
    }

    public interface MessageListener{
        void openProfileFragment();
    }
}
