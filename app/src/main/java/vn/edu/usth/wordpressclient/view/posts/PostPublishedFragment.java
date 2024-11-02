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
import vn.edu.usth.wordpressclient.PostsPublishedAdapter;
import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.view.ContentTextEditor;


public class PostPublishedFragment extends Fragment {
    Button CreateButton;
    private RecyclerView recyclerView;
    private ConstraintLayout noPostsMessage;
    private PostsPublishedAdapter adapter;
    private ArrayList<Post_page_card_model> postList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_post_published, container, false);

        recyclerView = view.findViewById(R.id.post_published_recycler_view);
        noPostsMessage = view.findViewById(R.id.no_post_screen_published);


        postList = new ArrayList<>();
        //Uncomment below to add sample post data
        postList.add(new Post_page_card_model("Oct 31", "French training", "Putain ma vie "));
        postList.add(new Post_page_card_model("Oct 30", "Notes", "In Android, we know multiple networking libraries like Retrofit, Volley. We use these libraries specifically for making networking requests like performing actions on API. But Besides that"));


        adapter = new PostsPublishedAdapter(getContext(), postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        toggleNoPostsMessage();

        CreateButton = view.findViewById(R.id.published_post_button);
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