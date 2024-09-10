package vn.edu.usth.wordpressclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new All_Fragment();
            case 1:
                return new images_Fragment();
            case 2:
                return new Documents_Fragment();
            case 3:
                return new Videos_Fragment();
            default:
                return new All_Fragment();

        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        String title = "";
        switch (position){
            case 0:
                title = "All";
                break;
            case 1:
                title = "Images";
                break;
            case 2:
                title = "Documents";
                break;
            case 3:
                title = "Videos";
                break;
        }
        return title;
    }


}
