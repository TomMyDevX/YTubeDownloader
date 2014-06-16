package com.tools.tommydev.videofinder.Adapter;

/**
 * Created by TomMy on 9/5/13.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.List;

/**
 * The <code>PagerAdapterX</code> serves the fragments when paging.
 * @author mwho
 */
public class PagerAdapterX extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    /**
     * @param fm
     * @param fragments
     */
    public PagerAdapterX(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
     */
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.PagerAdapterX#getCount()
     */
    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }


}