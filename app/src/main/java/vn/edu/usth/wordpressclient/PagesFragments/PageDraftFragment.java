package vn.edu.usth.wordpressclient.PagesFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import vn.edu.usth.wordpressclient.ContentTextEditor;
import vn.edu.usth.wordpressclient.R;

public class PageDraftFragment extends Fragment {
    Button DraftButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_draft, container, false);
        DraftButton = view.findViewById(R.id.draft_page_button);
        DraftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ContentTextEditor.class);
                startActivity(intent);
            }
        });
        return view;

    }
}