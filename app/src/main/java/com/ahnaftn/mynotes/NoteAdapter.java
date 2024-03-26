package com.ahnaftn.mynotes;

import static android.view.View.*;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note,NoteAdapter.NoteViewHolder> {


    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.title.setText(note.getTitle());
        holder.content.setText(note.getContent());
        //holder.timestamp.setText(Utility.timestampToString(note.getTimestamp()));
        String docId= this.getSnapshots().getSnapshot(position).getId();
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotesPage.class);
            intent.putExtra("title", note.getTitle());
            intent.putExtra("content", note.getContent());

            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent, false);
        return new NoteViewHolder(view);
    }

      static class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView title,content,timestamp;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.note_title);
            content= itemView.findViewById(R.id.note_content);
            //timestamp= itemView.findViewById(R.id.note_time);
        }
    }
}
