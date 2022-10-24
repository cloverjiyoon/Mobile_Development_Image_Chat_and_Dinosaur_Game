package edu.northeastern.group33webapi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.ViewHolder>{


    private List<Cloud> postsList;

    public LinkAdapter(List<Cloud> postsList){
        this.postsList = postsList;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal = Calendar.getInstance();

    @NonNull
    @Override
    public LinkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_link, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LinkAdapter.ViewHolder holder, int position) {
        cal.add(Calendar.DAY_OF_MONTH, position);
        String dateAfter = sdf.format(cal.getTime());
        holder.cloud.setText("Forecast for date of  " +dateAfter + "   :   " + postsList.get(position).getCloudlevel());
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cloud;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cloud = itemView.findViewById(R.id.cloud);
        }
    }
}

//public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.ViewHolder> {
//
//    private final List<Link> links;
//
//    /**
//     * Creates a PersonAdapter with the provided arraylist of Person objects.
//     *
//     * @param links    arraylist of url link list Name object.
//     * @param context   context of the activity used for inflating layout of the viewholder.
//     */
//    public LinkAdapter(List<Link> links, Context context) {
//        this.links = links;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        // Create an instance of the viewholder by passing it the layout inflated as view and no root.
//        return new LinkViewHolder(LayoutInflater.from(context).inflate(R.layout.item_link, null));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull LinkAdapter.ViewHolder holder, int position) {
//        // sets the name of the website to the name textview of the viewholder.
//        holder.temp.setText(links.get(position).getTemp());
//        // sets the url of the website to the age textview of the viewholder.
//        holder.humidity.setText(links.get(position).getHumidity());
//        holder.pressure.setText(links.get(position).getPressure());
//        holder.temp_min.setText(links.get(position).getTemp_min());
//        holder.temp_max.setText(links.get(position).getTemp_max());
//    }
//
//    @Override
//    public int getItemCount() {
//        // Returns the size of the recyclerview that is the list of the arraylist.
//        return links.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TexView temp;
//
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }
//}
