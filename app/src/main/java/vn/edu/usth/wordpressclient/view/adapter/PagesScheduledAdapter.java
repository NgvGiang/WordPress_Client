package vn.edu.usth.wordpressclient.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.model.ContentCardModel;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.view.ContentTextEditor;
import vn.edu.usth.wordpressclient.view.pages.PageScheduledFragment;
import vn.edu.usth.wordpressclient.viewmodel.ContentViewModel;

public class PagesScheduledAdapter extends RecyclerView.Adapter<PagesScheduledAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ContentCardModel> postList;
    private ContentViewModel contentViewModel;
    private PageScheduledFragment fragment;
    String domain = DomainManager.getInstance().getSelectedDomain();

    public PagesScheduledAdapter(Context context, PageScheduledFragment fragment) {
        this.context = context;
        this.postList = new ArrayList<>();
        this.fragment = fragment;
        this.contentViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(ContentViewModel.class);
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

        int id = currentPost.getId();
        holder.Date.setText(currentPost.getDate());
        holder.Title.setText(currentPost.getTitle());
        holder.content_cardview.setOnClickListener(v -> {
            Intent intent = new Intent(context, ContentTextEditor.class);
            intent.putExtra("id", id);
            intent.putExtra("title", currentPost.getTitle());
            intent.putExtra("content", currentPost.getContent());
            intent.putExtra("endpoint", "pages");
            context.startActivity(intent);
        });
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
//                Toast.makeText(context, "Viewed from scheduled page", Toast.LENGTH_SHORT).show();
                String url = currentPost.getLink();
                Intent domainIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(domainIntent);
                popupWindow.dismiss();
            });


            popupView.findViewById(R.id.scheduled_set_parent_item_page).setOnClickListener(view -> {
                Toast.makeText(context, "Set parent from scheduled page", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.scheduled_move_to_draft_item_page).setOnClickListener(view -> {
                holder.progressBar.setVisibility(View.VISIBLE);
                contentViewModel.restoreContent("pages",domain , id);
                contentViewModel.getRestoreSuccessLiveData().observe((LifecycleOwner) context, success -> {
                    holder.progressBar.setVisibility(View.INVISIBLE);
                    if (success) {
                        fragment.refresh();
                        Snackbar.make(fragment.getView(), R.string.restore_successfully, Snackbar.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(fragment.getView(), R.string.restore_failed, Snackbar.LENGTH_SHORT).show();
                    }
                });
                popupWindow.dismiss();
            });


            popupView.findViewById(R.id.scheduled_share_item_page).setOnClickListener(view -> {
                Toast.makeText(context, "Share from scheduled page", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.scheduled_trash_item_page).setOnClickListener(view -> {
                holder.progressBar.setVisibility(View.VISIBLE);
                contentViewModel.trashContent("pages",domain , id);
                contentViewModel.getDeleteSuccessLiveData().observe((LifecycleOwner) context, success -> {
                    holder.progressBar.setVisibility(View.INVISIBLE);
                    if (success) {
                        fragment.refresh();
                        Snackbar.make(fragment.getView(), R.string.deleted_successfully, Snackbar.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(fragment.getView(), R.string.deleted_failed, Snackbar.LENGTH_SHORT).show();
                    }
                });
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
        ProgressBar progressBar;
        LinearLayout content_cardview;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Grabbing views
            Date = itemView.findViewById(R.id.item_date);
            Title = itemView.findViewById(R.id.item_title);
            Setting = itemView.findViewById(R.id.content_setting_btn);
            progressBar = itemView.findViewById(R.id.progress_bar);
            content_cardview = itemView.findViewById(R.id.content_cardview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ContentCardModel currentPost = postList.get(position);
                        Intent intentToEditor = new Intent(context, ContentTextEditor.class);
                        intentToEditor.putExtra("id", currentPost.getId());
                        intentToEditor.putExtra("title", currentPost.getTitle());
                        intentToEditor.putExtra("content", currentPost.getContent());
                        intentToEditor.putExtra("endpoint", "pages");
                        context.startActivity(intentToEditor);
                    }
                }
            });
        }
    }

    public void setSchedulePage(ArrayList<ContentCardModel> schedulePage){
        this.postList = schedulePage;
    }

}

