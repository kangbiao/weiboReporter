package org.kangbiao.weiboReporter.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bradykang on 4/18/2017.
 * 微博页面类型
 */
public enum  PageType {
    CONTAINER_ID("微博容器ID"),
    WEIBO_FEED("微博动态"),
    WEIBO_COMMENT("微博评论"),
    USER_PROFILE("用户信息");

    private String comment;

    PageType(String comment) {
        this.comment=comment;
    }
    private final static Map<String , PageType> ENUM_MAP = new HashMap<String, PageType>(64);


    static {
        for(PageType v : values()) {
            ENUM_MAP.put(v.toString() , v);
        }
    }

    public static PageType fromObject(Object v) {
        return ENUM_MAP.get(String.valueOf(v));
    }
    public String getComment(){
        return this.comment;
    }
}
