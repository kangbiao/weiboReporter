package entity;

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
    public String getComment(){
        return this.comment;
    }
}
