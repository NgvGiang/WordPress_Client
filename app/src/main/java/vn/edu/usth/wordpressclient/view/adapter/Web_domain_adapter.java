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
import vn.edu.usth.wordpressclient.model.Web_card_model;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.view.UserWebManagement;

public class Web_domain_adapter extends RecyclerView.Adapter<Web_domain_adapter.MyViewHolder> {
    private Context context;
    private ArrayList<Web_card_model> webModels;

    // Adapter constructor
    public Web_domain_adapter(Context context) {
        this.context = context;
        this.webModels = new ArrayList<>();
    }
    public void setWebModels(ArrayList<Web_card_model> webModels) {
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
//        holder.WebIcon.setImageResource(currentWebModel.getWeb_icon_url());
        Picasso.get()
                .load(currentWebModel.getWeb_icon_url())   // URL của ảnh
                .placeholder(R.drawable.compass)  // Hình ảnh hiển thị khi đang tải
                .error(R.drawable.compass)              // Hình ảnh hiển thị nếu có lỗi
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Get the clicked Web_card_model
                        Web_card_model clickedWebsite = webModels.get(position);

                        Intent intent = new Intent(context, UserWebManagement.class);
                        String tempDomain = clickedWebsite.getWeb_domain();
                        String domain = tempDomain.replace("https://", "");
//                        intent.putExtra("domain", domain);
                        intent.putExtra("title", clickedWebsite.getWeb_title());
                        intent.putExtra("imgUrl",clickedWebsite.getWeb_icon_url());
                        DomainManager.getInstance().setSelectedDomain(domain);
                        context.startActivity(intent);

                    }
                }
            });
        }
    }
}
