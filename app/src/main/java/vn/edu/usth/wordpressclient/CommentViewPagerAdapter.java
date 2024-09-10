package vn.edu.usth.wordpressclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import vn.edu.usth.wordpressclient.commentgroupfragments.AllTabFragment;
import vn.edu.usth.wordpressclient.commentgroupfragments.ApprovedTabFragment;
import vn.edu.usth.wordpressclient.commentgroupfragments.PendingTabFragment;
import vn.edu.usth.wordpressclient.commentgroupfragments.SpamTabFragment;
import vn.edu.usth.wordpressclient.commentgroupfragments.TrashedTabFragment;
import vn.edu.usth.wordpressclient.commentgroupfragments.UnrepliedTabFragment;

public class CommentViewPagerAdapter extends FragmentStateAdapter {
    public CommentViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AllTabFragment();
            case 1:
                return new PendingTabFragment();
            case 2:
                return new UnrepliedTabFragment();
            case 3:
                return new ApprovedTabFragment();
            case 4:
                return new SpamTabFragment();
            case 5:
                return new TrashedTabFragment();
            default:
                return new AllTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
