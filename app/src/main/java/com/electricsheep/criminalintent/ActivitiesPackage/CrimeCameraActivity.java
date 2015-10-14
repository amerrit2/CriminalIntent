package com.electricsheep.criminalintent.ActivitiesPackage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.electricsheep.criminalintent.FragmentsPackage.CrimeCameraFragment;

/**
 * Created by Adam on 9/12/2015.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Hide the status bar and other OS-level chrome
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);


    }

    @Override
    protected Fragment createFragment(){
        return new CrimeCameraFragment();
    }
}
