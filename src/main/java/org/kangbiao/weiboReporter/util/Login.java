package org.kangbiao.weiboReporter.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bradykang on 3/29/2017.
 *
 */
public class Login {

    public void login(String username,String password){
        Map<String,String> postData=new HashMap<String, String>(){{
            put("savestate", "1");
            put("ec", "0");
            put("pagerefer", "");
            put("entry", "mweibo");
            put("wentry", "");
            put("loginfrom", "");
            put("client_id", "");
            put("code", "");
            put("qq", "");
            put("hff", "");
            put("hfp", "");
        }};
        postData.put("username", username);
        postData.put("password", password);



    }

}
