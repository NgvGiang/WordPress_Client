package vn.edu.usth.wordpressclient.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.model.CommentCardModel;
import vn.edu.usth.wordpressclient.model.MediaCardModel;
import vn.edu.usth.wordpressclient.view.comment.CommentDetailActivity;
import vn.edu.usth.wordpressclient.viewmodel.CommentViewModel;

public class CommentAllAdapter extends RecyclerView.Adapter<CommentAllAdapter.CommentCardViewHolder>{
    private Context context;
    private ArrayList<CommentCardModel> commentCardModels;
//    private CommentViewModel commentViewModel;
//    private LifecycleOwner lifecycleOwner;

//    public CommentAllAdapter(Context context, CommentViewModel commentViewModel, LifecycleOwner lifecycleOwner) {
//        this.context = context;
//        this.commentCardModels = new ArrayList<>();
//        this.commentViewModel = commentViewModel;
//        this.lifecycleOwner = lifecycleOwner;
//    }

    public CommentAllAdapter(Context context) {
        this.context = context;
        this.commentCardModels = new ArrayList<>();
    }

    public void setCommentCardModels(ArrayList<CommentCardModel> commentCardModels) {
        this.commentCardModels.clear();
        this.commentCardModels = commentCardModels;
    }

    @NonNull
    @Override
    public CommentAllAdapter.CommentCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment_recycler_view_item, parent, false);
        return new CommentAllAdapter.CommentCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAllAdapter.CommentCardViewHolder holder, int position) {
        holder.date.setText(commentCardModels.get(position).getDate());
        holder.authorNameAndPostName.setText(commentCardModels.get(position).getAuthorName() + " on " + commentCardModels.get(position).getPostTitle());
        holder.content.setText(Html.fromHtml(commentCardModels.get(position).getContent(), Html.FROM_HTML_MODE_LEGACY).toString());
        Picasso.get().load(commentCardModels.get(position).getAuthorAvatar()).error(R.drawable.blank_avatar).into(holder.authorAvatar);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentDetailActivity.class);
                intent.putExtra("commentId", commentCardModels.get(holder.getAdapterPosition()).getId());
                intent.putExtra("authorName", commentCardModels.get(holder.getAdapterPosition()).getAuthorName());
                intent.putExtra("authorAvatar", commentCardModels.get(holder.getAdapterPosition()).getAuthorAvatar());
                intent.putExtra("post", commentCardModels.get(holder.getAdapterPosition()).getPost());
                intent.putExtra("content", commentCardModels.get(holder.getAdapterPosition()).getContent());
                intent.putExtra("status", commentCardModels.get(holder.getAdapterPosition()).getStatus());
                intent.putExtra("link", commentCardModels.get(holder.getAdapterPosition()).getLink());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentCardModels.size();
    }


    class CommentCardViewHolder extends RecyclerView.ViewHolder{
        TextView authorNameAndPostName, content, date;
        CircleImageView authorAvatar;
        LinearLayout cardView;

        public CommentCardViewHolder(@NonNull View itemView) {
            super(itemView);
            authorNameAndPostName = itemView.findViewById(R.id.author_name_comment_and_post_name);
            content = itemView.findViewById(R.id.comment_content);
            date = itemView.findViewById(R.id.comment_date);
            authorAvatar = itemView.findViewById(R.id.author_avatar_comment);
            cardView = itemView.findViewById(R.id.cmt_rec_card);
        }
    }
}