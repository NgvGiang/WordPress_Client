package vn.edu.usth.wordpressclient.view.media;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.view.adapter.MediaImagesAdapter;
import vn.edu.usth.wordpressclient.viewmodel.MediaViewModel;


public class MediaImagesFragment extends Fragment {
    private androidx.recyclerview.widget.RecyclerView RecyclerView;
    private MediaViewModel mediaViewModel;
    private Button uploadMediaButton;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media_images, container, false);

        String accessToken = SessionManager.getInstance(getContext()).getAccessToken();
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        String domain = DomainManager.getInstance().getSelectedDomain();
        uploadMediaButton = view.findViewById(R.id.uploadMediaButton);
        mediaViewModel = new ViewModelProvider(this).get(MediaViewModel.class);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri fileUri = result.getData().getData();
                        String authToken = "Bearer " + SessionManager.getInstance(getContext()).getAccessToken();
                        View rootView = view.findViewById(android.R.id.content);
                        // Gọi hàm upload ảnh lên WordPress
                        mediaViewModel.uploadImage(fileUri, authToken, rootView);

                    }
                }
        );

        uploadMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                imagePickerLauncher.launch(intent);
            }
        });
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
    @Override
    public void onResume() {
        super.onResume();
        mediaViewModel.fetchMediaUrls(SessionManager.getInstance(getContext()).getAccessToken(), DomainManager.getInstance().getSelectedDomain());
        RecyclerView.getAdapter().notifyDataSetChanged();
    }
}