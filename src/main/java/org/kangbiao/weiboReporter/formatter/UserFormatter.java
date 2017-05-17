package org.kangbiao.weiboReporter.formatter;

import org.kangbiao.weiboReporter.entity.WeiboUser;
import org.kangbiao.weiboReporter.util.JsonUtil;
import us.codecraft.webmagic.selector.Json;

import java.util.List;
import java.util.Map;

/**
 * Created by bradykang on 5/15/2017.
 * 微博用户解析格式化
 */
public class UserFormatter extends BaseFormatter{

    public WeiboUser parse(Json json){
        List<String> type11cards = json.jsonPath("$.cards[*]").all();
        WeiboUser weiboUser = new WeiboUser();
        String containerid=json.jsonPath("$.cardlistInfo.containerid").get();
        if (containerid!=null&&!containerid.equals("")){
            weiboUser.setId(containerid.substring(2,5));
        }
        for (String type11card : type11cards) {
            Map<String, Object> type11cardMap = JsonUtil.fromJson(type11card, Map.class);
            if (type11cardMap.get("card_group") == null) {
                continue;
            }
            List cardGroups = (List) type11cardMap.get("card_group");
            if (cardGroups.size() == 0){
                continue;
            }
            for (Object o : cardGroups) {
                Map<String, String> profileItem = (Map<String, String>) o;
                if (profileItem.get("item_name") == null)
                    continue;
                if (profileItem.get("item_name").equals("注册时间"))
                    weiboUser.setJoinTime(super.getDateTime(profileItem.get("item_content")));
                else if (profileItem.get("item_name").equals("昵称"))
                    weiboUser.setNickname(profileItem.get("item_content"));
                else if (profileItem.get("item_name").equals("性别"))
                    weiboUser.setSex(profileItem.get("item_content"));
                else if (profileItem.get("item_name").equals("所在地"))
                    weiboUser.setLocation(profileItem.get("item_content"));
                else if (profileItem.get("item_name").equals("简介"))
                    weiboUser.setIntro(profileItem.get("item_content"));
                else if (profileItem.get("item_name").equals("等级"))
                    weiboUser.setLevel(profileItem.get("item_content"));
                else if (profileItem.get("item_name").equals("阳光信用"))
                    weiboUser.setCredit(profileItem.get("item_content"));
                else if (profileItem.get("item_name").equals("标签"))
                    weiboUser.setTag(profileItem.get("item_content"));
                else if (profileItem.get("item_name").equals("博客"))
                    weiboUser.setBlog(profileItem.get("item_content"));
                else if (profileItem.get("item_name").equals("公司"))
                    weiboUser.setCompany(profileItem.get("item_content"));
                else if (profileItem.get("item_name").contains("学校"))
                    weiboUser.setSchool(profileItem.get("item_content"));
                else if (profileItem.get("item_name").contains("邮箱"))
                    weiboUser.setEmail(profileItem.get("item_content"));
            }
        }
        return weiboUser;
    }
}
