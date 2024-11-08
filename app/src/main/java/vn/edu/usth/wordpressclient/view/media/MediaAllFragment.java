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
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.view.adapter.MediaAllAdapter;
import vn.edu.usth.wordpressclient.viewmodel.MediaViewModel;


public class MediaAllFragment extends Fragment {
    private RecyclerView RecyclerView;
    private MediaViewModel mediaViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Button uploadMediaButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media_all, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        String accessToken = SessionManager.getInstance(getContext()).getAccessToken();
        String domain = DomainManager.getInstance().getSelectedDomain();;
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
//
//        mediaViewModel.getUploadSuccessLiveData().observe(getViewLifecycleOwner(), success ->{
//            toolbarProgressBar.setVisibility(View.GONE);
//            if (success) {
//                Snackbar.make(view.findViewById(android.R.id.content),R.string.content_edited_successfully,2500).show();
//                new Handler(Looper.getMainLooper()).postDelayed(getViewLifecycleOwner()::finish, 2500);
//            }
//            else {
//                Snackbar.make(view.findViewById(android.R.id.content), R.string.failed_to_edit_content_please_try_again, Snackbar.LENGTH_SHORT).show();
//            }
//        });
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