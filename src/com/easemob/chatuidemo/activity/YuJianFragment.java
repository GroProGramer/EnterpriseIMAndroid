package com.easemob.chatuidemo.activity;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import wyj.bean.User;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.radar.RadarNearbySearchOption;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchListener;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.adapter.YuJianAdapter1;
import com.enterpriseIM.R;

public class YuJianFragment extends Fragment implements IXListViewListener,RadarSearchListener,BDLocationListener,OnClickListener,OnItemClickListener{

    RadarSearchManager mManager=null;
    LatLng pt=null;
    public static final int FRIST_GET_DATE = 0x1;
    public static final int REFRESH_GET_DATE = 0x2;
    public static final int LOADMORE_GET_DATE = 0x3;
    private static List<User> userList;
    private static ProgressDialog pd = null ;
    private static boolean progressShow = false;//进度条是否显示
    private LinearLayout pengpeng;
    private LinearLayout screenLl;
    private XListView mListView;
    private YuJianAdapter1 mAdapter;
    private boolean firstLoad=true;
    private TextView screenTv;
    private BDLocation location;
    private LocationClient locationClient;
    private LocationClientOption locationoption;
    
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreateView(inflater, container, savedInstanceState);
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view=inflater.inflate(R.layout.fragment_conversation_zaina, container,false);
        mListView = (XListView)view.findViewById(R.id.xListView);
        mListView.setPullLoadEnable(true);
        
        //pengpeng = (LinearLayout)view.findViewById(R.id.pengpeng);
        screenLl = (LinearLayout)view.findViewById(R.id.screenLl);
        //screenTv=(TextView)view.findViewById(R.id.screen);
        screenLl.setOnClickListener(this);
        userList=new ArrayList<User>();
        loadUsers();
        //mAdapter = new YuJianAdapter(getActivity(), R.layout.zaina_list_item, userList);
        mAdapter = new YuJianAdapter1(getActivity(),  userList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return view;
    }
    
    

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

       
    }



    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        uploadOnce();
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        
    }
    
    private void loadUsers() {
        
        mManager = RadarSearchManager.getInstance();
        mManager.addNearbyInfoListener(this);
      
        Log.v("baidu","百度地图初始化成功");
        locationClient = new LocationClient(getActivity());
        locationoption = new LocationClientOption();
        locationoption.setOpenGps(true); // 是否打开GPS
        locationoption.setCoorType("bd09ll"); 
        locationoption.setPoiExtraInfo(true);
        locationoption.setAddrType("all");
        locationoption.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先,不设置，默认是gps优先
        locationoption.setPoiNumber(10);
        locationoption.setScanSpan(10000);//设置定位频率
        locationClient.setLocOption(locationoption);
        locationClient.registerLocationListener(this);
       
        if(!locationClient.isStarted()){
            locationClient.start();
        }
        locationClient.requestLocation();
        //locationClient.requestPoi();
    }
    



    @Override
    public void onGetClearInfoState(RadarSearchError arg0) {
        // TODO Auto-generated method stub
        
    }



    @Override
    public void onGetNearbyInfoList(RadarNearbyResult result,
            RadarSearchError error) {
        // TODO Auto-generated method stub
        if (error == RadarSearchError.RADAR_NO_ERROR) {
            /*Toast.makeText(getActivity(), "查询周边成功", Toast.LENGTH_LONG)
                .show();*/
            //获取成功，处理数据
            Log.v("baidu","查询周边成功："+result);
            if(result.infoList.size()==0){
                Toast.makeText(getActivity(), "附近没有其他用户", Toast.LENGTH_LONG).show();
                return;
            }
            userList.clear();
            for(int i=0;i<result.infoList.size();i++){
                User user=new User();
                user.setUser_id(result.infoList.get(i).userID);
                userList.add(user);
            }
            Log.v("baidu","初始化userList成功");
            mAdapter.notifyDataSetChanged();
        } else {
            //获取失败
            Toast.makeText(getActivity(), "查询周边失败", Toast.LENGTH_LONG)
                .show();
            Log.v("baidu","查询周边失败："+result);
        }
    }



    @Override
    public void onGetUploadState(RadarSearchError error) {
        // TODO Auto-generated method stub
        if (error == RadarSearchError.RADAR_NO_ERROR) {
            //上传成功
           /*Toast.makeText(getActivity(), "单次上传位置成功", Toast.LENGTH_LONG)
                    .show();*/
            Log.v("baidu","上传位置成功");
            LatLng p=pt;    
            RadarNearbySearchOption option = new RadarNearbySearchOption().centerPt(pt).radius(2000);
            boolean b=mManager.nearbyInfoRequest(option);
           
           // Log.v("baidu","请求周边用户信息结果："+b);
        } else {
            //上传失败
            /*Toast.makeText(getActivity(), "单次上传位置失败", Toast.LENGTH_LONG)
                    .show();*/
            Log.v("baidu","上传位置失败");
        }
    }
    
    public void uploadOnce(){
        firstLoad=false;
        if (pt == null) {
            Toast.makeText(getActivity(), "未获取到位置", Toast.LENGTH_LONG)
            .show();
            return;
        }
        mManager.setUserID(DemoApplication.getInstance().getUserName());
        RadarUploadInfo info = new RadarUploadInfo();
        info.comments = "我是测试机";
        info.pt = pt;
        boolean c=RadarSearchManager.getInstance().uploadInfoRequest(info);
        //Log.v("baidu","主动上传位置结果："+c);
    }



    @Override
    public void onReceiveLocation(BDLocation location) {
        // TODO Auto-generated method stub
        if (location == null){
            Toast.makeText(getActivity(), "获取位置信息失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if(firstLoad==false){
            return;
        }   
        this.location=location;
        pt = new LatLng(location.getLatitude(), location.getLongitude());
        Log.v("baidu","获取本地位置成功"+location.getAddrStr()+"--pt:"+pt);
        uploadOnce();
    }



    @Override
    public void onReceivePoi(BDLocation arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch(view.getId()){
           case R.id.screenLl:
               
               break;
        }
    }



    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        // TODO Auto-generated method stub
        User user=userList.get(position-1);
        //跳入用户个人资料页面
    }

}
