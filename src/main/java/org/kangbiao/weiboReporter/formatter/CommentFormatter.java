package org.kangbiao.weiboReporter.formatter;

import org.kangbiao.weiboReporter.entity.Category;
import org.kangbiao.weiboReporter.entity.WeiboComment;
import org.kangbiao.weiboReporter.util.JsonUtil;
import us.codecraft.webmagic.selector.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by bradykang on 5/15/2017.
 * 微博评论解析格式化
 */
public class CommentFormatter {

    public List<WeiboComment> parse(String response){
        Json json=new Json(response);
        List<WeiboComment> weiboComments=new ArrayList<WeiboComment>();
        List<String> comments =json.jsonPath("$.data[*]").all();
        for (String comment:comments){
            WeiboComment weiboComment=JsonUtil.fromJson(comment, WeiboComment.class);
            weiboComment.setCreated_at(getDateTime(weiboComment.getCreated_at()));
            weiboComment.setTime(getTime(weiboComment.getCreated_at()));
            weiboComment.setFormatedSource(parseSource(weiboComment.getSource()));
            weiboComments.add(weiboComment);
        }
        return weiboComments;
    }

    private String getDateTime(String rawTime){
        String reg="^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}$";
        boolean result= Pattern.compile(reg).matcher(rawTime).matches();
        return result?rawTime:"2017-"+rawTime;
    }

    private String getTime(String rawTime){
        String[] arr=rawTime.split("\\s");
        return arr.length>1?arr[1]:null;
    }

    private String parseSource(String rawSource){
        rawSource=rawSource.toLowerCase();
        Set a=Category.phone.keySet();
        for (String key:Category.phone.keySet()){
            if (rawSource.contains(key)){
                return Category.phone.get(key);
            }
        }
        return "其他";
    }
}
