package org.kangbiao.weiboReporter.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.kangbiao.weiboReporter.entity.Category;
import org.kangbiao.weiboReporter.entity.WeiboComment;
import org.kangbiao.weiboReporter.entity.WeiboUser;
import org.kangbiao.weiboReporter.formatter.CommentFormatter;
import org.kangbiao.weiboReporter.formatter.FeedFormatter;
import org.kangbiao.weiboReporter.formatter.UserFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.selector.Json;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bradykang on 5/8/2017.
 *
 */
public class ElasticsearchUploader {
    private static Logger logger = LoggerFactory.getLogger(ElasticsearchUploader.class);
    public static void main(String[] args) throws IOException {
            test();
//        String template="{\"id\":\"%s\",\"count1\":%s,\"count2\":%s,\"name\":\"%s\",\"createTime\":\"2016-08-%s\"}";
//
//        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
//                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
//
//        BulkRequestBuilder builder=client.prepareBulk();
//
//        for (int i=10;i<=30;i++){
//            String temp=String.valueOf(i);
//            String temp2=String.format(template,i,i,i,"company"+i,i);
//            builder.add(client.prepareIndex("test2","company")
//                    .setSource(temp2,XContentType.JSON));
//
//        }
//        BulkResponse bulkItemResponses=builder.execute().actionGet();
//
//        System.out.print("sssss");
    }



    public static void test() throws IOException {
//        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
//                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
//        BulkProcessor bulkProcessor = BulkProcessor.builder(
//                client,
//                new BulkProcessor.Listener() {
//                    public void beforeBulk(long l, BulkRequest bulkRequest) {
//                        logger.info("bulk request numberOfActions:" + bulkRequest.numberOfActions());
//                    }
//
//                    public void afterBulk(long l, BulkRequest bulkRequest,
//                                          BulkResponse bulkResponse) {
//                        logger.info("bulk response has failures: " + bulkResponse.hasFailures());
//                    }
//
//                    public void afterBulk(long l, BulkRequest bulkRequest,
//                                          Throwable throwable) {
//                        logger.warn("bulk failed: " + throwable);
//                    }
//                })
//                .setBulkActions(10000)
//                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
//                .setFlushInterval(TimeValue.timeValueSeconds(5))
//                .setConcurrentRequests(1)
//                .setBackoffPolicy(
//                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
//                .build();

        String path="C:\\Users\\I337077\\Desktop\\data5000-4000\\m.weibo.com";
        File file=new File(path);
        File[] files=file.listFiles();
        FeedFormatter feedFormatter =new FeedFormatter();
        CommentFormatter commentFormatter=new CommentFormatter();
        UserFormatter userFormatter=new UserFormatter();
        List<WeiboUser> weiboUsers=new ArrayList<WeiboUser>();
        for (File f:files){
            if (f.isFile()){
                if (f.getName().startsWith("USER_PROFILE")){
                    String content = FileUtils.readFileToString(f, "UTF-8");
                    Map map = JSON.parseObject(content, Map.class);
                    try {
                        if (map.size() == 3) {
                            Json json = new Json(String.valueOf(map.get("response")));
                            if (Integer.valueOf(json.jsonPath("$.ok").get()) == 0) {
                                json = new Json(Http.sendGet(String.valueOf(map.get("url"))));
                            }
                            if (Integer.valueOf(json.jsonPath("$.ok").get()) == 0) {
                                continue;
                            }
                            weiboUsers.add(userFormatter.parse(json));
                        }
                    }
                    catch (Exception e)
                    {
                        e.getMessage();
                    }
                }
//                if (f.getName().startsWith("WEIBO_COMMENT")) {
//                    String content = FileUtils.readFileToString(f, "UTF-8");
//                    Map map = JSON.parseObject(content, Map.class);
//                    if (map.size() == 3) {
//                        for (WeiboComment weiboComment:commentFormatter.parse(String.valueOf(map.get("response")))) {
//                            String string=JSON.toJSONString(weiboComment);
//                            bulkProcessor.add(
//                                    new IndexRequest("1234", "WEIBO_COMMENT", weiboComment.getId())
//                                            .source(string, XContentType.JSON));
//                        }
//                    }
//                }
            }
        }
        System.out.print("aaaaaaaaa");
    }
}

