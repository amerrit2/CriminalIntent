package com.electricsheep.criminalintent.ActivitiesPackage;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.electricsheep.criminalintent.Crime;
import com.electricsheep.criminalintent.FragmentsPackage.CrimeFragment;
import com.electricsheep.criminalintent.FragmentsPackage.CrimeListFragment;
import com.electricsheep.criminalintent.R;

/**
 * Created by Adam on 10/11/2014.
 */
public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks , CrimeFragment.Callbacks{

    @Override
    public void onCrimeUpdated(Crime crime) {
        FragmentManager fm = getSupportFragmentManager();
        CrimeListFragment listFragment = (CrimeListFragment)
                fm.findFragmentById(R.id.fragmentContainer);
        listFragment.updateUI();
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if(findViewById(R.id.detailFragmentContainer) == null){
            //Start CrimePagerActivity
            Intent i = new Intent(this, CrimePagerActivity.class);
            i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
            startActivity(i);
        }else{
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());

            if(oldDetail != null){
                ft.remove(oldDetail);
            }

            ft.add(R.id.detailFragmentContainer, newDetail);
            ft.commit();


        }
    }

    @Override
    protected Fragment createFragment() {

        Fragment fragment = new CrimeListFragment();

        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_masterdetail;
    }
}
