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
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment {
    public static final String EXTRA_TIME="com.william.criminalintent.time";

    private TimePicker mTimePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {//创建dialog，可以设置时间和位置
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);//需要使用到layoutInflater.from(getActivity)
        mTimePicker= view.findViewById(R.id.dialog_time_picker);
        return new AlertDialog.Builder(getActivity())
                .setView(view)//在一个dialog中设置一个组件view，其中的view可以直接设置为DatePicker
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour=mTimePicker.getHour();
                        int minute=mTimePicker.getMinute();
                        String time;
                        if (minute < 10) {
                            time=hour+":0"+minute;
                        }else{
                            time=hour+":"+minute;
                        }
                        sendResult(Activity.RESULT_OK,time);//传递值并带有请求值和intent
                    }
                })//默认的位置是右下角
                .create();
    }
    //fragment之间进行传值
    private void sendResult(int resultCode, String time) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME,time);//intent进行传值
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
