package wyj.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wyj.bean.RegResult;

public class RegisterUtil {
    public static void register(String url,List<NameValuePair> params,RegisterListener listener){
    	InputStream is=null;
    	String result=null;
    	DefaultHttpClient httpclient=new DefaultHttpClient();
    	HttpPost httpPost=new HttpPost(url);
    	try {
			httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpResponse response=httpclient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
	        is = httpEntity.getContent();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
              {
                sb.append(line);
               }
              is.close();
             result = sb.toString();
             RegResult regResult=(RegResult)JsonUtil.getGsonInstance().fromJson(result, RegResult.class);
            
             if(listener!=null){
                 if(regResult!=null){
                     if(regResult.getRegstatus()==RegResult.RegStatus.RegSucess){
                         listener.registedSucess(regResult);
                     }
                     else listener.registedFailed(regResult);
                 }
                 else listener.registedFailed(regResult);
             }
             
             
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//loginListener.loginSucess(result);
    }

 
    
    public interface RegisterListener{
    	void registedSucess(RegResult response);
    	void registedFailed(RegResult response);
    }
    
}
