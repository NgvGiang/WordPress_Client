package vn.edu.usth.wordpressclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import vn.edu.usth.wordpressclient.models.GetCommentsCallback;

public class CommentDetails extends AppCompatActivity {
    TextView authorName, title, content, spanEditText;
    EditText editText;
    LinearLayout replyCommentField;
    RelativeLayout approve, spam, like, more;
    String userDomain, editedContent;
    Comment comment;
    int position;
    String fragment;
    Comment repliedComment;
    ActivityResultLauncher<Intent> editCommentLauncher;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment_details);

        Toolbar toolbar = findViewById(R.id.comment_detail_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.comments));

        comment = getIntent().getParcelableExtra("comment", Comment.class);
        userDomain = getIntent().getStringExtra("domain");
        position = getIntent().getIntExtra("position", -1);
        fragment = getIntent().getStringExtra("fragment");
        Log.i("Fragment from comment detail", fragment);
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

        ImageView uploadReply = findViewById(R.id.upload_reply);
        editText = findViewById(R.id.input_reply);
        replyCommentField = findViewById(R.id.reply_cmt_field);

        editCommentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            editedContent = data.getStringExtra("content");
                            Log.i("editedContent", editedContent);
                        }
                        updateCommentOnContentTextView(editedContent);
                    }
                }
        );

        if (comment.getStatus().equals("approved")) {
            approvedIcon.setImageResource(R.drawable.baseline_done_24);
            approvedText.setTextColor(Color.parseColor("#26A41A"));
        }

        if (comment.getStatus().equals("spam")) {
            spamText.setText("Not Spam");
        }

        if (comment.getStatus().equals("trash")) {
            approvedIcon.setImageResource(R.drawable.baseline_restore_24);
            approvedText.setText("Restore");
        }

//        float dpOffset = -300f;
//        float pixelOffset = TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                dpOffset,
//                getResources().getDisplayMetrics()
//        );
//        editText.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                replyCommentField.setTranslationY(pixelOffset);
//                return false;
//            }
//        });
//        editText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN) {
//                    // Handle the dropdown action
//                    replyCommentField.setTranslationY(0);
//                    return true; // Consume the event
//                }
//                return false;
//            }
//        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!comment.getStatus().equals("trash")) {
                    showPopupMenu(v);
                } else {
                    showTrashPopupMenu(v);
                }
            }
        });

        uploadReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyComment();
            }
        });

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
                    if (comment.getStatus().equals("hold")) {
                        resultIntent.putExtra("ChangeFrom", "ApproveHold");
                    } else if (comment.getStatus().equals("spam")) {
                        resultIntent.putExtra("ChangeFrom", "SpamToApprove");
                    }
                    resultIntent.putExtra("fragment", fragment);
                } else {
                    Log.i("Status approve", comment.getStatus());
                    Log.i("position", "" + position);
                    pendingComment("hold");
                    resultIntent.putExtra("updatedCommentId", position);
                    resultIntent.putExtra("status", "hold");
                    resultIntent.putExtra("ChangeFrom", "ApproveHold");
                    resultIntent.putExtra("fragment", fragment);
                }
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        spam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (!comment.getStatus().equals("spam")) {
                    Log.i("Status spam", comment.getStatus());
                    Log.i("position", "" + position);
                    spamComment("spam");
                    intent.putExtra("updatedCommentId", position);
                    intent.putExtra("status", "spam");
                    if (comment.getStatus().equals("approved")) {
                        intent.putExtra("ChangeFrom", "ApproveSpam");
                    }
                    if (comment.getStatus().equals("hold")){
                        intent.putExtra("ChangeFrom", "SpamHold");
                    }
                    intent.putExtra("fragment", fragment);
                } else {
                    Log.i("Status approved", comment.getStatus());
                    Log.i("position", "" + position);
                    approveComment("approve");
                    intent.putExtra("updatedCommentId", position);
                    intent.putExtra("status", "approve");
                    intent.putExtra("ChangeFrom", "ApproveSpam");
                    intent.putExtra("fragment", fragment);
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent();
        intent.putExtra("updatedCommentId", position);
        intent.putExtra("fragment", fragment);
        intent.putExtra("content", editedContent);
        setResult(Activity.RESULT_OK, intent);
        finish();
        return true;
    }

    public void approveComment(String status) {
        CommentAPIServices.updateCommentStatus(this, userDomain, comment.getId(), status, new CommentDetailCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
    }

    public void spamComment(String status) {
        CommentAPIServices.updateCommentStatus(this, userDomain, comment.getId(), status, new CommentDetailCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
    }

    public void pendingComment(String status) {
        CommentAPIServices.updateCommentStatus(this, userDomain, comment.getId(), status, new CommentDetailCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
    }

    private void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(new ProgressBar(this));
        builder.setCancelable(false);
        progressDialog = builder.create();
        progressDialog.show();
    }

    public void replyComment() {
        String reply = editText.getText().toString();
        showLoadingDialog();
        CommentAPIServices.replyComment(this, userDomain, reply, comment.getId(), comment.getPost(), new GetCommentsCallback() {
            @Override
            public void onSuccess(List<Comment> newComments) {
                repliedComment = newComments.get(0);
                Log.i("Reply comment log cat in function", repliedComment.getContent());
                Toast.makeText(CommentDetails.this, "Reply published", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("replyComment", (Parcelable) repliedComment);
                setResult(Activity.RESULT_OK, intent);
                progressDialog.dismiss();
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.comment_detail_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.move_to_trash) {
                    Intent intent = new Intent();
                    moveCommentToTrash("trash");
                    intent.putExtra("updatedCommentId", position);
                    intent.putExtra("status", "trash");
                    intent.putExtra("fragment", fragment);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.edit_comment) {
                    Intent intent1 = new Intent(CommentDetails.this, EditCommentActivity.class);
                    intent1.putExtra("author", comment.getAuthor());
                    intent1.putExtra("content", comment.getContent());
                    intent1.putExtra("authorName", comment.getAuthorName());
                    intent1.putExtra("authorUrl", comment.getAuthorUrl());
                    intent1.putExtra("domain", userDomain);
                    intent1.putExtra("commentId", comment.getId());
                    CommentDetails.this.editCommentLauncher.launch(intent1);
                    return true;
                } else if (item.getItemId() == R.id.copy_address) {
                    Toast.makeText(CommentDetails.this, "copy address", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    return true;
                }
            }
        });
        popupMenu.show();
    }

    private void showTrashPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.trash_comment_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent();
                if (item.getItemId() == R.id.delete_forever) {
                    deleteComment();
                    intent.putExtra("updatedCommentId", position);
                    intent.putExtra("status", "delete");
                    intent.putExtra("fragment", fragment);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.trash_edit_comment) {
                    Intent intent1 = new Intent(CommentDetails.this, EditCommentActivity.class);
                    intent1.putExtra("author", comment.getAuthor());
                    intent1.putExtra("content", comment.getContent());
                    intent1.putExtra("domain", userDomain);
                    intent1.putExtra("commentId", comment.getId());
                    CommentDetails.this.editCommentLauncher.launch(intent1);
                    return true;
                } else {
                    return true;
                }
            }
        });
        popupMenu.show();
    }

    public void deleteComment() {
        CommentAPIServices.deleteComment(this, userDomain, comment.getId(), new GetCommentsCallback() {
            @Override
            public void onSuccess(List<Comment> newComments) {
                Toast.makeText(CommentDetails.this, "Deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void moveCommentToTrash(String status) {
        CommentAPIServices.updateCommentStatus(this, userDomain, comment.getId(), status, new CommentDetailCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(CommentDetails.this, "Move to trash", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {

            }
        });
    }

    public void updateCommentOnContentTextView(String updateContent) {
        content.setText(Html.fromHtml(updateContent, Html.FROM_HTML_MODE_LEGACY).toString());
    }
}