package vn.edu.usth.wordpressclient.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.model.WebCardModel;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.view.UserWebManagement;

public class WebDomainAdapter extends RecyclerView.Adapter<WebDomainAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<WebCardModel> webModels;

    // Adapter constructor
    public WebDomainAdapter(Context context) {
        this.context = context;
        this.webModels = new ArrayList<>();
    }
    public void setWebModels(ArrayList<WebCardModel> webModels) {
        this.webModels = webModels;
    }

    @NonNull
    @Override
    public WebDomainAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each row
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.website_cardview, parent, false);
        return new WebDomainAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WebDomainAdapter.MyViewHolder holder, int position) {
        // Assign values to the views based on the position of the RecyclerView
        WebCardModel currentWebModel = webModels.get(position);
        holder.WebTitle.setText(currentWebModel.getWeb_title());
        String domain = currentWebModel.getWeb_domain();
        if (domain.startsWith("https://")) {
            domain = domain.substring(8);
        } else if (domain.startsWith("http://")) {
            domain = domain.substring(7);
        }
        holder.WebDomain.setText(domain);


//        holder.WebIcon.setImageResource(currentWebModel.getWeb_icon_url());
        Picasso.get()
                .load(currentWebModel.getWeb_icon_url())
                .placeholder(R.drawable.compass)
                .error(R.drawable.compass)
                .into(holder.WebIcon);
    }

    @Override
    public int getItemCount() {
        // Return the size of the data list
        return webModels.size();
    }

    // ViewHolder class
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView WebIcon;
        TextView WebTitle, WebDomain;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Grabbing views
            WebIcon = itemView.findViewById(R.id.web_icon);
            WebTitle = itemView.findViewById(R.id.web_title);
            WebDomain = itemView.findViewById(R.id.web_domain);

            // Handle item click event
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Get the clicked WebCardModel
                    WebCardModel clickedWebsite = webModels.get(position);

                    Intent intent = new Intent(context, UserWebManagement.class);
                    String tempDomain = clickedWebsite.getWeb_domain();
                    String domain = tempDomain;
                    if (domain.startsWith("https://")) {
                        domain = domain.substring(8);
                    } else if (domain.startsWith("http://")) {
                        domain = domain.substring(7);
                    }

                    intent.putExtra("title", clickedWebsite.getWeb_title());
                    intent.putExtra("imgUrl",clickedWebsite.getWeb_icon_url());
                    DomainManager.getInstance().setSelectedDomain(domain);
                    context.startActivity(intent);

                }
            });
        }
    }
}
