package vn.edu.usth.wordpressclient;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import vn.edu.usth.wordpressclient.MediaFragments.All_Media_Fragment;
import vn.edu.usth.wordpressclient.MediaFragments.Documents_Media_Fragment;
import vn.edu.usth.wordpressclient.MediaFragments.Videos_Media_Fragment;
import vn.edu.usth.wordpressclient.MediaFragments.images_Media_Fragment;

public class WordPress_media extends AppCompatActivity {

    private TabLayout MediatabLayout;
    private ViewPager2 MediaviewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_press_media);

        Toolbar toolbar = findViewById(R.id.Mediatoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("WordPress Media");


        MediatabLayout = findViewById(R.id.MediatabLayout);
        MediaviewPager2 = findViewById(R.id.MediaviewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(new All_Media_Fragment(), "ALL");
        viewPagerAdapter.addFragment(new images_Media_Fragment(), "IMAGES");
        viewPagerAdapter.addFragment(new Documents_Media_Fragment(), "DOCUMENTS");
        viewPagerAdapter.addFragment(new Videos_Media_Fragment(), "VIDEOS");
        MediaviewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(MediatabLayout, MediaviewPager2, (tab, position) -> {
            tab.setText(viewPagerAdapter.getTitle(position));
        }).attach();
    }
}