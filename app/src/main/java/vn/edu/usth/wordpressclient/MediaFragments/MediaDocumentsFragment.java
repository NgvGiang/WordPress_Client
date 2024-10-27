package vn.edu.usth.wordpressclient.MediaFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.edu.usth.wordpressclient.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MediaDocumentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MediaDocumentsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media_documents, container, false);
    }
}