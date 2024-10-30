package vn.edu.usth.wordpressclient;

import android.content.Context;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Post_page_adapter extends RecyclerView.Adapter<Post_page_adapter.MyViewHolder> {
    private Context context;
    private ArrayList<Post_page_card_model> postList;
    private OnMenuClickListener popupClickListener;

    // Adapter constructor
    public Post_page_adapter(Context context, ArrayList<Post_page_card_model> postList) {
        this.context = context;
        this.postList = postList;
        this.popupClickListener = popupClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each row
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.content_cardview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Post_page_card_model currentPost = postList.get(position);

        holder.Date.setText(currentPost.getPost_date());
        holder.Title.setText(currentPost.getPost_title());
        holder.Content.setText(currentPost.getPost_content());
        holder.Setting.setOnClickListener(v -> {
            // Inflate the custom popup layout
            View popupView = LayoutInflater.from(context).inflate(R.layout.post_draft_popupmenu, null);

            // Create the PopupWindow with desired width and height
            final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            // Set a background for the popup to enable outside touch dismissal
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);

            // Show the popup directly below the button that was clicked
            popupWindow.showAsDropDown(v, 0, 0);

            // Set click listeners for each menu item
            popupView.findViewById(R.id.view_item).setOnClickListener(view -> {
                Toast.makeText(context, "Viewed", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.publish_item).setOnClickListener(view -> {
                Toast.makeText(context, "Published", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.duplicate_item).setOnClickListener(view -> {
                Toast.makeText(context, "Duplicated", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.share_item).setOnClickListener(view -> {
                Toast.makeText(context, "Shared", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.trash_item).setOnClickListener(view -> {
                Toast.makeText(context, "Trashed", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });
        });
    }

    public interface OnMenuClickListener {
        void onMenuClick(View anchorView, int position);
    }

    @Override
    public int getItemCount() {
        // Return the size of the data list
        return postList.size();
    }

    // ViewHolder class
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Date, Title, Content;
        ImageView Setting;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Grabbing views
            Date = itemView.findViewById(R.id.item_date);
            Title = itemView.findViewById(R.id.item_title);
            Content = itemView.findViewById(R.id.item_content);
            Setting = itemView.findViewById(R.id.content_setting_btn);
        }
    }
}