package vn.edu.usth.wordpressclient.PostsFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import vn.edu.usth.wordpressclient.Post_page_adapter;
import vn.edu.usth.wordpressclient.Post_page_card_model;
import vn.edu.usth.wordpressclient.R;

public class DraftPostFragment extends Fragment {
    private RecyclerView recyclerView;
    private ConstraintLayout noPostsMessage;
    private Post_page_adapter adapter;
    private ArrayList<Post_page_card_model> postList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_draft_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.post_recycler_view);
        noPostsMessage = view.findViewById(R.id.no_post_screen);


        postList = new ArrayList<>();
        //Uncomment below to add sample post data
        postList.add(new Post_page_card_model("Oct 13", "Sample Title 1", "Sample Content 1"));
        postList.add(new Post_page_card_model("Oct 14", "Diary", "im gay"));
        postList.add(new Post_page_card_model("Oct 15", "Why slaves should be free", "i made a mistake ..."));
        postList.add(new Post_page_card_model("Oct 22", "French Midterm", "Je suis un chien"));
        postList.add(new Post_page_card_model("Oct 23", "Happy Bday Chuck", "HAPPYY BIRTHDAYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY"));

        adapter = new Post_page_adapter(getContext(), postList);
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
