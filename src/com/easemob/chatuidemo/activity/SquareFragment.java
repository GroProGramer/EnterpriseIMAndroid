package com.easemob.chatuidemo.activity;

import java.util.ArrayList;
import java.util.List;
import com.enterpriseIM.R;

import wyj.bean.MoodBean;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SquareFragment extends Fragment implements IXListViewListener,OnClickListener{

    private XListView mListView;
    private LinearLayout publish;
    private List<MoodBean> usersMood;
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreateView(inflater, container, savedInstanceState);
        View  view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_conversation_gushi, container,false);
        
        initUI();
        initData();
        return view;
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        
    }

    public void initUI(){
        mListView = (XListView)getView().findViewById(R.id.gushi_xListView);
        mListView.setPullLoadEnable(true);
        publish = (LinearLayout)getView().findViewById(R.id.fabu_gushi);
        publish.setOnClickListener(this);
    }
    
    public void initData(){
        usersMood=new ArrayList<MoodBean>();
        
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch(view.getId()){
          case R.id.fabu_gushi:
              //Intent intent = new Intent(getActivity(), PublishActivity.class);
              //startActivity(intent);
              break;
          default:break;
        }
    }
    
}
