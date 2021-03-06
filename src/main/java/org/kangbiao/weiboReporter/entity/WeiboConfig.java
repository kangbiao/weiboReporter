package org.kangbiao.weiboReporter.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import us.codecraft.webmagic.Site;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by bradykang on 4/19/2017.
 *
 */
public class WeiboConfig {
    private Site site;
    private String uid;
    private int threadNum;
    private String urlCacheDir;
    private String dataDir;
    private Map<String,Map<String,List<String>>> category;
    private List<String> dataPaths;

    public WeiboConfig() throws IOException {
        String  path=WeiboConfig.class.getResource("/").getPath()+"config.json";
        String configString;
        try {
            configString=FileUtils.readFileToString(new File(path),"UTF-8");
            JSONObject jsonObject=JSON.parseObject(configString);
            this.setSite(JSON.parseObject(jsonObject.getString("site"),Site.class));
            this.setUid(jsonObject.getString("uid"));
            this.setThreadNum(jsonObject.getInteger("threadNum"));
            this.setUrlCacheDir(jsonObject.getString("urlCacheDir"));
            this.setDataDir(jsonObject.getString("dataDir"));
            this.setDataPaths((List<String>) jsonObject.get("dataPaths"));
            this.setCategory((Map<String, Map<String, List<String>>>) jsonObject.get("category"));
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

    public String getUrlCacheDir() {
        return urlCacheDir;
    }

    public void setUrlCacheDir(String urlCacheDir) {
        this.urlCacheDir = urlCacheDir;
    }

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public Map<String, Map<String, List<String>>> getCategory() {
        return category;
    }

    public void setCategory(Map<String, Map<String, List<String>>> category) {
        this.category = category;
    }

    public List<String> getDataPaths() {
        return dataPaths;
    }

    public void setDataPaths(List<String> dataPaths) {
        this.dataPaths = dataPaths;
    }
}
