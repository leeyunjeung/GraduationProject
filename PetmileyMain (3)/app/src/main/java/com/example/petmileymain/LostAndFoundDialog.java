package com.example.petmileymain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class LostAndFoundDialog extends DialogFragment {

    private Button search;
    private Spinner spinnerLocal;
    private Spinner spinnerType;
    private Spinner spinnerMF;
    private EditText start_date;
    private EditText end_date;
    private View view;

    private Context context;

    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar2 = Calendar.getInstance();



    final DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    final DatePickerDialog.OnDateSetListener myDatePicker2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar2.set(Calendar.YEAR, year);
            myCalendar2.set(Calendar.MONTH, month);
            myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel2();
        }
    };

    public interface OnCompleteListener{
        void onInputedData(String local,String type,String mf,String start,String end);
    }

    private OnCompleteListener mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnCompleteListener) activity;
        }
        catch (ClassCastException e) {
            Log.d("DialogFragmentExample", "Activity doesn't implement the OnCompleteListener interface");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.lostandfound_search, null);
        builder.setView(view);

        search = (Button) view.findViewById(R.id.btnSearch);
        spinnerLocal = (Spinner) view.findViewById(R.id.searchLocal);
        spinnerType = (Spinner) view.findViewById(R.id.searchType);
        spinnerMF = (Spinner) view.findViewById(R.id.searchMF);
        start_date = (EditText) view.findViewById(R.id.start_date);
        end_date = (EditText) view.findViewById(R.id.end_date);

        start_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
        String end = mFormat.format(date);

        end_date.setText(end);

        Calendar mon = Calendar.getInstance();
        mon.add(Calendar.MONTH , -1);
        String beforeMonth = new java.text.SimpleDateFormat("yyyy-MM-dd").format(mon.getTime());

        start_date.setText(beforeMonth);


        end_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), myDatePicker2, myCalendar2.get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH), myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String local = spinnerLocal.getSelectedItem().toString();
                String type = spinnerType.getSelectedItem().toString();
                String MF = spinnerMF.getSelectedItem().toString();
                String start = start_date.getText().toString();
                String end = end_date.getText().toString();


                dismiss();
                mCallback.onInputedData(local,type,MF,start,end);
            }
        });

        return builder.create();
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        start_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel2() {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        end_date.setText(sdf.format(myCalendar2.getTime()));
    }
}
