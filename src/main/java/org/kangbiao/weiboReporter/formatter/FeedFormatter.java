package org.kangbiao.weiboReporter.formatter;

import com.alibaba.fastjson.JSON;
import org.kangbiao.weiboReporter.entity.WeiboFeed;
import org.kangbiao.weiboReporter.entity.Category;
import us.codecraft.webmagic.selector.Json;

import java.io.IOException;
import java.util.List;

/**
 * Created by bradykang on 5/15/2017.
 * 微博动态解析格式化
 */
public class FeedFormatter extends BaseFormatter{

    public void parse(String response){
        Json json=new Json(response);
        List<String> mblogs=json.jsonPath("$.cards[*].mblog").all();
        for (String mblog:mblogs){
            WeiboFeed weiboFeed=JSON.parseObject(mblog, WeiboFeed.class);
            weiboFeed.setText(this.parseText(weiboFeed.getText()));
            weiboFeed.setCreated_at(super.getDateTime(weiboFeed.getCreated_at()));
            weiboFeed.setTopic(this.getTopic(weiboFeed.getText()));
            weiboFeed.setCreate_time(super.getTime(weiboFeed.getCreated_at()));
        }
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
        return "其他";
    }

    public static void main(String[] args) throws IOException {
        FeedFormatter feedFormatter =new FeedFormatter();
        String result= feedFormatter.getTopic("这周去温江写作业了吗");
        System.out.println(result);
    }
}
