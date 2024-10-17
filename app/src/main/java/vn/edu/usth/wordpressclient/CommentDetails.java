package vn.edu.usth.wordpressclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import vn.edu.usth.wordpressclient.models.Comment;
import vn.edu.usth.wordpressclient.models.CommentDetailCallback;

public class CommentDetails extends AppCompatActivity {
    TextView authorName, title, content;
    RelativeLayout approve, spam, like, more;
    String userDomain;
    Comment comment;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment_details);

        Toolbar toolbar = findViewById(R.id.comment_detail_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.comments));

        comment = (Comment) getIntent().getSerializableExtra("comment");
        userDomain = getIntent().getStringExtra("domain");
        position = getIntent().getIntExtra("position", -1);
        Picasso.get().load(comment.getAuthorAvatar())
                .error(R.drawable.blank_avatar)
                .transform(new Transformation() {
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
                }).into((ImageView) findViewById(R.id.author_avatar_comment_detail));
        authorName = findViewById(R.id.author_name_comment_detail);
        authorName.setText(comment.getAuthorName());
        title = findViewById(R.id.post_title_comment_detail);
        title.setText("" + comment.getPost());
        content = findViewById(R.id.comment_content_in_comment_detail);
        content.setText(Html.fromHtml(comment.getContent(), Html.FROM_HTML_MODE_LEGACY).toString());

        approve = findViewById(R.id.mark_approve_comment);
        ImageView approvedIcon = findViewById(R.id.done_icon_comment_detail);
        TextView approvedText = findViewById(R.id.done_text_comment_detail);
        spam = findViewById(R.id.mark_spam_comment);
        TextView spamText = findViewById(R.id.report_text_comment_detail);
        like = findViewById(R.id.like_comment);
        more = findViewById(R.id.more_action_on_comment);

        if (comment.getStatus().equals("approved")) {
            approvedIcon.setImageResource(R.drawable.baseline_done_24);
            approvedText.setTextColor(Color.parseColor("#26A41A"));
        }

        if (comment.getStatus().equals("spam")) {
            spamText.setText("Not Spam");
        }

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Approve", "Clicked");
                Log.i("Status of comment", comment.getStatus());
                Intent resultIntent = new Intent();
                if (!comment.getStatus().equals("approved")) {
                    Log.i("Status not approved", comment.getStatus());
                    Log.i("position", "" + position);
                    approveComment("approve");
                    resultIntent.putExtra("updatedCommentId", position);
                    resultIntent.putExtra("status", "approve");
                } else {
                    Log.i("Status approve", comment.getStatus());
                    Log.i("position", "" + position);
                    pendingComment("hold");
                    resultIntent.putExtra("updatedCommentId", position);
                    resultIntent.putExtra("status", "hold");
                }
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        spam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!comment.getStatus().equals("spam")) {
                    spamComment("spam");
                } else {
                    approveComment("approve");
                }
                finish();
            }
        });
    }

    public void approveComment(String status) {
        CommentAPIServices.updateCommentStatus(this, userDomain, comment.getId(), status);
    }

    public void spamComment(String status) {
        CommentAPIServices.updateCommentStatus(this, userDomain, comment.getId(), status);
    }

    public void pendingComment(String status) {
        CommentAPIServices.updateCommentStatus(this, userDomain, comment.getId(), status);
    }
}