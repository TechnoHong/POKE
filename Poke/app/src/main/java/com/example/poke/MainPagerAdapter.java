package com.example.poke;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainPagerAdapter extends FragmentStateAdapter {
    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0 ) return new MyInfoActivity();
        else if(position == 1) return new MainRecyclerViewFragment();
        else if(position == 2) return new FridgeFragment();
        else return new SearchFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
