package vn.edu.usth.wordpressclient.view.pages;

import android.content.Intent;
import android.os.Bundle;

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
import vn.edu.usth.wordpressclient.view.ContentTextEditor;
import vn.edu.usth.wordpressclient.R;

import vn.edu.usth.wordpressclient.view.adapter.PagesPublishedAdapter;
import vn.edu.usth.wordpressclient.viewmodel.ContentViewModel;

public class PagePublishedFragment extends Fragment {
    Button PublishedButton;
    private RecyclerView recyclerView;
    private LinearLayout noPagesMessage;
    private PagesPublishedAdapter adapter;
    private ContentViewModel contentViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_published, container, false);

        recyclerView = view.findViewById(R.id.page_recycler_view_published);
        noPagesMessage = view.findViewById(R.id.page_no_post_screen_published);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_published_page);

        String domain = DomainManager.getInstance().getSelectedDomain();

        adapter = new PagesPublishedAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        contentViewModel = new ViewModelProvider(this).get(ContentViewModel.class);
        contentViewModel.getPublishPagesArrayLiveData().observe(getViewLifecycleOwner(), publishedPageModel -> {
            adapter.setPublishedPage(publishedPageModel);
            if (publishedPageModel.isEmpty()) {
                noPagesMessage.setVisibility(View.VISIBLE);
            } else {
                noPagesMessage.setVisibility(View.INVISIBLE) ;
            }
            adapter.notifyDataSetChanged();
        });

        contentViewModel.fetchContent(domain,"pages","publish");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contentViewModel.fetchContent(domain,"pages","publish");
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        PublishedButton = view.findViewById(R.id.published_page_button);
        PublishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ContentTextEditor.class);
                intent.putExtra("endpoint", "pages");
                startActivity(intent);
            }
        });
        return view;
    }
}