package vn.edu.usth.wordpressclient.PagesFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import vn.edu.usth.wordpressclient.R;

public class PageScheduledFragment extends Fragment {
    Button ScheduledButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page_scheduled, container, false);
        ScheduledButton = view.findViewById(R.id.create_page_btn);
        ScheduledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "This should set a page to schedule", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}