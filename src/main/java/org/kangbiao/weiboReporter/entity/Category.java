package org.kangbiao.weiboReporter.entity;

import java.io.IOException;
import java.util.*;

/**
 * Created by bradykang on 5/15/2017.
 *
 */
public class Category {

    public static Map<String,String> phone=new HashMap<String, String>();

    public static Map<String,String> topic=new HashMap<String, String>();

    static {
        try {
            WeiboConfig weiboConfig=new WeiboConfig();
            Map<String ,List<String>> rawPhone=weiboConfig.getCategory().get("phone");
            Map<String ,List<String>> rawTopic=weiboConfig.getCategory().get("topic");
            for (String key :rawPhone.keySet()){
                for (String eachValue:rawPhone.get(key)){
                    phone.put(eachValue.toLowerCase(),key);
                }
            }
            for (String key :rawTopic.keySet()){
                for (String eachValue:rawTopic.get(key)){
                    topic.put(eachValue.toLowerCase(),key);
                }
            }
        } catch (IOException e) {
            System.out.println("配置文件初始化失败");
            System.exit(-1);
        }
    }
}
