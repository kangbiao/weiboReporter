package org.kangbiao.weiboReporter.processer;

import org.kangbiao.weiboReporter.entity.PageType;
import us.codecraft.webmagic.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bradykang on 4/19/2017.
 * 微博页面处理策略选择类
 */
public class WeiboProcessorContext {

    private Map<PageType,WeiboProcessor> weiboProcessorMap=new HashMap<PageType, WeiboProcessor>();

    public void process(PageType pageType,Page page){
        WeiboProcessor weiboProcessor=this.weiboProcessorMap.get(pageType);
        if (weiboProcessor==null){
            weiboProcessor=this.getWeiboProcessorByPageType(pageType);
            weiboProcessorMap.put(pageType,weiboProcessor);
        }
        weiboProcessor.process(page);
    }

    private WeiboProcessor getWeiboProcessorByPageType(PageType pageType){
        WeiboProcessor weiboProcessor;
        switch (pageType){
            case CONTAINER_ID:
                weiboProcessor=new WeiboContainerProcessor();
                break;
            case USER_PROFILE:
                weiboProcessor=new WeiboUserProcessor();
                break;
            case WEIBO_COMMENT:
                weiboProcessor=new WeiboCommentProcessor();
                break;
            case WEIBO_FEED:
                weiboProcessor=new WeiboFeedProcessor();
                break;
            default:
                weiboProcessor=null;
                break;
        }
        return weiboProcessor;
    }
}
