package vn.edu.usth.wordpressclient.PostsFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import vn.edu.usth.wordpressclient.PagesTextEditor;

import vn.edu.usth.wordpressclient.Post_page_card_model;
import vn.edu.usth.wordpressclient.PostsTrashedAdapter;
import vn.edu.usth.wordpressclient.R;

public class TrashedPostFragment extends Fragment {
    private RecyclerView recyclerView;
    private ConstraintLayout noPostsMessage;
    private PostsTrashedAdapter adapter;
    private ArrayList<Post_page_card_model> postList;
    private Button CreatePost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_trashed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.post_trashed_recycler_view);
        noPostsMessage = view.findViewById(R.id.no_post_screen_trashed);
        CreatePost = view.findViewById(R.id.trashed_post_button);

        postList = new ArrayList<>();
        //Uncomment below to add sample post data
        postList.add(new Post_page_card_model("Nov 11", "no11", "trashed"));
        postList.add(new Post_page_card_model("Nov 23", "bruh", "brug"));

        CreatePost.setOnClickListener(v -> {
            Intent createPost = new Intent(getActivity(), PagesTextEditor.class);
            startActivity(createPost);
        });


        adapter = new PostsTrashedAdapter(getContext(), postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        toggleNoPostsMessage();
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