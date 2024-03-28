/* package com.example.hanseohelper_muso;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.View;

import java.util.ArrayList;

public class ChattingActivity extends AppCompatActivity {

    EditText et;
    ListView chatView;
    int i;
    boolean isChatRoom;

    ArrayList<ChattingInfo> chattingInfoArrayList = new ArrayList<>();
    ChattingAdapter adapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        chattingInfoArrayList = new ArrayList<ChattingInfo>();

        chatView = (ListView) findViewById(R.id.chatting_view);
        adapter = new ChattingAdapter(chattingInfoArrayList, getLayoutInflater());
        chatView.setAdapter(adapter);

        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
        String fileName = userEmail.replaceAll(match, "") + "_" + friendsEmail.replaceAll(match, "");
        String fileName2 = friendsEmail.replaceAll(match, "") + "_" + userEmail.replaceAll(match, "");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Chatting");

        i = 0;

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    num++;
                }
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        ++i;

                        if(dataSnapshot.getKey().equals(fileName)){
                            Log.i("Chatting Room Name", dataSnapshot.getKey());

                            isChatRoom = true;

                            chatList(databaseReference.child(fileName));

                            btn_send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    sendMsg(databaseReference.child(fileName));
                                }
                            });
                        }

                        else if(dataSnapshot.getKey().equals(fileName2)){
                            Log.i("Chatting Room Name", dataSnapshot.getKey());

                            isChatRoom = true;

                            chatList(databaseReference.child(fileName2));


                        }
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
                })
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
*/