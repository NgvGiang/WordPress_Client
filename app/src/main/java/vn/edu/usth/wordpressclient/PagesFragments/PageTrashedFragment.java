package vn.edu.usth.wordpressclient.PagesFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.TextEditor;

public class PageTrashedFragment extends Fragment {
    Button TrashedButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_trashed, container, false);
        TrashedButton = view.findViewById(R.id.trashed_page_button);
        TrashedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TextEditor.class);
                startActivity(intent);
            }
        });

        return view;
    }
}