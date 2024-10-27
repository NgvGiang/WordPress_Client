package vn.edu.usth.wordpressclient.PagesFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.ContentTextEditor;

public class PageScheduledFragment extends Fragment {
    Button ScheduledButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_scheduled, container, false);
        ScheduledButton = view.findViewById(R.id.create_page_btn);
        ScheduledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ContentTextEditor.class);
                startActivity(intent);
            }
        });
        return view;
    }
}