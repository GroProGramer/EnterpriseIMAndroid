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
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.baidu.mapapi.radar.RadarUploadInfoCallback;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.adapter.YuJianAdapter;
import com.easemob.chatuidemo.adapter.YuJianAdapter1;
import com.enterpriseIM.R;

public class YuJianFragment extends Fragment implements IXListViewListener,RadarSearchListener{

    RadarSearchManager mManager=null;
    LatLng pt=null;
    public static final int FRIST_GET_DATE = 0x1;
    public static final int REFRESH_GET_DATE = 0x2;
    public static final int LOADMORE_GET_DATE = 0x3;
    private static List<User> userList;
    private static ProgressDialog pd = null ;
    private static boolean progressShow = false;//进度条是否显示
    private LinearLayout pengpeng;
    private LinearLayout shaixuan;
    private XListView mListView;
    private YuJianAdapter1 mAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreateView(inflater, container, savedInstanceState);
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view=inflater.inflate(R.layout.fragment_conversation_zaina, container,false);
        mListView = (XListView)view.findViewById(R.id.xListView);
        mListView.setPullLoadEnable(true);
        
        pengpeng = (LinearLayout)view.findViewById(R.id.pengpeng);
        shaixuan = (LinearLayout)view.findViewById(R.id.shaixuan);
        return view;
    }
    
    

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        userList=new ArrayList<User>();
        loadUsers();
        //mAdapter = new YuJianAdapter(getActivity(), R.layout.zaina_list_item, userList);
        mAdapter = new YuJianAdapter1(getActivity(),  userList);
        mListView.setAdapter(mAdapter);
    }



    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        
    }
    
    private void loadUsers() {
        
        mManager = RadarSearchManager.getInstance();
        mManager.addNearbyInfoListener(this);
      
        Log.v("baidu","百度地图初始化成功");
        LocationClient locationClient = new LocationClient(getActivity());
        LocationClientOption locationoption = new LocationClientOption();
        locationoption.setOpenGps(true); // 是否打开GPS
        locationoption.setCoorType("bd09ll"); 
        locationoption.setPoiExtraInfo(true);
        locationoption.setAddrType("all");
        locationoption.setScanSpan(500);
        locationoption.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先,不设置，默认是gps优先
        locationoption.setPoiNumber(10);
        locationClient.setLocOption(locationoption);
        locationClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                // TODO Auto-generated method stub
                if (location == null) {
                    Log.v("baidu","获取不到本地位置");
                    return;
                }
                pt=new LatLng(location.getLatitude(),location.getLongitude());
                Log.v("baidu","获取本地位置成功"+location.getAddrStr()+"--pt:"+pt);
                mManager.setUserID(DemoApplication.getInstance().getUserName());
                RadarUploadInfo info = new RadarUploadInfo();
                info.comments = "我是测试用户";
                info.pt = pt;
                boolean c=mManager.uploadInfoRequest(info);
                
                Log.v("baidu","主动上传位置结果："+c);
                mManager.startUploadAuto(new RadarUploadInfoCallback(){

                    @Override
                    public RadarUploadInfo onUploadInfoCallback() {
                        // TODO Auto-generated method stub
                        RadarUploadInfo info = new RadarUploadInfo();
                        mManager.setUserID(DemoApplication.getInstance().getUserName());
                        info.comments = "我是测试用户";
                        info.pt = pt;
                        return info;
                    }}, 5000);
            }

            @Override
            public void onReceivePoi(BDLocation location) {
                // TODO Auto-generated method stub
                
            }});
        if(!locationClient.isStarted()){
            locationClient.start();
        }
        locationClient.requestLocation();
        locationClient.requestPoi();
       
        mManager.setUserID(DemoApplication.getInstance().getUserName());
        RadarUploadInfo info = new RadarUploadInfo();
        info.comments = "我是测试用户";
        info.pt = pt;
        boolean a=mManager.uploadInfoRequest(info);
        
        Log.v("baidu","上传地址信息结果："+a);
        
        
       //RadarNearbySearchOption option = new RadarNearbySearchOption();//.centerPt(pt);//.radius(2000);
        //mManager.nearbyInfoRequest(option);
        //Log.v("baidu","获取周边用户信息");
    }
    
    private void geneItems(final int ACTION){
        
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
            RadarNearbySearchOption option = new RadarNearbySearchOption().radius(2000);
            boolean b=mManager.nearbyInfoRequest(option);
           
            Log.v("baidu","请求周边用户信息结果："+b);
        } else {
            //上传失败
            /*Toast.makeText(getActivity(), "单次上传位置失败", Toast.LENGTH_LONG)
                    .show();*/
            Log.v("baidu","上传位置失败");
        }
    }
    
    

}
