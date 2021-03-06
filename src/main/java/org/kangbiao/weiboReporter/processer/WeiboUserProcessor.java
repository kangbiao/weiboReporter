package org.kangbiao.weiboReporter.processer;

import org.kangbiao.weiboReporter.entity.PageType;
import us.codecraft.webmagic.Page;


/**
 * Created by bradykang on 4/19/2017.
 * 微博用户信息处理器
 */
public class WeiboUserProcessor implements WeiboProcessor{
    public void process(Page page) {
        page.putField("url",page.getRequest().getUrl());
        page.putField("response",page.getRawText());
        page.putField("type", PageType.USER_PROFILE);
    }
}
