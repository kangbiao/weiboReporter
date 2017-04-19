package org.kangbiao.weiboReporter.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import us.codecraft.webmagic.Site;

import java.io.File;
import java.io.IOException;

/**
 * Created by bradykang on 4/19/2017.
 *
 */
public class WeiboConfig {
    private Site site;
    private String uid;
    private int threadNum;

    public WeiboConfig() throws IOException {
        String  path=WeiboConfig.class.getResource("/").getPath()+"config.json";
        String configString;
        try {
            configString=FileUtils.readFileToString(new File(path),"UTF-8");
            JSONObject jsonObject=JSON.parseObject(configString);
            this.setSite(JSON.parseObject(jsonObject.getString("site"),Site.class));
            this.setUid(jsonObject.getString("uid"));
            this.setThreadNum(jsonObject.getInteger("threadNum"));
        } catch (IOException e) {
            throw new IOException("配置文件读取失败");
        }
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }
}
