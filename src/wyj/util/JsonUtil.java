package wyj.util;

import com.google.gson.Gson;

public class JsonUtil {
   private static Gson gson = new Gson();
   public static String Object2JsonString(Object obj){ 
	   
	   return gson.toJson(obj);	   
   }
   
   /*public static Object JsonString2Object(String src){
       
    return gson.fromJson(src, Class<Object.class> classType);
    
       
   }*/
   
   public static Gson getGsonInstance(){
       return gson;
   }
}
