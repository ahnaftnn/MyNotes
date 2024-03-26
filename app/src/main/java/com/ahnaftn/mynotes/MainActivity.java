package com.ahnaftn.mynotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addNote;
    RecyclerView recyclerView;
    ImageButton menu;

    NoteAdapter noteAdapter;
    public static final String shared_pref= "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNote = findViewById(R.id.addNote);
        recyclerView = findViewById(R.id.recycler_view);
        menu = findViewById(R.id.menu);

        addNote.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NotesPage.class)));

        menu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, menu );
            popupMenu.getMenu().add("Sign out");
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getTitle()=="Sign out"){

                        //storing login data
                        SharedPreferences sharedPreferences = getSharedPreferences(shared_pref,MODE_PRIVATE);
                        SharedPreferences.Editor editor= sharedPreferences.edit();

                        editor.putString("name","");
                        editor.apply();


                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    }
                    return false;
                }
            });
        });
       setupRecyclerView();
    }

    void setupRecyclerView() {

        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options, this);
        recyclerView.setAdapter(noteAdapter);

    }

    @Override
    protected void onStart(){
        super.onStart();
       noteAdapter.startListening();
    }
    /*@Override
    protected void onStop(){
        super.onStop();
        noteAdapter.stopListening();

    }
*/




}