package com.william.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;

import com.william.criminalintent.database.CrimeBaseHelper;
import com.william.criminalintent.database.CrimeDbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
//这是单例，比fragment和activity活的久，内存消失才会被销毁，单例其实就是把构造方法设置为私有,视图呈现不出来很可能是没有数据，数据出错了，找bug从源头开始找
public class CrimeLab {
    private static CrimeLab sCrimeLab;
//    private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    //获取单例的方法是使用get，new一个,一个静态的方法里面的变量应该也是静态的
    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab=new CrimeLab(context);
        }
        return sCrimeLab;
    }
    //这个是拿来构建crime列表的
    private CrimeLab(Context context){
        mContext=context.getApplicationContext();
        mDatabase=new CrimeBaseHelper(mContext).getWritableDatabase();
//        mCrimes=new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            Crime crime=new Crime();
//            crime.setTitle("Crime #"+i);
//            crime.setSolved(i%2==0);
//            mCrimes.add(crime);
//        }
    }

    public void updateCrime(Crime crime) {
        String uuidString=crime.getId().toString();
        ContentValues values=getContentValues(crime);
        mDatabase.update(CrimeDbSchema.CrimeTable.NAME,values,CrimeDbSchema.CrimeTable.Cols.UUID+"=?",new String[]{uuidString});
    }
    public void addCrime(Crime crime) {
//        mCrimes.add(crime);
        ContentValues values=getContentValues(crime);
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME,null,values);
    }

    private Cursor queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeDbSchema.CrimeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return cursor;
    }
    //获得列表
    public List<Crime> getCrimes() {
//        return mCrimes;
        return new ArrayList<>();
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values=new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID,crime.getId().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE,crime.getTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE,crime.getDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED,crime.isSolved()?1:0);
        return values;
    }
//    获得单个，根据uuid
    public Crime getCrime(UUID uuid) {
//        for (Crime crime : mCrimes) {
//            if (crime.getId().equals(uuid)) {//使用equals来进行比较值
//                return crime;
//            }
//        }
        return null;
    }
}
