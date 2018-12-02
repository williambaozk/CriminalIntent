package com.william.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {//要用到一个单独的fragment
    public static final String EXTRA_DATE="com.william.criminalintent.date";
    private static final String ARG_DATE = "date";

    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Date date) {//是为了在fragment之间传递值，newInstance
        Bundle args = new Bundle();//bundle就是用来传参的,里面放置需要传递的参数
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);//setArgument(bundle)
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {//创建dialog，可以设置时间和位置
        Date date=(Date)getArguments().getSerializable(ARG_DATE);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);//calendar是拿来设置时间的
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date,null);//需要使用到layoutInflater.from(getActivity)
        mDatePicker= view.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year,month,day,null);
        return new AlertDialog.Builder(getActivity())
                .setView(view)//在一个dialog中设置一个组件view，其中的view可以直接设置为DatePicker
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year=mDatePicker.getYear();
                        int month=mDatePicker.getMonth();
                        int day=mDatePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year,month,day).getTime();//点击以后设置时间以后再返回
                        sendResult(Activity.RESULT_OK,date);//传递值并带有请求值和intent
                    }
                })//默认的位置是右下角
                .create();
    }
    //fragment之间进行传值
    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);//intent进行传值
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
