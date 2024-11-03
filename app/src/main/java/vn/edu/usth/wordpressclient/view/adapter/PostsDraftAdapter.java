package vn.edu.usth.wordpressclient.view.adapter;

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

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.model.ContentCardModel;

public class PostsDraftAdapter extends RecyclerView.Adapter<PostsDraftAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ContentCardModel> postList;
    private OnMenuClickListener popupClickListener;

    // Adapter constructor
    public PostsDraftAdapter(Context context) {
        this.context = context;
        this.postList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PostsDraftAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each row
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.page_post_cardview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ContentCardModel currentPost = postList.get(position);

        holder.Date.setText(currentPost.getDate());
        holder.Title.setText(currentPost.getTitle());
        holder.Content.setText(currentPost.getContent());
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
            popupView.findViewById(R.id.draft_view_item).setOnClickListener(view -> {
                Toast.makeText(context, "Viewed", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.draft_publish_item).setOnClickListener(view -> {
                Toast.makeText(context, "Published", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.draft_duplicate_item).setOnClickListener(view -> {
                Toast.makeText(context, "Duplicated", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.draft_share_item).setOnClickListener(view -> {
                Toast.makeText(context, "Shared", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.draft_trash_item).setOnClickListener(view -> {
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

    public void setDraftPost(ArrayList<ContentCardModel> draftPost){
        this.postList = draftPost;
    }

}