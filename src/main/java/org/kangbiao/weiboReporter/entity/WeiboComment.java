package org.kangbiao.weiboReporter.entity;

import javax.swing.text.html.parser.DTD;

/**
 * Created by bradykang on 5/15/2017.
 * 评论
 */
public class WeiboComment {

    private String created_at;
    private String id;
    private Integer like_counts;
    private Boolean liked;
    private String source;
    private String text;
    private User user;

    private String time;
    private String formatedSource;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getLike_counts() {
        return like_counts;
    }

    public void setLike_counts(Integer like_counts) {
        this.like_counts = like_counts;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFormatedSource() {
        return formatedSource;
    }

    public void setFormatedSource(String formatedSource) {
        this.formatedSource = formatedSource;
    }
}

class User{
    private String id;
    private Integer mbtype;
    private String profile_image_url;
    private String profile_url ;
    private String screen_name;
    private Boolean verified;
    private Integer verified_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMbtype() {
        return mbtype;
    }

    public void setMbtype(Integer mbtype) {
        this.mbtype = mbtype;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Integer getVerified_type() {
        return verified_type;
    }

    public void setVerified_type(Integer verified_type) {
        this.verified_type = verified_type;
    }
}
