package vn.edu.usth.wordpressclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Web_domain_adapter extends RecyclerView.Adapter<Web_domain_adapter.MyViewHolder> {
    private Context context;
    private ArrayList<Web_card_model> webModels;

    // Adapter constructor
    public Web_domain_adapter(Context context, ArrayList<Web_card_model> webModels) {
        this.context = context;
        this.webModels = webModels;
    }

    @NonNull
    @Override
    public Web_domain_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each row
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.website_cardview, parent, false);
        return new Web_domain_adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Web_domain_adapter.MyViewHolder holder, int position) {
        // Assign values to the views based on the position of the RecyclerView
        Web_card_model currentWebModel = webModels.get(position);
        holder.WebTitle.setText(currentWebModel.getWeb_title());
        holder.WebDomain.setText(currentWebModel.getWeb_domain());
        holder.WebIcon.setImageResource(currentWebModel.getWeb_icon());
    }

    @Override
    public int getItemCount() {
        // Return the size of the data list
        return webModels.size();
    }

    // ViewHolder class
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView WebIcon;
        TextView WebTitle, WebDomain;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Grabbing views from the item layout (R.layout.website_cardview)
            WebIcon = itemView.findViewById(R.id.web_icon);
            WebTitle = itemView.findViewById(R.id.web_title);
            WebDomain = itemView.findViewById(R.id.web_domain);

            // Handle item click event
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Get the clicked Web_card_model
                        Web_card_model clickedWebsite = webModels.get(position);

                        // Show a toast with the web title
                        Toast.makeText(v.getContext(), "Opening " + clickedWebsite.getWeb_title(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
