package com.william.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
//R文件报错一般是xml有问题,有多少个activity就有多少个页面，其实activity就是拿来创建fragment的
public class CrimeListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE="subtitle";
    private RecyclerView mCrimeRecyclerView;//可以移动列表位置
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override//fragment中需要创建视图，就是找到那个视图
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_crime_list,container,false);
        mCrimeRecyclerView=view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//需要使用LayoutManager来进行协助

        if (savedInstanceState != null) {
            mSubtitleVisible=savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);//保存状态
        }
        updateUI();//创建adapter，同时创建了crimeList
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUI();//更新数据
    }
    //ViewHolder是处理视图的，可以添加点击事件
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;
        public CrimeHolder(LayoutInflater inflater,ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime,parent,false));//创建小视图
            itemView.setOnClickListener(this);//调用的时候就进行设置点击事件

            mTitleTextView=itemView.findViewById(R.id.crime_title);//一个crime对应一个crimeHolder
            mDateTextView=itemView.findViewById(R.id.crime_date);
            mSolvedImageView=itemView.findViewById(R.id.crime_solved);
        }
        //绑定新数据
        public void bind(Crime crime) {
            mCrime=crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedImageView.setVisibility(crime.isSolved()?View.VISIBLE:View.GONE);
        }

        @Override
        public void onClick(View v) {
            //toast的参数有上下文、文字、时间长度
//            Toast.makeText(getActivity(), mCrime.getTitle()+" clicked", Toast.LENGTH_SHORT).show();
//            Intent intent = CrimeActivity.newIntent(getActivity(),mCrime.getId());
            Intent intent=CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
            startActivity(intent);
        }
    }
    //Adapter是用来模型和视图结合，这个地方也就是viewHolder和Crime,他有三个方法需要实现，构造方法中传入数据，继承的类型是viewHolder
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{//其中的参数是单个item视图viewHolder
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            this.mCrimes = crimes;
        }
        //调用构造方法后会调用以下的方法
        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {//首先是建立viewHolder
            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater,viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder crimeHolder, int i) {
            Crime crime=mCrimes.get(i);
            crimeHolder.bind(crime);//其实是viewHolder进行绑定数据
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    private void updateUI() {
        CrimeLab crimeLab=CrimeLab.get(getActivity());
        List<Crime> crimes=crimeLab.getCrimes();
        if (mAdapter == null) {
            mAdapter=new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();//Adapter有监听数据是否发生改变的的方法
        }

        updateSubtitle();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);

        MenuItem subtitleItem=menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Crime crime=new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent=CrimePagerActivity.newIntent(getActivity(),crime.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible=!mSubtitleVisible;
                getActivity().invalidateOptionsMenu();//重建菜单项
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        CrimeLab crimeLab=CrimeLab.get(getActivity());
        int crimeCount=crimeLab.getCrimes().size();
//        String subtitle=getString(R.string.subtitle_format,crimeCount);//getString是为了格式化
        String subtitle=getResources().getQuantityString(R.plurals.subtitle_plural,crimeCount,crimeCount);//处理字符串的复数问题
        if (!mSubtitleVisible) {
            subtitle=null;
        }
        AppCompatActivity activity=(AppCompatActivity)getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);//副标题
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }
}
