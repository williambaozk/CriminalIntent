package com.william.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
//这是单例，比fragment和activity活的久，内存消失才会被销毁，单例其实就是把构造方法设置为私有,视图呈现不出来很可能是没有数据，数据出错了，找bug从源头开始找
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;
    //获取单例的方法是使用get，new一个,一个静态的方法里面的变量应该也是静态的
    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab=new CrimeLab(context);
        }
        return sCrimeLab;
    }
    //这个是拿来构建crime列表的
    private CrimeLab(Context context){
        mCrimes=new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Crime crime=new Crime();
            crime.setTitle("Crime #"+i);
            crime.setSolved(i%2==0);
            mCrimes.add(crime);
        }
    }
    //获得列表
    public List<Crime> getCrimes() {
        return mCrimes;
    }
//    获得单个，根据uuid
    public Crime getCrime(UUID uuid) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(uuid)) {//使用equals来进行比较值
                return crime;
            }
        }
        return null;
    }
}
