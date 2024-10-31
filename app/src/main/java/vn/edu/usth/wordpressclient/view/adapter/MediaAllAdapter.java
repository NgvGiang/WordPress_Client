package vn.edu.usth.wordpressclient.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.content.res.AppCompatResources;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.model.MediaCardModel;

public class MediaAllAdapter extends RecyclerView.Adapter<MediaAllAdapter.MediaCardViewHolder> {
    private Context context;
    private ArrayList<MediaCardModel> mediaUrls;

    public MediaAllAdapter(Context context){
        this.context = context;
        this.mediaUrls = new ArrayList<>();
    }
    public void setMediaUrls(ArrayList<MediaCardModel> mediaUrls){
        this.mediaUrls = mediaUrls;
    }

    @NonNull
    @Override
    public MediaAllAdapter.MediaCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi CardView
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.media_cardview, parent, false);
        return new MediaAllAdapter.MediaCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaAllAdapter.MediaCardViewHolder holder, int position) {
        int startIndex = position * 4;
        int endIndex = Math.min(startIndex + 4, mediaUrls.size());
        ArrayList<MediaCardModel> images = new ArrayList<>(mediaUrls.subList(startIndex, endIndex));

        // Load images into the 4 ImageViews
        ImageView[] imageViews = {holder.picture1, holder.picture2, holder.picture3, holder.picture4};
        for (int i = 0; i < imageViews.length; i++) {
            final int index = i;
            if (i < images.size()) {
                String url = images.get(i).getPicture_url();
                imageViews[index].setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(url)
                        .error(R.drawable.premium)
                        .into(imageViews[i]);
                imageViews[index].setOnClickListener(v -> {
                    if (imageViews[index].getDrawable().getConstantState()
                            .equals(AppCompatResources.getDrawable(context, R.drawable.premium).getConstantState())) {
                        Snackbar.make(v, "Only available for Premium", Snackbar.LENGTH_LONG).show();
                    } else {
                        showFullScreenImageDialog(url);
                    }
                });
            } else {
                imageViews[index].setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil(mediaUrls.size() / 4.0);
    }

    class MediaCardViewHolder extends RecyclerView.ViewHolder{
        ImageView picture1, picture2, picture3, picture4;

        public MediaCardViewHolder(@NonNull View itemView) {
            super(itemView);
            picture1 = itemView.findViewById(R.id.imageView1);
            picture2 = itemView.findViewById(R.id.imageView2);
            picture3 = itemView.findViewById(R.id.imageView3);
            picture4 = itemView.findViewById(R.id.imageView4);
        }
    }

    private void showFullScreenImageDialog(String imageUrl) {
        // Create a full-screen dialog
        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_fullscreen_image);

        PhotoView photoView = dialog.findViewById(R.id.fullScreenImageView);
        Picasso.get().load(imageUrl).into(photoView);

        // Close the dialog when the user taps on the image
        photoView.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}