package vn.edu.usth.wordpressclient;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import vn.edu.usth.wordpressclient.models.MySingleton;

public class TextEditor extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextContent;
    private String domain;
    private String Date;
    private SessionManagement session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_text_editor);

        Intent intentdomain = getIntent();
        domain = intentdomain.getStringExtra("domain");
        if (domain != null) {
            Log.i("domain", domain);
        } else {
            Log.i("domain", "Domain is null");
        }


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
//            Toast.makeText(this, "Published", Toast.LENGTH_SHORT).show();
            createPageByAPI();
            Intent intent = new Intent(this, PagesActivity.class);
            intent.putExtra("domain", domain);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.save_btn) {
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.structure_btn) {
            showStructureDialog();
            return true;
        } else if (item.getItemId() == R.id.help_btn) {
            Toast.makeText(this, "Helped", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(TextEditor.this, dateTime, Toast.LENGTH_SHORT).show();

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
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {

                        Date = String.format("%d-%02d-%02dT%02d:%02d", year, month + 1, day, selectedHour, selectedMinute);


                        String dateTime = String.format("Scheduled for %d-%02d-%02d %02d:%02d", year, month + 1, day, selectedHour, selectedMinute);

                        Toast.makeText(TextEditor.this, dateTime, Toast.LENGTH_SHORT).show();

                    }
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private String formatForAPI(int year, int month, int day, int hour, int minute) {
        return String.format("%d-%02d-%02dT%02d:%02d", year, month + 1, day, hour, minute);
    }

    public void createPageByAPI(){
        String Url = "https://public-api.wordpress.com/wp/v2/sites/"+domain+"/pages";

        // Chuyển nội dung người dùng nhập thành chuỗi
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();

        if (title == null || title.trim().isEmpty()) {
            title = "Untitled";
        }

        // Lấy acess token của người dùng
//        SessionManagement session = new SessionManagement(TextEditor.this);
        session = SessionManagement.getInstance(this);
        String accessToken = session.getAccessToken();

        JSONObject pageData = new JSONObject();
        try {
            pageData.put("title", title);
            pageData.put("content", content);
            pageData.put("status", "publish");
            if (Date != null && !Date.isEmpty()) {
                Log.i("Date",Date);
                pageData.put("date", Date+":00");  // Sử dụng giá trị từ Date
            } else {
                // Nếu người dùng không chọn giá trị cho date, sử dụng thời gian hiện tại
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);

                // Chuyển đổi sang định dạng ISO 8601
                Date = String.format("%d-%02d-%02dT%02d:%02d:%02d", year, month + 1, day, hour, minute, second);
                Log.i("To API",Date+":00");
                pageData.put("date",Date);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest pageRequest = new StringRequest(
                Request.Method.POST,
                Url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String date = jsonResponse.getString("date");
                        Log.i("page created at:", date);
                        Log.i("Creating a page", "success");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                }
        ){
            @Override
            public Map<String,String> getHeaders(){
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            public String getBodyContentType(){
                return "application/json; charset=UTF-8";
            }
            @Override
            public byte[] getBody() {
                return pageData.toString().getBytes(StandardCharsets.UTF_8);
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(pageRequest);
    }
}



