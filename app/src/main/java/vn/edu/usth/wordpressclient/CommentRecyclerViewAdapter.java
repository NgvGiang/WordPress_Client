package vn.edu.usth.wordpressclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Parcelable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import vn.edu.usth.wordpressclient.models.Comment;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    private List<Comment> comments;
    private Context context;
    private String domain;

    public CommentRecyclerViewAdapter(List<Comment> comments, Context context, String domain) {
        this.comments = comments;
        this.context = context;
        this.domain = domain;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_recycler_view_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        LocalDateTime date = LocalDateTime.parse(comments.get(position).getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        Long dateInSeconds = Duration.between(date, LocalDateTime.now()).getSeconds();
        if (dateInSeconds <= 86400) {
            holder.date.setText(R.string.today);
        } else if (86400 < dateInSeconds && dateInSeconds <= 172800) {
            holder.date.setText(R.string.yesterday);
        } else {
            holder.date.setText(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }
        holder.authorNameAndPostName.setText(comments.get(position).getAuthorName() + " on Saturday, 12 October 2024");
        holder.content.setText(Html.fromHtml(comments.get(position).getContent(), Html.FROM_HTML_MODE_LEGACY).toString());
        Picasso.get().load(comments.get(position).getAuthorAvatar()).error(R.drawable.blank_avatar).transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                int size = Math.min(source.getWidth(), source.getHeight());
                int x = (source.getWidth() - size) / 2;
                int y = (source.getHeight() - size) / 2;

                Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
                if (squaredBitmap != source) {
                    source.recycle();
                }

                Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                BitmapShader shader = new BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                paint.setShader(shader);
                paint.setAntiAlias(true);
                float r = size / 2f;
                canvas.drawCircle(r, r, r, paint);

                squaredBitmap.recycle();
                return bitmap;
            }

            @Override
            public String key() {
                return "circle";
            }
        }).into(holder.authorAvatar);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentDetails.class);
                intent.putExtra("comment", (Serializable) comments.get(holder.getAdapterPosition()));
                intent.putExtra("domain", domain);
                intent.putExtra("position", holder.getAdapterPosition());
                ((CommentActivity) context).commentDetailLauncher.launch(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}

class CommentViewHolder extends RecyclerView.ViewHolder{
    TextView authorNameAndPostName, content, date;
    ImageView authorAvatar;
    LinearLayout cardView;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        authorNameAndPostName = itemView.findViewById(R.id.author_name_comment_and_post_name);
        content = itemView.findViewById(R.id.comment_content);
        date = itemView.findViewById(R.id.comment_date);
        authorAvatar = itemView.findViewById(R.id.author_avatar_comment);
        cardView = itemView.findViewById(R.id.cmt_rec_card);
    }
}
