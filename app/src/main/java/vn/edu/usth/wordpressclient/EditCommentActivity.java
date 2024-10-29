package vn.edu.usth.wordpressclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import vn.edu.usth.wordpressclient.models.CommentDetailCallback;

public class EditCommentActivity extends AppCompatActivity {
    private String domain, content, authorName, authorUrl;
    private Long commentId;
    private TextInputEditText editTextName, editTextWebAddress, editTextComment;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_comment);

        Toolbar toolbar = findViewById(R.id.edit_comment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.comments));

        domain = getIntent().getStringExtra("domain");
        content = getIntent().getStringExtra("content");
        authorName = getIntent().getStringExtra("authorName");
        authorUrl = getIntent().getStringExtra("authorUrl");
        commentId = getIntent().getLongExtra("commentId", -1);

        editTextName = findViewById(R.id.txtInputEditTxtName);
        editTextWebAddress = findViewById(R.id.txtInputEditTxtAddress);
        editTextComment = findViewById(R.id.txtInputEditTxtComment);

        editTextName.setText(authorName);
        editTextWebAddress.setText(authorUrl);
        editTextComment.setText(content);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_comment_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            updateComment(editTextComment.getText().toString());
            Intent intent = new Intent();
            intent.putExtra("content", editTextComment.getText().toString());
            setResult(Activity.RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void showLoadingDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(new ProgressBar(this));
//        builder.setCancelable(false);
//        progressDialog = builder.create();
//        if (!isFinishing()) {
//            progressDialog.show();
//        }
//        progressDialog.show();
//    }

    private void updateComment(String updateContent) {
//        showLoadingDialog();
        CommentAPIServices.editComment(this, domain, commentId, updateContent, new CommentDetailCallback() {
            @Override
            public void onSuccess() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onError() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }
}