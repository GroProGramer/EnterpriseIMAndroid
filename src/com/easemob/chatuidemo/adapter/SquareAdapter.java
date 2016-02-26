package com.easemob.chatuidemo.adapter;

import java.util.List;

import com.easemob.chatuidemo.widget.NoScrollGridView;
import com.enterpriseIM.R;

import wyj.bean.MoodBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SquareAdapter extends BaseAdapter{
    
    private Context context;
    private List<MoodBean> datas;
    private LayoutInflater mInflater;
    
    SquareAdapter(Context context,List<MoodBean> datas){
        this.context=context;
        this.datas=datas;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(datas!=null){
            return datas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if(datas!=null){
            return datas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.gushi_list_item, null);
            holder.avator=(ImageView)convertView.findViewById(R.id.avator);
            holder.name=(TextView)convertView.findViewById(R.id.name);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.gridView=(NoScrollGridView)convertView.findViewById(R.id.gridView);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.zan = (TextView) convertView.findViewById(R.id.zan);
            //zan_tv = holder.zan;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        return convertView;
    }

    private static class ViewHolder {

        public TextView name;
        public ImageView avator;
        TextView content;
        TextView time;
        TextView zan;
        NoScrollGridView gridView;
    }
}
