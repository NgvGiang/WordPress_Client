package vn.edu.usth.wordpressclient.PagesFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.TextEditor;

public class PagePublishedFragment extends Fragment {
    Button PublishedButton;
    private String domain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_published, container, false);
        domain = getArguments().getString("domain");

        PublishedButton = view.findViewById(R.id.published_page_button);
        PublishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TextEditor.class);
                intent.putExtra("domain",domain);

                startActivity(intent);
            }
        });
        return view;
    }
}