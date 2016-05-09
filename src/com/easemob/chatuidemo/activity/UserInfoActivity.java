package com.easemob.chatuidemo.activity;

import java.io.ByteArrayOutputStream;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMValueCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.UserUtils;
import com.enterpriseIM.R;
import com.squareup.picasso.Picasso;

public class UserInfoActivity extends BaseActivity implements OnClickListener{
    
    private static final int REQUESTCODE_PICK = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    private ImageView headAvatar;
    private ImageView headPhotoUpdate;
    private ImageView iconRightArrow;
    private TextView tvNickName;
    private TextView tvUsername;
    private LinearLayout llAddFriend;
    private ProgressDialog dialog;
    private RelativeLayout rlNickName;
    private String username;
    private boolean isFriend=false;
    private ProgressDialog progressDialog;
    private Button btn;
    
    
    
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_user_info);
        initView();
        initListener();
    }
    
    private void initView() {
        headAvatar = (ImageView) findViewById(R.id.user_head_avatar);
        headPhotoUpdate = (ImageView) findViewById(R.id.user_head_headphoto_update);
        tvUsername = (TextView) findViewById(R.id.user_username);
        tvNickName = (TextView) findViewById(R.id.user_nickname);
        rlNickName = (RelativeLayout) findViewById(R.id.rl_nickname);
        iconRightArrow = (ImageView) findViewById(R.id.ic_right_arrow);
        llAddFriend=(LinearLayout)findViewById(R.id.ll_add_friend);
        btn=(Button)findViewById(R.id.btn_send_msg_or_make_friend);
    }
    
    private void initListener() {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        isFriend=intent.getBooleanExtra("isFriend", false);
        boolean enableUpdate = intent.getBooleanExtra("setting", false);
        if (enableUpdate) {
            headPhotoUpdate.setVisibility(View.VISIBLE);
            iconRightArrow.setVisibility(View.VISIBLE);
            rlNickName.setOnClickListener(this);
            headAvatar.setOnClickListener(this);
        } else {
            headPhotoUpdate.setVisibility(View.GONE);
            iconRightArrow.setVisibility(View.INVISIBLE);
        }
        if (username == null) {
            tvUsername.setText(EMChatManager.getInstance().getCurrentUser());
            UserUtils.setCurrentUserNick(tvNickName);
            UserUtils.setCurrentUserAvatar(this, headAvatar);
        } else if (username.equals(EMChatManager.getInstance().getCurrentUser())) {
            tvUsername.setText(EMChatManager.getInstance().getCurrentUser());
            UserUtils.setCurrentUserNick(tvNickName);
            UserUtils.setCurrentUserAvatar(this, headAvatar);
        } else {
            tvUsername.setText(username);
            UserUtils.setUserNick(username, tvNickName);
            UserUtils.setUserAvatar(this, username, headAvatar);
            asyncFetchUserInfo(username);
        }
        
        
        llAddFriend.setOnClickListener(this);
    }
    
    public void beginConversation(String username){
        startActivity(new Intent(this, ChatActivity.class).putExtra("userId", username));
    }

    public void addContact(){
       /* if(DemoApplication.getInstance().getUserName().equals(username)){
            String str = getString(R.string.not_add_myself);
            startActivity(new Intent(this, AlertDialog.class).putExtra("msg", str));
            return;
        }
        
        if(((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList().containsKey(username)){
            //提示已在好友列表中，无需添加
            if(EMContactManager.getInstance().getBlackListUsernames().contains(username)){
                startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "此用户已是你好友(被拉黑状态)，从黑名单列表中移出即可"));
                return;
            }
            String strin = getString(R.string.This_user_is_already_your_friend);
            //startActivity(new Intent(this, AlertDialog.class).putExtra("msg", strin));
            Toast.makeText(this, strin, Toast.LENGTH_SHORT).show();
            return;
        }*/
        Log.v("baidu","点击了添加好友！");
        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        
        new Thread(new Runnable() {
            public void run() {
                
                try {
                    //demo写死了个reason，实际应该让用户手动填入
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMContactManager.getInstance().addContact(username, s);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, 1).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), 1).show();
                        }
                    });
                }
            }
        }).start();
    }
    
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.user_head_avatar:
            uploadHeadPhoto();
            break;
        case R.id.ll_add_friend:
            if(isFriend){
                btn.setText(this.getString(R.string.button_send_msg));
                beginConversation(username);
            }
            else{
                addContact();
            }
            break;
        case R.id.rl_nickname:
            final EditText editText = new EditText(this);
            new AlertDialog.Builder(this).setTitle(R.string.setting_nickname).setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                    .setPositiveButton(R.string.dl_ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String nickString = editText.getText().toString();
                            if (TextUtils.isEmpty(nickString)) {
                                Toast.makeText(UserInfoActivity.this, getString(R.string.toast_nick_not_isnull), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            updateRemoteNick(nickString);
                        }
                    }).setNegativeButton(R.string.dl_cancel, null).show();
            break;
        default:
            break;
        }

    }
    
    public void asyncFetchUserInfo(String username){
        ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().asyncGetUserInfo(username, new EMValueCallBack<User>() {
            
            @Override
            public void onSuccess(User user) {
                if (user != null) {
                    tvNickName.setText(user.getNick());
                    if(!TextUtils.isEmpty(user.getAvatar())){
                         Picasso.with(UserInfoActivity.this).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(headAvatar);
                    }else{
                        Picasso.with(UserInfoActivity.this).load(R.drawable.default_avatar).into(headAvatar);
                    }
                    UserUtils.saveUserInfo(user);
                }
            }
            
            @Override
            public void onError(int error, String errorMsg) {
            }
        });
    }
    
    
    
    private void uploadHeadPhoto() {
        AlertDialog.Builder builder = new Builder(this);
        builder.setTitle(R.string.dl_title_upload_photo);
        builder.setItems(new String[] { getString(R.string.dl_msg_take_photo), getString(R.string.dl_msg_local_upload) },
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                        case 0:
                            Toast.makeText(UserInfoActivity.this, getString(R.string.toast_no_support),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Intent pickIntent = new Intent(Intent.ACTION_PICK,null);
                            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(pickIntent, REQUESTCODE_PICK);
                            break;
                        default:
                            break;
                        }
                    }
                });
        builder.create().show();
    }
    
    

    private void updateRemoteNick(final String nickName) {
        dialog = ProgressDialog.show(this, getString(R.string.dl_update_nick), getString(R.string.dl_waiting));
        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean updatenick = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().updateParseNickName(nickName);
                if (UserInfoActivity.this.isFinishing()) {
                    return;
                }
                if (!updatenick) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(UserInfoActivity.this, getString(R.string.toast_updatenick_fail), Toast.LENGTH_SHORT)
                                    .show();
                            dialog.dismiss();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(UserInfoActivity.this, getString(R.string.toast_updatenick_success), Toast.LENGTH_SHORT)
                                    .show();
                            tvNickName.setText(nickName);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQUESTCODE_PICK:
            if (data == null || data.getData() == null) {
                return;
            }
            startPhotoZoom(data.getData());
            break;
        case REQUESTCODE_CUTTING:
            if (data != null) {
                setPicToView(data);
            }
            break;
        default:
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }
    
    /**
     * save the picture data
     * 
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(getResources(), photo);
            headAvatar.setImageDrawable(drawable);
            uploadUserAvatar(Bitmap2Bytes(photo));
        }

    }
    
    private void uploadUserAvatar(final byte[] data) {
        dialog = ProgressDialog.show(this, getString(R.string.dl_update_photo), getString(R.string.dl_waiting));
        new Thread(new Runnable() {

            @Override
            public void run() {
                final String avatarUrl = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().uploadUserAvatar(data);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if (avatarUrl != null) {
                            Toast.makeText(UserInfoActivity.this, getString(R.string.toast_updatephoto_success),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserInfoActivity.this, getString(R.string.toast_updatephoto_fail),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        }).start();

        dialog.show();
    }
    
    
    public byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}