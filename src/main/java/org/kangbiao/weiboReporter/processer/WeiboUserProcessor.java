package org.kangbiao.weiboReporter.processer;

import us.codecraft.webmagic.Page;


/**
 * Created by bradykang on 4/19/2017.
 * 微博用户信息处理器
 */
public class WeiboUserProcessor implements WeiboProcessor{
    public void process(Page page) {
        String name=page.getJson().jsonPath("$.cards[\"0\"].card_group[\"0\"].item_content").get();
        System.out.println("name:"+name);
    }
}
