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

import java.util.ArrayList;

import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.view.adapter.PostsScheduledAdapter;
import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.model.ContentCardModel;
import vn.edu.usth.wordpressclient.view.ContentTextEditor;
import vn.edu.usth.wordpressclient.viewmodel.ContentViewModel;


public class PostScheduledFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayout noPostsMessage;
    private PostsScheduledAdapter adapter;
    private ContentViewModel contentViewModel;
    Button CreateButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_scheduled, container, false);

        recyclerView = view.findViewById(R.id.post_scheduled_recycler_view);
        noPostsMessage = view.findViewById(R.id.no_post_screen_scheduled);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_scheduled);

        String domain = DomainManager.getInstance().getSelectedDomain();

        adapter = new PostsScheduledAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        contentViewModel = new ViewModelProvider(this).get(ContentViewModel.class);
        contentViewModel.getScheduledPostsArrayLiveData().observe(getViewLifecycleOwner(), schedulePostModel -> {
                    adapter.setSchedulePost(schedulePostModel);
                    if (schedulePostModel.isEmpty()) {
                        noPostsMessage.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        noPostsMessage.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
            adapter.notifyDataSetChanged();
        });

        contentViewModel.fetchContent(domain,"posts","schedule");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contentViewModel.fetchContent(domain,"posts","schedule");
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        CreateButton = view.findViewById(R.id.scheduled_post_button);
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
        String domain = DomainManager.getInstance().getSelectedDomain();
        contentViewModel.fetchContent(domain,"posts","future");
        adapter.notifyDataSetChanged();
    }
}