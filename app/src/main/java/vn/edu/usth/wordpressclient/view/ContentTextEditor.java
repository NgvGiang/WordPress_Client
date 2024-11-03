package vn.edu.usth.wordpressclient.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.viewmodel.ContentViewModel;

public class ContentTextEditor extends AppCompatActivity {
    private ProgressBar toolbarProgressBar;
    private EditText editTextTitle;
    private EditText editTextContent;
    private String domain;
    private String Date;
    private SessionManager session;
    private ContentViewModel contentViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_text_editor);
        domain = DomainManager.getInstance().getSelectedDomain();

        if (domain != null) {
            Log.i("domain", domain);
        } else {
            Log.i("domain", "Domain is null");
        }

        toolbarProgressBar = findViewById(R.id.toolbar_progress_bar);
        editTextContent = findViewById(R.id.fab_content);
        editTextTitle = findViewById(R.id.fab_title);

        Toolbar toolbar = findViewById(R.id.fab_toolbar);
        setSupportActionBar(toolbar);
        // Set title
        getSupportActionBar().setTitle("");
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.vert_dot));
        //mapping viewmodel
        contentViewModel = new ViewModelProvider(this).get(ContentViewModel.class);
        contentViewModel.getCreateSuccessLiveData().observe(this, success -> {
            toolbarProgressBar.setVisibility(View.GONE);
            if (success) {
                //just finish but after 2.5 second
                Snackbar.make(findViewById(android.R.id.content), "Content created successfully", 2500).show();
                new Handler(Looper.getMainLooper()).postDelayed(this::finish, 2500);
            }else {
                //occur network problem ?
                Snackbar.make(findViewById(android.R.id.content), "Failed to create content, please try again", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fab_menu, menu);
        return true;
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.publish_button) {
            createContentByAPI("publish", getIntent().getStringExtra("endpoint"));
            return true;
        } else if (item.getItemId() == R.id.save_btn) {
            createContentByAPI("draft", getIntent().getStringExtra("endpoint"));
            return true;
        } else if (item.getItemId() == R.id.structure_btn) {
            showStructureDialog();
            return true;
        } else if (item.getItemId() == R.id.help_btn) {
            Snackbar.make(findViewById(android.R.id.content), "Helped", Snackbar.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.publish_date_btn) {
            showDateTimePicker();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showStructureDialog() {
        // Get (fab_content)
        String content = editTextContent.getText().toString();

        int blockCount = content.isEmpty() ? 0 : content.split("\n").length;  // Count blocks by newline
        int wordCount = content.isEmpty() ? 0 : content.split("\\s+").length;  // Count words by spaces
        int charCount = content.length();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Content Structure")
                .setMessage("Blocks: " + blockCount + "\nWords: " + wordCount + "\nCharacters: " + charCount)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();  //ok
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();

        // Get current date and time
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Date picker
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                R.style.CustomDatePicker,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // After date is picked, show time picker dialog
                        showTimePickerDialog(selectedYear, selectedMonth, selectedDay);
                    }
                }, year, month, day);

        //OR Choose RN
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Immediately", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Take right now
                String dateTime = String.format("Scheduled for %d-%02d-%02d %02d:%02d", year, month + 1, day, hour, minute);

                Snackbar.make(findViewById(android.R.id.content), dateTime, Snackbar.LENGTH_SHORT).show();

            }
        });

        datePickerDialog.show();


    }

    private void showTimePickerDialog(int year, int month, int day) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        //time picker
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                R.style.CustomTimePicker,
                (view, selectedHour, selectedMinute) -> {

                    Date = String.format("%d-%02d-%02dT%02d:%02d", year, month + 1, day, selectedHour, selectedMinute);


                    String dateTime = String.format("Scheduled for %d-%02d-%02d %02d:%02d", year, month + 1, day, selectedHour, selectedMinute);

                    Snackbar.make(findViewById(android.R.id.content), dateTime, Snackbar.LENGTH_SHORT).show();

                }, hour, minute, true);

        timePickerDialog.show();
    }

    private String formatForAPI(int year, int month, int day, int hour, int minute) {
        return String.format("%d-%02d-%02dT%02d:%02d", year, month + 1, day, hour, minute);
    }

    public void createContentByAPI(String status, String endpoint){
        toolbarProgressBar.setVisibility(View.VISIBLE);
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();
        if (title.trim().isEmpty()) {
            title = "Untitled";
        }
        contentViewModel.createContent(endpoint, domain, title, content, status, Date);
    }
}



