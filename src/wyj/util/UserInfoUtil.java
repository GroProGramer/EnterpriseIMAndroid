package wyj.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import wyj.bean.User;

public class UserInfoUtil {
    private  User user=new User();
    private SharedPreferences mSharedPreferences;
    private static Context context;
    public  String spNamme="user_info";
    public  String sex="sex";
    private static UserInfoUtil instance=new UserInfoUtil();
    public static UserInfoUtil getInstance(Context context){
        UserInfoUtil.context=context;
        return instance;
    }
    public  User getUserInfo(){
        initUserInfo(user);
        return user;
    }
    
    private void initUserInfo(User user){
        mSharedPreferences=context.getSharedPreferences(spNamme, Activity.MODE_PRIVATE);
        //SharedPreferences.Editor editor =mSharedPreferences.edit();
        user.setSex(mSharedPreferences.getString(sex, "保密"));
    }
    
    public void saveUserInfo(User user){
        mSharedPreferences=context.getSharedPreferences(spNamme, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor =mSharedPreferences.edit();
        editor.putString(sex, user.getSex());
        editor.commit();
    }
}





