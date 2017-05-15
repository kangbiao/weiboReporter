package org.kangbiao.weiboReporter.entity;

/**
 * Created by bradykang on 5/15/2017.
 * 动态
 */
public class WeiboFeed {

    private Integer attitudes_count;
    private String bid;
    private Integer comments_count;
    private String created_at;
    private Boolean favorited;
    private String id;
    private String idstr;
    private Boolean isLongText;
    private Integer mblogtype;
    private String mid;
    private Integer reposts_count;
    private String source;
    private String text;

    private String topic;
    private String create_time;


    public Integer getAttitudes_count() {
        return attitudes_count;
    }

    public void setAttitudes_count(Integer attitudes_count) {
        this.attitudes_count = attitudes_count;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public Integer getComments_count() {
        return comments_count;
    }

    public void setComments_count(Integer comments_count) {
        this.comments_count = comments_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Boolean getFavorited() {
        return favorited;
    }

    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public Boolean getLongText() {
        return isLongText;
    }

    public void setLongText(Boolean longText) {
        isLongText = longText;
    }

    public Integer getMblogtype() {
        return mblogtype;
    }

    public void setMblogtype(Integer mblogtype) {
        this.mblogtype = mblogtype;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Integer getReposts_count() {
        return reposts_count;
    }

    public void setReposts_count(Integer reposts_count) {
        this.reposts_count = reposts_count;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
