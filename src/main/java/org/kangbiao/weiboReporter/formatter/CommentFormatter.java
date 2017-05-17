package org.kangbiao.weiboReporter.formatter;

import org.kangbiao.weiboReporter.entity.Category;
import org.kangbiao.weiboReporter.entity.WeiboComment;
import org.kangbiao.weiboReporter.util.JsonUtil;
import us.codecraft.webmagic.selector.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bradykang on 5/15/2017.
 * 微博评论解析格式化
 */
public class CommentFormatter extends BaseFormatter{

    public List<WeiboComment> parse(String response){
        Json json=new Json(response);
        List<WeiboComment> weiboComments=new ArrayList<WeiboComment>();
        List<String> comments =json.jsonPath("$.data[*]").all();
        for (String comment:comments){
            WeiboComment weiboComment=JsonUtil.fromJson(comment, WeiboComment.class);
            weiboComment.setCreated_at(super.getDateTime(weiboComment.getCreated_at()));
            weiboComment.setTime(super.getTime(weiboComment.getCreated_at()));
            weiboComment.setFormatedSource(this.parseSource(weiboComment.getSource()));
            weiboComments.add(weiboComment);
        }
        return weiboComments;
    }

    private String parseSource(String rawSource){
        rawSource=rawSource.toLowerCase();
        for (String key:Category.phone.keySet()){
            if (rawSource.contains(key)){
                return Category.phone.get(key);
            }
        }
        return "其他";
    }
}
