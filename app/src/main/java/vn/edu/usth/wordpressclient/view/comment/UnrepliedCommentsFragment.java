package vn.edu.usth.wordpressclient.view.comment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.json.JSONException;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.view.adapter.CommentAllAdapter;
import vn.edu.usth.wordpressclient.viewmodel.CommentViewModel;
import vn.edu.usth.wordpressclient.viewmodel.UserViewModel;

public class UnrepliedCommentsFragment extends Fragment {
    private RecyclerView recyclerView;
    private CommentViewModel commentViewModel;
    private UserViewModel userViewModel;
    private CommentAllAdapter adapter;
    private RelativeLayout noUnreplyComment;
    private String domain;
    private String accessToken;
    int authorId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unreplied_comments, container, false);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_unreplied_layout);
        String accessToken = SessionManager.getInstance(getContext()).getAccessToken();
        String domain = DomainManager.getInstance().getSelectedDomain();
        noUnreplyComment = view.findViewById(R.id.no_unreply_comment);

        recyclerView = view.findViewById(R.id.fragment_unreplied_comments_rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CommentAllAdapter(getContext());
        recyclerView.setAdapter(adapter);
        commentViewModel = new ViewModelProvider(requireActivity()).get(CommentViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserInfoLiveData().observe(getViewLifecycleOwner(), jsonObject -> {
            try {
                authorId = jsonObject.getInt("ID");
                getComments();
                commentViewModel.getUnrepliedComment(authorId);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        userViewModel.getUserInfo(accessToken);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentViewModel.getUnrepliedComment(authorId);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    public void getComments() {
        Log.i("authorId", "" + authorId);
        commentViewModel.getUnrepliedCommentModelsLiveData().observe(getViewLifecycleOwner(), commentModels -> {
            adapter.setCommentCardModels(commentModels);
            if (commentModels.isEmpty()) {
                noUnreplyComment.setVisibility(View.VISIBLE);
            } else {
                noUnreplyComment.setVisibility(View.INVISIBLE);
            }
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        commentViewModel.getUnrepliedComment(authorId);
        adapter.notifyDataSetChanged();
    }
}