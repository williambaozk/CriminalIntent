package com.william.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE=0;
    private static final int REQUEST_TIME=1;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;//一般的报错原因是list没有实例化,没有捕获异常
    //为了封装到fragment
    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();//主要是bundle，意思就是fragment可以设置setArgument()参数是bundle，主要是为了封装
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mCrime=new Crime();

//        UUID crimeId=(UUID)getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        UUID crimeId=(UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime=CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_crime,container,false);

        mTitleField=v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        //editText可以添加内容改变监听器，参数是TextWatcher()
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDateButton=v.findViewById(R.id.crime_date);
        updateDate();
//        mDateButton.setEnabled(false);//没法选择时就设置为setEnabled(false)
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=getFragmentManager();
//                DatePickerFragment dialog=new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);//在这个地方设置是为了对方传递过来，需要fragment进行设置
                dialog.show(manager,DIALOG_DATE);//将dialog呈现出来.fragment有一个show函数
            }
        });

        mTimeButton=v.findViewById(R.id.crime_time);
        initTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=getFragmentManager();
                TimePickerFragment dialogTime=new TimePickerFragment();
                dialogTime.setTargetFragment(CrimeFragment.this,REQUEST_TIME);
                dialogTime.show(manager,DIALOG_TIME);
            }
        });
        mSolvedCheckBox=v.findViewById(R.id.crime_solved);//表单类的是change
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);//checkBox有isChecked的参数
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date=(Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
        if (requestCode == REQUEST_TIME) {
            String time=data.getStringExtra(TimePickerFragment.EXTRA_TIME);
            mTimeButton.setText(time);
        }
    }

    private void updateDate() {
        SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
        mDateButton.setText(fmt.format(mCrime.getDate()));
    }

    private void initTime() {
        SimpleDateFormat fmt=new SimpleDateFormat("HH:mm");
        mTimeButton.setText(fmt.format(new Date()));
    }
}
