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

import vn.edu.usth.wordpressclient.view.adapter.PagesScheduledAdapter;
import vn.edu.usth.wordpressclient.viewmodel.ContentViewModel;

public class PageScheduledFragment extends Fragment {
    Button ScheduledButton;
    private RecyclerView recyclerView;
    private LinearLayout noPagesMessage;
    private PagesScheduledAdapter adapter;
    private ContentViewModel contentViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_scheduled, container, false);

        recyclerView = view.findViewById(R.id.page_recycler_view_scheduled);
        noPagesMessage = view.findViewById(R.id.page_no_post_screen_scheduled);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_scheduled_page);

        String domain = DomainManager.getInstance().getSelectedDomain();

        adapter = new PagesScheduledAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        contentViewModel = new ViewModelProvider(this).get(ContentViewModel.class);
        contentViewModel.getScheduledPagesArrayLiveData().observe(getViewLifecycleOwner(), scheduledPageModel -> {
            adapter.setSchedulePage(scheduledPageModel);
            if (scheduledPageModel.isEmpty()) {
                noPagesMessage.setVisibility(View.VISIBLE);
            } else {
                noPagesMessage.setVisibility(View.INVISIBLE) ;
            }
            adapter.notifyDataSetChanged();
        });

        contentViewModel.fetchContent(domain,"pages","schedule");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contentViewModel.fetchContent(domain,"pages","schedule");
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        ScheduledButton = view.findViewById(R.id.schedule_page_button);
        ScheduledButton.setOnClickListener(new View.OnClickListener() {
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