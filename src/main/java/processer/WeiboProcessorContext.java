package processer;

import entity.PageType;
import us.codecraft.webmagic.Page;

/**
 * Created by bradykang on 4/19/2017.
 * 微博页面处理策略选择类
 */
public class WeiboProcessorContext {

    private WeiboProcessor weiboProcessor;

    public WeiboProcessorContext(PageType pageType){
        switch (pageType){
            case CONTAINER_ID:
                this.weiboProcessor=new WeiboContainerProcessor();
                break;
            case USER_PROFILE:
                this.weiboProcessor=new WeiboUserProcessor();
                break;
            case WEIBO_COMMENT:
                this.weiboProcessor=new WeiboCommentProcessor();
                break;
            case WEIBO_LIST:
                this.weiboProcessor=new WeiboFeedProcessor();
                break;
            default:
                break;
        }
    }

    public void process(Page page){
        this.weiboProcessor.process(page);
    }
}
