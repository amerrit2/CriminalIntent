package com.electricsheep.criminalintent;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.zip.Inflater;

/**
 * Created by Adam on 10/13/2014.
 */
public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_CRIME_DATE = "com.electricsheep.criminalintent.crime_date";
    private Date mDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity()
                .getLayoutInflater()
                .inflate(R.layout.dialog_date, null);


        Bundle args = getArguments();
        mDate  = (Date) args.getSerializable(EXTRA_CRIME_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year  = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day   = calendar.get(Calendar.DAY_OF_MONTH);

        DatePicker datePicker = (DatePicker) v.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i2, int i3) {
                mDate = new GregorianCalendar(i, i2, i3).getTime();
                getArguments().putSerializable(EXTRA_CRIME_DATE, mDate);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle( R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setView(v)
                .create();
    }

    private void sendResult(int resultCode){
        if(getTargetFragment() == null){
            return;
        }

        Intent i = new Intent();
        i.putExtra(EXTRA_CRIME_DATE, mDate);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }


//Adding a comment


    public static DatePickerFragment newInstance(Date date){

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);

        return fragment;

    }
}
