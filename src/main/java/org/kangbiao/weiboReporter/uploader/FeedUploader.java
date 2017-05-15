package org.kangbiao.weiboReporter.uploader;

import com.alibaba.fastjson.JSON;
import org.kangbiao.weiboReporter.entity.WeiboFeed;
import org.kangbiao.weiboReporter.entity.Category;
import us.codecraft.webmagic.selector.Json;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by bradykang on 5/15/2017.
 * 微博动态上传
 */
public class FeedUploader {

    public void parse(String response){
        Json json=new Json(response);
        List<String> mblogs=json.jsonPath("$.cards[*].mblog").all();
        for (String mblog:mblogs){
            WeiboFeed weiboFeed=JSON.parseObject(mblog, WeiboFeed.class);
            weiboFeed.setText(parseText(weiboFeed.getText()));
            weiboFeed.setSource(parseSource(weiboFeed.getSource()));
            weiboFeed.setCreated_at(getDateTime(weiboFeed.getCreated_at()));

            weiboFeed.setTopic(getTopic(weiboFeed.getText()));
            weiboFeed.setCreate_time(getTime(weiboFeed.getCreated_at()));
        }
    }

    private String getDateTime(String rawTime){
        String reg="^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}$";
        boolean result=Pattern.compile(reg).matcher(rawTime).matches();
        return result?rawTime:"2017-"+rawTime;
    }

    private String getTime(String rawTime){
        String[] arr=rawTime.split("\\s");
        return arr.length>1?arr[1]:null;
    }

    private String parseSource(String rawSource){
        return rawSource;
    }

    private String parseText(String rawText){
        return rawText.replaceAll("<[^>]*>|\\s","").replaceAll("#成电树洞#","");
    }

    private String getTopic(String text){
        text=text.toLowerCase();
        for (String key:Category.topic.keySet()){
            if (text.contains(key)){
                return Category.topic.get(key);
            }
        }
        System.out.println(text);
        return text;
    }

    public static void main(String[] args) throws IOException {
        FeedUploader feedUploader=new FeedUploader();
        String result=feedUploader.getTopic("这周去温江写作业了吗");
        System.out.println(result);
    }
}
