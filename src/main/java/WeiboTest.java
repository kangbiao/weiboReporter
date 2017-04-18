import com.google.gson.reflect.TypeToken;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;
import util.JsonUtil;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by bradykang on 3/29/2017.
 *
 */
public class WeiboTest implements PageProcessor {


    private static String uid="2241191261";

    public void process(Page page) {
        int code=page.getStatusCode();
        PageExtra pageExtra= (PageExtra) page.getRequest().getExtra("pageExtra");
        if (pageExtra.getType()==PageType.CONTAINER_ID){
           List<String> tabs= new JsonPathSelector("$.tabsInfo.tabs").selectList(page.getRawText());
           Type type=new TypeToken<Map<String,String>>(){}.getType();
           String weiboListUrl="http://m.weibo.cn/api/container/getIndex?type=uid&value=%s&containerid=%s";
           for (String tabString:tabs){
               Map<String,String> tabMap=JsonUtil.fromJson(tabString, type);
               if (tabMap.get("tab_type").equals("profile")){
                   System.out.println(tabString);
               }
               else if (tabMap.get("tab_type").equals("weibo")){
                   Request request=new Request(String.format(weiboListUrl,uid,tabMap.get("containerid")));
                   Map<String, Object> pageExtrasMap=new HashMap<String, Object>();
                   PageExtra pageExtra1=new PageExtra();
                   pageExtra1.setDepth(2);
                   pageExtra1.setType(PageType.WEIBO_LIST_INIT);
                   pageExtrasMap.put("pageExtra",pageExtra1);
                   pageExtrasMap.put("containerid",tabMap.get("containerid"));
                   request.setExtras(pageExtrasMap);
                   page.addTargetRequest(request);
               }
           }
        }
        else if (pageExtra.getType()==PageType.WEIBO_LIST_INIT){
            String url="http://m.weibo.cn/api/container/getIndex?type=uid&value=%s&containerid=%s&page=%s";
            Integer total= Integer.valueOf(page.getJson().jsonPath("$.cardlistInfo.total").get());
            Map<String, Object> pageExtrasMap=new HashMap<String, Object>();
            PageExtra pageExtra2=new PageExtra();
            pageExtra2.setDepth(2);
            pageExtra.setType(PageType.WEIBO_LIST_INIT);
            pageExtrasMap.put("pageExtra",pageExtra2);
            for (int i=2;i<total/10+1;i++){
                Request request=new Request(String.format(url,uid,page.getRequest().getExtra("containerid"),i));
                request.setExtras(pageExtrasMap);
                page.addTargetRequest(request);
            }
        }
        else {
            List<String> ids=page.getJson().jsonPath(".cards[*].mblog.id").all();
            for (String id:ids){
                System.out.println(id);
            }
        }
    }

    public Site getSite() {
        Set<Integer> statusCodeSet=new HashSet<Integer>();
        statusCodeSet.add(200);
        statusCodeSet.add(414);
        return Site.me().setRetryTimes(3).setSleepTime(100).setAcceptStatCode(statusCodeSet);
    }

    public static void main(String[] args) {
        String url="http://m.weibo.cn/api/container/getIndex?type=uid&value=%s";
        Request request=new Request(String.format(url,uid));
        Map<String, Object> pageExtrasMap=new HashMap<String, Object>();
        PageExtra pageExtra=new PageExtra();
        pageExtra.setDepth(1);
        pageExtra.setType(PageType.CONTAINER_ID);
        pageExtrasMap.put("pageExtra",pageExtra);
        request.setExtras(pageExtrasMap);
        Spider.create(new WeiboTest())
                .addRequest(request)
                .addPipeline(new ConsolePipeline())
                .thread(5)
                .run();
    }
}
