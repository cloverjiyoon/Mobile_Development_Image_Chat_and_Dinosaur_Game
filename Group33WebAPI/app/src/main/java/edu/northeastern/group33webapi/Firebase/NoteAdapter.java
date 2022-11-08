package edu.northeastern.group33webapi.Firebase;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {


    public NoteAdapter(List<Note> notes) {
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
