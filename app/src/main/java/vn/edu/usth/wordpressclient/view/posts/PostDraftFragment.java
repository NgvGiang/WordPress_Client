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

import vn.edu.usth.wordpressclient.view.adapter.PostsDraftAdapter;
import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.view.ContentTextEditor;
import vn.edu.usth.wordpressclient.viewmodel.ContentViewModel;

public class PostDraftFragment extends Fragment {
    Button CreateButton;
    private RecyclerView recyclerView;
    private LinearLayout noPostsMessage;
    private PostsDraftAdapter adapter;
    private ContentViewModel contentViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_draft, container, false);

        recyclerView = view.findViewById(R.id.post_recycler_view);
        noPostsMessage = view.findViewById(R.id.no_post_screen);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        String domain = DomainManager.getInstance().getSelectedDomain();

        adapter = new PostsDraftAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        contentViewModel = new ViewModelProvider(this).get(ContentViewModel.class);
        contentViewModel.getDraftPostsArrayLiveData().observe(getViewLifecycleOwner(), draftPostModel -> {
            adapter.setDraftPost(draftPostModel);
            if (draftPostModel.isEmpty()) {
                noPostsMessage.setVisibility(View.VISIBLE);
            } else {
                noPostsMessage.setVisibility(View.INVISIBLE) ;
            }
            adapter.notifyDataSetChanged();
        });

        contentViewModel.fetchContent(domain,"posts","draft");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contentViewModel.fetchContent(domain,"posts","draft");
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

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

    @Override
    public void onResume() {
        super.onResume();
        String domain = DomainManager.getInstance().getSelectedDomain();
        contentViewModel.fetchContent(domain,"posts","draft");
        adapter.notifyDataSetChanged();
    }
}
