package vn.edu.usth.wordpressclient.view.media;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

import vn.edu.usth.wordpressclient.R;


public class MediaVideosFragment extends Fragment {
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media_videos, container, false);
        button = view.findViewById(R.id.uploadMediaButton);
        button.setOnClickListener(v -> {
            Snackbar.make(view,R.string.no_money,Snackbar.LENGTH_SHORT).show();
        });
        return view;
    }
}