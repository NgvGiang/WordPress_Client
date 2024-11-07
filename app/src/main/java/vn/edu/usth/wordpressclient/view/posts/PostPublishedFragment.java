package vn.edu.usth.wordpressclient.view.posts;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.view.adapter.PostsPublishedAdapter;
import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.model.ContentCardModel;
import vn.edu.usth.wordpressclient.view.ContentTextEditor;
import vn.edu.usth.wordpressclient.viewmodel.ContentViewModel;


public class PostPublishedFragment extends Fragment {
    Button CreateButton;
    private RecyclerView recyclerView;
    private LinearLayout noPostsMessage;
    private PostsPublishedAdapter adapter;
    private ContentViewModel contentViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_post_published, container, false);

        recyclerView = view.findViewById(R.id.post_published_recycler_view);
        noPostsMessage = view.findViewById(R.id.no_post_screen_published);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_published);

        String domain = DomainManager.getInstance().getSelectedDomain();

        adapter = new PostsPublishedAdapter(getContext(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        contentViewModel = new ViewModelProvider(this).get(ContentViewModel.class);
        refresh();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            refresh();
            swipeRefreshLayout.setRefreshing(false);
        });

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
    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
    public void refresh(){
        String domain = DomainManager.getInstance().getSelectedDomain();
        contentViewModel.fetchContent(domain,"posts","publish");
        contentViewModel.getPublishPostsArrayLiveData().observe(getViewLifecycleOwner(), publishedPostModel -> {
            adapter.setPublishedPost(publishedPostModel);
            if (publishedPostModel.isEmpty()) {
                noPostsMessage.setVisibility(View.VISIBLE);
            } else {
                noPostsMessage.setVisibility(View.INVISIBLE) ;
            }
            adapter.notifyDataSetChanged();
        });
    }
}