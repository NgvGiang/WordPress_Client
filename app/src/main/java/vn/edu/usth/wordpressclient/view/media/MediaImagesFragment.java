package vn.edu.usth.wordpressclient.view.media;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.view.adapter.MediaImagesAdapter;
import vn.edu.usth.wordpressclient.viewmodel.MediaViewModel;


public class MediaImagesFragment extends Fragment {
    private androidx.recyclerview.widget.RecyclerView RecyclerView;
    private MediaViewModel mediaViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media_images, container, false);
        String accessToken = SessionManager.getInstance(getContext()).getAccessToken();
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        String domain = DomainManager.getInstance().getSelectedDomain();

        RecyclerView = view.findViewById(R.id.MediaImageRecyclerView);
        RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MediaImagesAdapter adapter = new MediaImagesAdapter(getContext());
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
}