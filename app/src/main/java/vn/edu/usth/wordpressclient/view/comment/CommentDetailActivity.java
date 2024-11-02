package vn.edu.usth.wordpressclient.view.comment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.DomainManager;

public class CommentDetailActivity extends AppCompatActivity {
    TextView authorName, title, content;
    EditText editText;
    LinearLayout replyCommentField;
    RelativeLayout approve, spam, like, more;
    String domain, editedContent;
    CircleImageView authorAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment_detail);

        Toolbar toolbar = findViewById(R.id.comment_detail_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.comments));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        domain = DomainManager.getInstance().getSelectedDomain();
        authorAvatar = findViewById(R.id.author_avatar_comment_detail);
        authorName = findViewById(R.id.author_name_comment_detail);
        title = findViewById(R.id.post_title_comment_detail);
        content = findViewById(R.id.comment_content_in_comment_detail);

        Picasso.get().load(getIntent().getStringExtra("authorAvatar")).error(R.drawable.blank_avatar).into(authorAvatar);
        authorName.setText(getIntent().getStringExtra("authorName"));
        title.setText(getIntent().getStringExtra("title"));
        content.setText(Html.fromHtml(getIntent().getStringExtra("content"), Html.FROM_HTML_MODE_LEGACY).toString());
    }
}