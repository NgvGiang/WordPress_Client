package vn.edu.usth.wordpressclient.view.posts;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import vn.edu.usth.wordpressclient.Post_page_card_model;
import vn.edu.usth.wordpressclient.PostsDraftAdapter;
import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.view.ContentTextEditor;

public class PostDraftFragment extends Fragment {
    Button CreateButton;
    private RecyclerView recyclerView;
    private ConstraintLayout noPostsMessage;
    private PostsDraftAdapter adapter;
    private ArrayList<Post_page_card_model> postList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_draft, container, false);

        recyclerView = view.findViewById(R.id.post_recycler_view);
        noPostsMessage = view.findViewById(R.id.no_post_screen);


        postList = new ArrayList<>();
        //Uncomment below to add sample post data
        postList.add(new Post_page_card_model("Oct 13", "Sample Title 1", "Sample Content 1"));
        postList.add(new Post_page_card_model("Oct 14", "Diary", "im gay"));
        postList.add(new Post_page_card_model("Oct 15", "Why slaves should be free", "i made a mistake ..."));
        postList.add(new Post_page_card_model("Oct 22", "French Midterm", "Je suis un chien"));
        postList.add(new Post_page_card_model("Oct 23", "Happy Bday Chuck", "HAPPYY BIRTHDAYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY"));

        adapter = new PostsDraftAdapter(getContext(), postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        toggleNoPostsMessage();

        CreateButton = view.findViewById(R.id.draft_post_button);
        CreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ContentTextEditor.class);
                intent.putExtra("endpoint", "posts");
                startActivity(intent);
            }
        });
        return view;
    }
    private void toggleNoPostsMessage() {
        if (postList.isEmpty()) {

            noPostsMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {

            noPostsMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}