package vn.edu.usth.wordpressclient.PagesFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import vn.edu.usth.wordpressclient.R;

public class PagePublishedFragment extends Fragment {
    Button PublishedButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page_published, container, false);
        PublishedButton = view.findViewById(R.id.published_page_button);
        PublishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "This should create a page to publish", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}