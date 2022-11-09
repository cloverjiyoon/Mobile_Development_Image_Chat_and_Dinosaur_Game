package edu.northeastern.group33webapi.Firebase;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.group33webapi.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    private final ImageView receivedNoteIV;
    private final TextView fromUserTV;
    private final TextView sendTimeTV;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        this.receivedNoteIV = itemView.findViewById(R.id.receivedStickerIV);
        this.fromUserTV = itemView.findViewById(R.id.fromUserTV);
        this.sendTimeTV = itemView.findViewById(R.id.sendTimeTV);
    }

    public void bindThisData(Note NoteToBind) {
        int imageResource = getImageResourceById(NoteToBind.getId());
        if (imageResource != -1) {
            receivedNoteIV.setImageResource(imageResource);
        }
        fromUserTV.setText(NoteToBind.getFromUser());
        sendTimeTV.setText(NoteToBind.getSendTime());
    }

    private int getImageResourceById(int id) {
        if (id == 1) {
            return R.drawable.sun;
        } else if (id == 2) {
            return R.drawable.rain;
        } else if (id == 3) {
            return R.drawable.rainstorm;
        } else if (id == 4) {
            return R.drawable.snow;
        } else if (id == 5) {
            return R.drawable.wind;
        } else if (id == 6) {
            return R.drawable.cloud;
        }
        return -1;
    }
}

