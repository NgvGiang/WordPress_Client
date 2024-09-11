package vn.edu.usth.wordpressclient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

public class AppSettingsFragment extends Fragment {

    private Switch optimizeImagesSwitch, removeLocationSwitch, optimizeVideosSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_app_settings, container, false);

        optimizeImagesSwitch = view.findViewById(R.id.switch_optimize_images);
        removeLocationSwitch = view.findViewById(R.id.switch_remove_location);
        optimizeVideosSwitch = view.findViewById(R.id.switch_optimize_videos);

        optimizeImagesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String message = isChecked ? "Optimize Images enabled" : "Optimize Images disabled";
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}