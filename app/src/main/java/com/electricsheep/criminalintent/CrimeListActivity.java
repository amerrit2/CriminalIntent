package com.electricsheep.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Adam on 10/11/2014.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

        Fragment fragment = new CrimeListFragment();

        return fragment;
    }
}
