package vn.edu.usth.wordpressclient.view.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ArrayList<String> titleArrayList =  new ArrayList<>();
    String domain;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String domain) {
        super(fragmentActivity);
        this.domain = domain;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = fragmentArrayList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("domain", domain);
        fragment.setArguments(bundle);
        return fragment;
    }


    public CharSequence getTitle(int position){
        return titleArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment, String title){
        fragmentArrayList.add(fragment);
        titleArrayList.add(title);
    }
}
