package com.electricsheep.criminalintent.FragmentsPackage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.electricsheep.criminalintent.Crime;
import com.electricsheep.criminalintent.ActivitiesPackage.CrimeCameraActivity;
import com.electricsheep.criminalintent.CrimeLab;
import com.electricsheep.criminalintent.Photo;
import com.electricsheep.criminalintent.PictureUtils;
import com.electricsheep.criminalintent.R;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Adam on 10/8/2014.
 */
public class CrimeFragment extends Fragment {

    private static final String TAG = "CrimeFragment";

    public static final String CRIME_FRAGMENT_LOG_TAG = "CrimeFragment";
    public static final String EXTRA_CRIME_ID = "com.electricsheep.criminalintent.crime_id";
    public static final String DIALOG_DATE = "date";
    public static final String DIALOG_IMAGE = "image";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;
    private static final int REQEUST_CONTACT = 2;

    private Crime mCrime;
    private EditText    mCrimeTitle;
    private Button      mDateButton;
    private CheckBox    mSolvedCheckBox;
    private ImageButton mPhotoButton;
    private ImageView   mPhotoView;
    private Button      mSuspectButton;
    private ImageButton mCallSuspectButton;
    private Callbacks   mCallbacks;

    /**
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private String getCrimeReport(){
        String solvedString = null;
        if(mCrime.getSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }else{
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = android.text.format.DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if(suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }else{
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(),
                dateString, solvedString, suspect);


        return report;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        UUID crimeId = (UUID) args.getSerializable(EXTRA_CRIME_ID);

        //UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);


        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(CRIME_FRAGMENT_LOG_TAG, "Item id: " + item.getItemId());
        Log.d(CRIME_FRAGMENT_LOG_TAG, "R.id.home: " + R.id.home);
        Log.d(CRIME_FRAGMENT_LOG_TAG, "R.id.homeAsUp: " + R.id.homeAsUp);
        Log.d(CRIME_FRAGMENT_LOG_TAG, "Android one: " + android.R.id.home);


        switch(item.getItemId()){
            case android.R.id.home:
                if(NavUtils.getParentActivityName(getActivity()) != null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            if(NavUtils.getParentActivityName(getActivity()) != null){
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        setHasOptionsMenu(true);

        mCrimeTitle = (EditText) v.findViewById(R.id.crime_title);
        mCrimeTitle.setText(mCrime.getTitle());
        mCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                mCrime.setTitle(charSequence.toString());
                mCallbacks.onCrimeUpdated(mCrime);
                getActivity().setTitle(mCrime.getTitle());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.getSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //Set mCrime's mSolved property
                mCrime.setSolved(isChecked);
                mCallbacks.onCrimeUpdated(mCrime);
            }
        });


        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.crime_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Photo photo = mCrime.getPhoto();
                if(photo == null){
                    return;
                }

                FragmentManager fm = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
            }
        });

        //If camera is not available, disable camera functionality
        PackageManager pm = getActivity().getPackageManager();
        boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD &&
                        Camera.getNumberOfCameras() > 0);
        if(!hasCamera){
            mPhotoButton.setEnabled(false);
        }

        Button reportButton = (Button)v.findViewById(R.id.crime_reportButton);
        reportButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        R.string.crime_report_subject);
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        mSuspectButton = (Button)v.findViewById(R.id.crime_suspectButton);
        mSuspectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, REQEUST_CONTACT);

            }
        });

        if(mCrime.getSuspect() != null){
            mSuspectButton.setText(mCrime.getSuspect());
        }

        mCallSuspectButton = (ImageButton)v.findViewById(R.id.crime_callSuspectButton);
        mCallSuspectButton.setEnabled(mCrime.getPhoneNumber() != null);
        mCallSuspectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mCrime.getPhoneNumber() == null){
                    return;
                }else{
                    Uri number = Uri.parse(mCrime.getPhoneNumber());
                    Intent i = new Intent(Intent.ACTION_DIAL,
                            number);
                    startActivity(i);
                }
            }
        });


        
        return v;
    }

    private void showPhoto(){
        //(Re)set the image button's image based on our photo
        Photo photo = mCrime.getPhoto();
        BitmapDrawable bit= null;
        if(photo != null){
            String path = getActivity()
                    .getFileStreamPath(photo.getFilename()).getAbsolutePath();
            bit = PictureUtils.getScaledDrawable(getActivity(), path);

        }
        mPhotoView.setImageDrawable(bit);

    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }

    private void updateDate(){
        DateFormat df = DateFormat.getDateInstance();
        mDateButton.setText(df.format(mCrime.getDate()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_CRIME_DATE);
            mCrime.setDate(date);
            mCallbacks.onCrimeUpdated(mCrime);
            updateDate();
        }else if(requestCode == REQUEST_PHOTO){
            //Create new photo object and attach it to the crime
            String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if(filename != null){
                Log.i(TAG, "Attaching filename: " + filename + " to crime");
                Photo p = new Photo(filename);

                Photo oldPhoto = mCrime.getPhoto();
                if(oldPhoto != null){
                    File dir = getActivity().getFilesDir();
                    File file = new File(dir, oldPhoto.getFilename());
                    file.delete();
                }

                mCrime.setPhoto(p);
                mCallbacks.onCrimeUpdated(mCrime);
                showPhoto();
                Log.i(TAG, "Crime: " + mCrime.getTitle() + " has  a photo");
            }
        }else if(requestCode == REQEUST_CONTACT){
            Uri contactUri = data.getData();

            //Specify which fields you want your query to return
            //values for
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER,
                    ContactsContract.Contacts._ID,
            };

            //Perform the query - the contactUri is like a "where"
            //clause here
            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);

            int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int hasPhoneIndex = c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
            int _idIndex = c.getColumnIndex(ContactsContract.Contacts._ID);

            //Double check that you actually got results
            if(c.getCount() == 0){
                c.close();
                return;
            }

            //Pull out the first column of the first row of data
            //that is your suspects name
            c.moveToFirst();
            String suspect = c.getString(nameIndex);
            mCrime.setSuspect(suspect);
            mSuspectButton.setText(suspect);
            mSuspectButton.setEnabled(true);

            Log.e(TAG, "Checking has phone number");
            if(c.getString(hasPhoneIndex).endsWith("1")){
                mCrime.setPhoneNumber(getPhoneNumber(c.getString(_idIndex)));
            }

            mCallbacks.onCrimeUpdated(mCrime);

            c.close();


        }


    }

    private String getPhoneNumber(String id){

        Log.e(TAG, "Getting phone number with " + id);


        Cursor c = getActivity().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone._ID + "=" + id,
                null, null);

        Log.e(TAG, "Got number of phones: " + c.getCount());

        if(c.getCount() == 0){
            return null;
        }else{
            c.moveToFirst();
            String phoneNumber = c.getString(
                    c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.e(TAG, "PHONE NUMBER: " + phoneNumber);
            if(phoneNumber != null){
                mCallSuspectButton.setEnabled(true);
                return "tel:" +phoneNumber;
            }else{
                mCallSuspectButton.setEnabled(false);
                return null;
            }
        }
    }


    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);

        return crimeFragment;
    }

}
