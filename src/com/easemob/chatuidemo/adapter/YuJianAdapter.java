package com.easemob.chatuidemo.adapter;

import java.util.List;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.enterpriseIM.R;

import wyj.bean.User;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class YuJianAdapter extends ArrayAdapter<User>{

    private LayoutInflater inflater;
    private Context context;
    public YuJianAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        // TODO Auto-generated constructor stub
        this.context=context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        super.getView(position, convertView, parent);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.zaina_list_item, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.zaina_name);
            //holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.message = (TextView) convertView.findViewById(R.id.zaina_message);
            holder.time = (TextView) convertView.findViewById(R.id.zai_time);
            holder.avatar = (ImageView) convertView.findViewById(R.id.zaina_avatar);
            holder.msgState = (TextView)convertView.findViewById(R.id.zaina_sex_state);
            holder.city = (TextView)convertView.findViewById(R.id.zaina_city);
            holder.list_item_layout=(RelativeLayout) convertView.findViewById(R.id.list_item_layout);
            convertView.setTag(holder);
        }
        
        final User user = getItem(position);
        String userId = user.getUser_id();
        EMConversation conversation = EMChatManager.getInstance().getConversation(userId);
        holder.name.setText(user.getNickname()!= null ? user.getNickname() : userId);
        
        return convertView;
    }
    
    

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return super.getItemId(position);
    }



    private static class ViewHolder {
        /** 和谁的聊天记录 */
        TextView name;
        /** 消息未读数 */
        TextView unreadLabel;
        /** 最后一此登录的地址 */
        TextView message;
        /** 最后一次登录的时间 */
        TextView time;
        /** 用户头像 */
        ImageView avatar;
        /** 性别 */
        TextView msgState;
        /** 城市 */
        TextView city;
        /**整个list中每一行总布局*/
        RelativeLayout list_item_layout;
        
        
    }
    
}
