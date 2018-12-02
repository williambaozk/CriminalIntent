package com.william.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private static final String EXTRA_CRIME_ID =
            "com.william.criminalintent.crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    //activity之间传输，使用为public的好处是可以在其他的类里面进行调用
    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent=new Intent(context,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        //获取这个的原因是需要是判断用户点击了第几个crime
        UUID crimeId=(UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);//如果在获取intent中的传值是报错，那么很可能是由于没有转换
        mViewPager=findViewById(R.id.crime_view_pager);//viewPager也是一个view
        mCrimes=CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager=getSupportFragmentManager();
//        mViewPager.setPageMargin(16);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {//是代理，工作就是返回fragment给pagerActivity

            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);//其实就是创建一个个的fragment
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);//如果你想从当前点击的crime开始，那么就使用viewPager.setCurrentItem();
                break;
            }
        }
    }
}
