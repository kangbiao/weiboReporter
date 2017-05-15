package org.kangbiao.weiboReporter.uploader;

import com.alibaba.fastjson.JSON;
import org.kangbiao.weiboReporter.entity.WeiboFeed;
import us.codecraft.webmagic.selector.Json;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by bradykang on 5/15/2017.
 * 微博动态上传
 */
public class FeedUploader {


    public void parse(Json json){
        List<String> mblogs=json.jsonPath("$.cards[*].mblog").all();
        for (String mblog:mblogs){
            WeiboFeed weiboFeed=JSON.parseObject(mblog, WeiboFeed.class);
            weiboFeed.setText(parseText(weiboFeed.getText()));
            weiboFeed.setCategory(getCategory(weiboFeed.getText()));
            weiboFeed.setSource(parseSource(weiboFeed.getSource()));
            weiboFeed.setCreated_at(getDateTime(weiboFeed.getCreated_at()));
            weiboFeed.setCreate_time(getTime(weiboFeed.getCreated_at()));
        }
    }

    private String getDateTime(String rawTime){
        String reg="^\\d\\{4}-\\d\\{2}-\\d\\{2}\\s \\d\\{2}:\\d\\{2}$";
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
        return null;
    }

    private String getCategory(String text){
        return null;
    }


}
