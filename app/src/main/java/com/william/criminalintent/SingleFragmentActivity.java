package com.william.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();//要创建多个fragment时，可以使用抽象类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//每个fragment和activity都需要首先创建状态
        setContentView(R.layout.activity_fragment);

        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.fragment_container);//一个FrameLayout可以包含很多fragment

        if (fragment == null) {
            fragment=createFragment();//abstract的类可以直接使用没实现的方法
            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment)//参数是存放fragment的那个xml，再将fragment添加到fragment_container对应的fragmentManager
                    .commit();
        }
    }
}
