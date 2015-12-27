package wyj.util;

import wyj.bean.Session;

public class SessionUtil {
   private static Session session=new Session();
   
   public static Session getInstance(){
       return session;
   }

}
