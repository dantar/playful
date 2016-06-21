package it.dantar.barcodehunt.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HuntPageAdapter extends FragmentPagerAdapter {

	public HuntPageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment;
		if (i == 0) {
			fragment = new ItemsFragment();
		} else {
			fragment = new EventsFragment();
		}
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt("pagenum", i);
        fragment.setArguments(args);
		return fragment ;
	}

	@Override
	public int getCount() {
		return 2;
	}

}
