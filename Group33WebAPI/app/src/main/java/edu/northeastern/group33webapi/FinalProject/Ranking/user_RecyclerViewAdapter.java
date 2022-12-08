package edu.northeastern.group33webapi.FinalProject.Ranking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;

import edu.northeastern.group33webapi.FinalProject.login.gameUser;
import edu.northeastern.group33webapi.R;

public class user_RecyclerViewAdapter extends RecyclerView.Adapter<user_RecyclerViewAdapter.MyViewHolder> {
    OnSuccessListener<DataSnapshot> context;
    ArrayList<gameUser> userArrayList;
    private DatabaseReference myDataBase;
    private String userId;
    private HashMap users;
//    private ArrayList<gameUser> userArrayList = new ArrayList<>();

    public user_RecyclerViewAdapter(OnSuccessListener<DataSnapshot> context, ArrayList<gameUser> userArrayList) {
        this.context = (OnSuccessListener<DataSnapshot>) context;
        this.userArrayList = userArrayList;

    }



    @NonNull
    @Override
    public user_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from( parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new user_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull user_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.username.setText(userArrayList.get(position).getUsername());
        holder.score.setText(userArrayList.get(position).getScore());
        holder.imageView.setImageResource(R.drawable.avator);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView username, score;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            username = itemView.findViewById(R.id.username);
            score = itemView.findViewById(R.id.score);

        }
    }
}
