package vn.edu.usth.wordpressclient.view.media;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.view.adapter.MediaAllAdapter;
import vn.edu.usth.wordpressclient.viewmodel.MediaViewModel;


public class MediaAllFragment extends Fragment {
    private RecyclerView RecyclerView;
    private MediaViewModel mediaViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media_all, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        String accessToken = SessionManager.getInstance(getContext()).getAccessToken();
        String domain = DomainManager.getInstance().getSelectedDomain();;

        RecyclerView = view.findViewById(R.id.MediaAllRecyclerView);
        RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MediaAllAdapter adapter = new MediaAllAdapter(getContext());
        RecyclerView.setAdapter(adapter);
        mediaViewModel = new ViewModelProvider(this).get(MediaViewModel.class);
        mediaViewModel.getMediaModelsLiveData().observe(getViewLifecycleOwner(), mediaModels -> {
            adapter.setMediaUrls(mediaModels);
            if(mediaModels.isEmpty()){
                view.findViewById(R.id.dont_have_media).setVisibility(View.VISIBLE);
            }else{
                view.findViewById(R.id.dont_have_media).setVisibility(View.INVISIBLE);
            }
            adapter.notifyDataSetChanged();
        });

        mediaViewModel.fetchMediaUrls(accessToken, domain);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mediaViewModel.fetchMediaUrls(accessToken, domain);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mediaViewModel.fetchMediaUrls(SessionManager.getInstance(getContext()).getAccessToken(), DomainManager.getInstance().getSelectedDomain());
        RecyclerView.getAdapter().notifyDataSetChanged();
    }
}