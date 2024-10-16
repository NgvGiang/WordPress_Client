package vn.edu.usth.wordpressclient;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import vn.edu.usth.wordpressclient.models.Comment;

public class CommentDetails extends AppCompatActivity {
    TextView authorName, title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment_details);

        Toolbar toolbar = findViewById(R.id.comment_detail_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.comments));

        Comment comment = (Comment) getIntent().getSerializableExtra("comment");
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


    }
}