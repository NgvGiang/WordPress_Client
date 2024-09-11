package vn.edu.usth.wordpressclient;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import vn.edu.usth.wordpressclient.MediaFragments.All_Fragment;
import vn.edu.usth.wordpressclient.MediaFragments.Documents_Fragment;
import vn.edu.usth.wordpressclient.MediaFragments.Videos_Fragment;
import vn.edu.usth.wordpressclient.MediaFragments.images_Fragment;

public class WordPress_media extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_press_media);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("WordPress Media");


        tabLayout = findViewById(R.id.PagetabLayout);
        viewPager2 = findViewById(R.id.PageviewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(new All_Fragment(), "ALL");
        viewPagerAdapter.addFragment(new images_Fragment(), "IMAGES");
        viewPagerAdapter.addFragment(new Documents_Fragment(), "DOCUMENTS");
        viewPagerAdapter.addFragment(new Videos_Fragment(), "VIDEOS");
        viewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(viewPagerAdapter.getTitle(position));
        }).attach();
    }
}