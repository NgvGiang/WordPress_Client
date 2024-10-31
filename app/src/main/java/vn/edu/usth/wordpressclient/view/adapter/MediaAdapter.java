package vn.edu.usth.wordpressclient.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.model.MediaCardModel;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaCardViewHolder> {
    private Context context;
    private ArrayList<MediaCardModel> mediaUrls;

    public MediaAdapter(Context context){
        this.context = context;
        this.mediaUrls = new ArrayList<>();
    }
    public void setMediaUrls(ArrayList<MediaCardModel> mediaUrls){
        this.mediaUrls = mediaUrls;
    }

    @NonNull
    @Override
    public MediaAdapter.MediaCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi CardView
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.media_cardview, parent, false);
        return new MediaAdapter.MediaCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaAdapter.MediaCardViewHolder holder, int position) {
        int startIndex = position * 4;
        int endIndex = Math.min(startIndex + 4, mediaUrls.size());
        ArrayList<MediaCardModel> images = new ArrayList<>(mediaUrls.subList(startIndex, endIndex));

        // Load images into the 4 ImageViews, hide any that aren't used
        ImageView[] imageViews = {holder.picture1, holder.picture2, holder.picture3, holder.picture4};
        for (int i = 0; i < imageViews.length; i++) {
            if (i < images.size()) {
                imageViews[i].setVisibility(View.VISIBLE);
                String imageUrl = images.get(i).getPicture_url();
                Picasso.get()
                        .load(images.get(i).getPicture_url())
                        .into(imageViews[i]);

                imageViews[i].setOnClickListener(v -> showFullScreenImageDialog(imageUrl));
            } else {
                imageViews[i].setVisibility(View.INVISIBLE);
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

//    public static class MediaCardViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView1, imageView2, imageView3, imageView4;
//
//        public MediaCardViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView1 = itemView.findViewById(R.id.imageView1);
//            imageView2 = itemView.findViewById(R.id.imageView2);
//            imageView3 = itemView.findViewById(R.id.imageView3);
//            imageView4 = itemView.findViewById(R.id.imageView4);
//        }
//    }