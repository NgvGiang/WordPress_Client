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

public class PagesScheduledAdapter extends RecyclerView.Adapter<PagesScheduledAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ContentCardModel> postList;

    public PagesScheduledAdapter(Context context) {
        this.context = context;
        this.postList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PagesScheduledAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each row
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pages_cardview, parent, false);
        return new PagesScheduledAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PagesScheduledAdapter.MyViewHolder holder, int position) {
        ContentCardModel currentPost = postList.get(position);

        holder.Date.setText(currentPost.getDate());
        holder.Title.setText(currentPost.getTitle());
        holder.Setting.setOnClickListener(v -> {
            View popupView = LayoutInflater.from(context).inflate(R.layout.pages_scheduled_popupmenu, null);

            // Create the PopupWindow with desired width and height
            final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            // Set a background for the popup to enable outside touch dismissal
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);

            int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
            int[] location = new int[2];
            v.getLocationOnScreen(location);
            int anchorY = location[1];
            int anchorHeight = v.getHeight();

            // Measure the PopupWindow dimensions
            popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int popupHeight = popupView.getMeasuredHeight();

            // Calculate y-offset based on available space
            int yOffset;
            if (anchorY + anchorHeight + popupHeight > screenHeight) {
                // Not enough space below, show above the anchor
                yOffset = anchorY - popupHeight;
            } else {
                // Enough space below, show below the anchor
                yOffset = anchorY + anchorHeight;
            }

            // Show the PopupWindow at the calculated position
            popupWindow.showAtLocation(v, 0, location[0], yOffset);

            // Set click listeners for each menu item
            popupView.findViewById(R.id.scheduled_view_item_page).setOnClickListener(view -> {
                Toast.makeText(context, "Viewed from scheduled page", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.scheduled_cancel_upload_item_page).setOnClickListener(view -> {
                Toast.makeText(context, "Canceled upload from scheduled page", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.scheduled_set_parent_item_page).setOnClickListener(view -> {
                Toast.makeText(context, "Set parent from scheduled page", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.scheduled_move_to_draft_item_page).setOnClickListener(view -> {
                Toast.makeText(context, "Moved to draft from scheduled page", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.scheduled_duplicate_item_page).setOnClickListener(view -> {
                Toast.makeText(context, "Duplicated from scheduled page", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.scheduled_share_item_page).setOnClickListener(view -> {
                Toast.makeText(context, "Share from scheduled page", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.scheduled_trash_item_page).setOnClickListener(view -> {
                Toast.makeText(context, "Trashed from scheduled page", Toast.LENGTH_SHORT).show();
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
            Setting = itemView.findViewById(R.id.content_setting_btn);
        }
    }

    public void setSchedulePage(ArrayList<ContentCardModel> schedulePage){
        this.postList = schedulePage;
    }

}

