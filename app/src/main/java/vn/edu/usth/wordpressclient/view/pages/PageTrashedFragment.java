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
import vn.edu.usth.wordpressclient.view.adapter.PagesTrashedAdapter;
import vn.edu.usth.wordpressclient.viewmodel.ContentViewModel;

public class PageTrashedFragment extends Fragment {
    Button TrashedButton;
    private RecyclerView recyclerView;
    private LinearLayout noPagesMessage;
    private PagesTrashedAdapter adapter;
    private ContentViewModel contentViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_page_trashed, container, false);

        recyclerView = view.findViewById(R.id.page_recycler_view_trashed);
        noPagesMessage = view.findViewById(R.id.page_no_post_screen_trashed);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_trashed_page);

        String domain = DomainManager.getInstance().getSelectedDomain();

        adapter = new PagesTrashedAdapter(getContext(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        contentViewModel = new ViewModelProvider(this).get(ContentViewModel.class);
        refresh();


        swipeRefreshLayout.setOnRefreshListener(() -> {
            refresh();
            swipeRefreshLayout.setRefreshing(false);
        });


        TrashedButton = view.findViewById(R.id.trashed_page_button);
        TrashedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ContentTextEditor.class);
                intent.putExtra("endpoint", "pages");
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
        contentViewModel.fetchContent(domain,"pages","trash");
        contentViewModel.getTrashedPagesArrayLiveData().observe(getViewLifecycleOwner(), trashPageModel -> {
            adapter.setTrashedPage(trashPageModel);
            if (trashPageModel.isEmpty()) {
                noPagesMessage.setVisibility(View.VISIBLE);
            } else {
                noPagesMessage.setVisibility(View.INVISIBLE);
            }
            adapter.notifyDataSetChanged();
        });
    }
}