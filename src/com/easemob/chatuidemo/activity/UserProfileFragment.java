package com.easemob.chatuidemo.activity;

import com.easemob.EMValueCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.UserUtils;
import com.enterpriseIM.R;
import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfileFragment extends Fragment implements OnClickListener{
    
    private static final int REQUESTCODE_PICK = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    private ImageView headAvatar;
    private ImageView headPhotoUpdate;
    private ImageView iconRightArrow;
    private TextView tvNickName;
    private TextView tvUsername;
    private ProgressDialog dialog;
    private RelativeLayout rlNickName;    

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.activity_user_profile, container, false);
    }
    
    

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        
    }



    private void initView() {
        headAvatar = (ImageView) getView().findViewById(R.id.user_head_avatar);
        headPhotoUpdate = (ImageView) getView().findViewById(R.id.user_head_headphoto_update);
        tvUsername = (TextView) getView().findViewById(R.id.user_username);
        tvNickName = (TextView) getView().findViewById(R.id.user_nickname);
        rlNickName = (RelativeLayout) getView().findViewById(R.id.rl_nickname);
        iconRightArrow = (ImageView) getView().findViewById(R.id.ic_right_arrow);
    }

    private void initListener() {
        Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra("username");
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
            UserUtils.setCurrentUserAvatar( getActivity(), headAvatar);
        } else if (username.equals(EMChatManager.getInstance().getCurrentUser())) {
            tvUsername.setText(EMChatManager.getInstance().getCurrentUser());
            UserUtils.setCurrentUserNick(tvNickName);
            UserUtils.setCurrentUserAvatar( getActivity(), headAvatar);
        } else {
            tvUsername.setText(username);
            UserUtils.setUserNick(username, tvNickName);
            UserUtils.setUserAvatar( getActivity(), username, headAvatar);
            asyncFetchUserInfo(username);
        }
    }

    public void asyncFetchUserInfo(String username){
        ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().asyncGetUserInfo(username, new EMValueCallBack<User>() {
            
            @Override
            public void onSuccess(User user) {
                if (user != null) {
                    tvNickName.setText(user.getNick());
                    if(!TextUtils.isEmpty(user.getAvatar())){
                         Picasso.with(getActivity()).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(headAvatar);
                    }else{
                        Picasso.with(getActivity()).load(R.drawable.default_avatar).into(headAvatar);
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
        AlertDialog.Builder builder = new Builder(getActivity());
        builder.setTitle(R.string.dl_title_upload_photo);
        builder.setItems(new String[] { getString(R.string.dl_msg_take_photo), getString(R.string.dl_msg_local_upload) },
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                        case 0:
                            Toast.makeText(getActivity(), getString(R.string.toast_no_support),
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
        dialog = ProgressDialog.show(getActivity(), getString(R.string.dl_update_nick), getString(R.string.dl_waiting));
        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean updatenick = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().updateParseNickName(nickName);
                if (getActivity().isFinishing()) {
                    return;
                }
                if (!updatenick) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), getString(R.string.toast_updatenick_fail), Toast.LENGTH_SHORT)
                                    .show();
                            dialog.dismiss();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), getString(R.string.toast_updatenick_success), Toast.LENGTH_SHORT)
                                    .show();
                            tvNickName.setText(nickName);
                        }
                    });
                }
            }
        }).start();
    }


    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        
    }

    

}
