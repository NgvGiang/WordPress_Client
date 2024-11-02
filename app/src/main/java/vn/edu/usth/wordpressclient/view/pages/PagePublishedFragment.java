package vn.edu.usth.wordpressclient.view.pages;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.view.ContentTextEditor;

public class PagePublishedFragment extends Fragment {
    Button PublishedButton;
//    private String domain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_published, container, false);
        PublishedButton = view.findViewById(R.id.published_page_button);
        PublishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ContentTextEditor.class);
                intent.putExtra("endpoint", "pages");
                startActivity(intent);
            }
        });
        return view;
    }
}