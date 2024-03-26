package com.ahnaftn.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NotesPage extends AppCompatActivity {

    EditText titleEdit,notesEdit;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    String title,content,docId;
    boolean isEditMode= false;
    ImageButton delNoteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_page);

        titleEdit= findViewById(R.id.notes_title);
        notesEdit= findViewById(R.id.notes);
        saveNoteBtn= findViewById(R.id.save_note);
        pageTitleTextView = findViewById(R.id.page_title);
        delNoteBtn = findViewById(R.id.delete_note);

        title= getIntent().getStringExtra("title");
        content= getIntent().getStringExtra("content");
        docId= getIntent().getStringExtra("docId");

        if(docId!= null && !docId.isEmpty() ){
            isEditMode= true;
        }

        titleEdit.setText(title);
        notesEdit.setText(content);
        if(isEditMode)
        {
            pageTitleTextView.setText("");
            delNoteBtn.setVisibility(View.VISIBLE);
        }



        saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteTitle = titleEdit.getText().toString();
                String notes= notesEdit.getText().toString();

                if(noteTitle==null || noteTitle.isEmpty()){
                    titleEdit.setError("Title is required");
                    return;
                }
                Note note = new Note();
                note.setTitle(noteTitle);
                note.setContent(notes);
                //note.setTimestamp(Timestamp.now());

                saveNoteToFirebase(note);

            }
        });

        delNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference;

                documentReference= Utility.getCollectionReferenceForNotes().document(docId);

                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Utility.showToast(NotesPage.this, "Note Deleted Successfully");
                            finish();
                        } else{
                            Utility.showToast(NotesPage.this, "Something went wrong!");
                        }
                    }
                });

            }
        });

    }
    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;

        if(isEditMode){
            documentReference= Utility.getCollectionReferenceForNotes().document(docId);
        } else {
            documentReference= Utility.getCollectionReferenceForNotes().document();
        }


        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utility.showToast(NotesPage.this, "Note Added Successfully");
                    finish();
                } else{
                    Utility.showToast(NotesPage.this, "Something went wrong");
                }
            }
        });
    }
}