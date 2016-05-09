package com.easemob.chatuidemo.adapter;

import java.util.List;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chatuidemo.utils.UserUtils;
import com.enterpriseIM.R;

import wyj.bean.User;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class YuJianAdapter1 extends BaseAdapter {

    private Context context;
    List<User> users;
    
    public YuJianAdapter1(Context context,List<User> users){
        this.context=context;
        this.users=users;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(users!=null){
            return users.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if(users!=null){
            return users.get(position);
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.zaina_list_item, parent, false);
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
        final User user = (User)getItem(position);
        String userId = user.getUser_id();
        //EMConversation conversation = EMChatManager.getInstance().getConversation(userId);
        //holder.name.setText(user.getNickname()!= null ? user.getNickname() : userId);
        UserUtils.setUserNick(userId, holder.name);
        holder.city.setText(user.getDistance());
        UserUtils.setUserAvatar(context, userId, holder.avatar);
        return convertView;
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
