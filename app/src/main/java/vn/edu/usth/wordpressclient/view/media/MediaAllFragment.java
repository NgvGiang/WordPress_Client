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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media_all, container, false);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        String accessToken = SessionManager.getInstance(getContext()).getAccessToken();
        String domain = DomainManager.getInstance().getSelectedDomain();;

        RecyclerView = view.findViewById(R.id.MediaAllRecyclerView);
        RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MediaAllAdapter adapter = new MediaAllAdapter(getContext());
        RecyclerView.setAdapter(adapter);
        mediaViewModel = new ViewModelProvider(this).get(MediaViewModel.class);
        mediaViewModel.getMediaModelsLiveData().observe(getViewLifecycleOwner(), mediaModels -> {
            adapter.setMediaUrls(mediaModels);
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


//        mediaUrls = new ArrayList<MediaCardModel>();
//        mediaUrls.add(new MediaCardModel("https://darkhent.wordpress.com/wp-content/uploads/2024/10/verybigimage.jpg"));
//        mediaUrls.add(new MediaCardModel("https://darkhent.wordpress.com/wp-content/uploads/2024/10/440368991_122207255042004730_9005723163750922673_n.jpg"));
//        mediaUrls.add(new MediaCardModel("https://darkhent.wordpress.com/wp-content/uploads/2024/10/yysagvvr.jpg"));
//        mediaUrls.add(new MediaCardModel("https://darkhent.wordpress.com/wp-content/uploads/2024/10/screenshot-2024-03-06-122913.png"));
//        mediaUrls.add(new MediaCardModel("https://darkhent.wordpress.com/wp-content/uploads/2024/10/454459059_1068935178132807_5855740790364600547_n.jpg"));
//        mediaUrls.add(new MediaCardModel("https://darkhent.wordpress.com/wp-content/uploads/2024/10/screenshot-2024-03-06-122913.png"));
//

//        adapter.setMediaUrls(mediaUrls);
        return view;
    }
}