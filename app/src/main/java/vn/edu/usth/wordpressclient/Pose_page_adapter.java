package vn.edu.usth.wordpressclient;

import android.content.Context;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Pose_page_adapter extends RecyclerView.Adapter<Pose_page_adapter.MyViewHolder> {
    private Context context;
    private ArrayList<Post_page_card_model> postList;

    // Adapter constructor
    public Pose_page_adapter(Context context, ArrayList<Post_page_card_model> postList) {
        this.context = context;
        this.postList = postList;
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
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.draft_popup_setting);

            try {
                // Set force show icon
                Field popup = PopupMenu.class.getDeclaredField("mPopup");
                popup.setAccessible(true);
                Object menuPopupHelper = popup.get(popupMenu);
                menuPopupHelper.getClass().getDeclaredMethod("setForceShowIcon", boolean.class).invoke(menuPopupHelper, true);

                // Set custom background using ContextCompat
                Field backgroundField = menuPopupHelper.getClass().getDeclaredField("mBackground");
                backgroundField.setAccessible(true);
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.overflow_menu_bg);
                backgroundField.set(menuPopupHelper, drawable);
            } catch (Exception e) {
                e.printStackTrace();
            }

            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.view) {
                    Toast.makeText(context, "Viewed", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.publish) {
                    Toast.makeText(context, "Published", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.duplicate) {
                    Toast.makeText(context, "Duplicated", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.share) {
                    Toast.makeText(context, "Shared", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.trash) {
                    Toast.makeText(context, "Trashed", Toast.LENGTH_SHORT).show();
                }
                return true;
            });
            popupMenu.show();
        });
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
