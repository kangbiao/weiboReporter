package org.kangbiao.weiboReporter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import org.kangbiao.weiboReporter.entity.WeiboComment;
import org.kangbiao.weiboReporter.entity.WeiboConfig;
import org.kangbiao.weiboReporter.entity.WeiboFeed;
import org.kangbiao.weiboReporter.entity.WeiboUser;
import org.kangbiao.weiboReporter.formatter.CommentFormatter;
import org.kangbiao.weiboReporter.formatter.FeedFormatter;
import org.kangbiao.weiboReporter.formatter.UserFormatter;
import org.kangbiao.weiboReporter.util.Http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.selector.Json;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * Created by bradykang on 5/15/2017.
 * 将数据上传到Elasticsearch
 */
public class ElasticsearchUploader {
    private static Logger logger = LoggerFactory.getLogger(ElasticsearchUploader.class);

    private BulkProcessor bulkProcessor;

    private FeedFormatter feedFormatter =new FeedFormatter();
    private CommentFormatter commentFormatter=new CommentFormatter();
    private UserFormatter userFormatter=new UserFormatter();
    private String index="weiboTest";

    private void init() throws UnknownHostException {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(
                        new InetSocketTransportAddress(
                                InetAddress.getByName("localhost"),
                                9300
                        )
                );
        bulkProcessor = BulkProcessor.builder(
                client,
                new BulkProcessor.Listener() {
                    public void beforeBulk(long l, BulkRequest bulkRequest) {
                        logger.info("bulk request numberOfActions:" + bulkRequest.numberOfActions());
                    }
                    public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                        logger.info("bulk response has failures: " + bulkResponse.hasFailures());
                    }
                    public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
                        logger.warn("bulk failed: " + throwable);
                    }
                })
                .setBulkActions(10000)
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1)
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();
    }

    public static void main(String[] args) throws IOException {
        WeiboConfig weiboConfig=new WeiboConfig();
        ElasticsearchUploader elasticsearchUploader=new ElasticsearchUploader();
        elasticsearchUploader.start(weiboConfig.getDataPaths());
    }

    public void start(List<String> dataPaths) throws IOException {
        this.init();
        for (String dataPath:dataPaths){
            this.process(dataPath);
        }
    }

    private void process(String path) throws IOException {
        File file = new File(path);
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                try {
                    String content = FileUtils.readFileToString(f, "UTF-8");
                    if (content == null || content.equals("") || content.equals("{}")) {
                        continue;
                    }
                    Map map = JSON.parseObject(content, Map.class);
                    if (map.size() == 3) {
                        send2BulkProcesser(map);
                    }
                }catch (Exception e){
                    logger.error("处理文件失败。 file="+f.getName()+" reason="+e.getMessage());
                }

            }
        }
    }

    private void send2BulkProcesser(Map map){
        Json json = new Json(String.valueOf(map.get("response")));
        if (Integer.valueOf(json.jsonPath("$.ok").get()) == 0) {
            json = new Json(Http.sendGet(String.valueOf(map.get("url"))));
        }
        if (Integer.valueOf(json.jsonPath("$.ok").get()) == 0) {
            logger.error("重试失败。 url="+map.get("url"));
        }
        if (map.get("type").equals("WEIBO_COMMENT")){
            for (WeiboComment weiboComment:commentFormatter.parse(json)) {
                bulkProcessor.add(new IndexRequest(
                        index,
                        "COMMENT",
                        weiboComment.getId())
                        .source(JSONObject.toJSONString(weiboComment), XContentType.JSON));
            }
        }else if (map.get("type").equals("WEIBO_FEED")){
            for (WeiboFeed weiboFeed:feedFormatter.parse(json)) {
                bulkProcessor.add(new IndexRequest(
                        index,
                        "FEED",
                        weiboFeed.getId())
                        .source(JSONObject.toJSONString(weiboFeed), XContentType.JSON));
            }
        }else if (map.get("type").equals("USER_PROFILE")){
            WeiboUser weiboUser=userFormatter.parse(json);
            bulkProcessor.add(new IndexRequest(
                    index,
                    "USER",
                    weiboUser.getId())
                    .source(JSONObject.toJSONString(weiboUser), XContentType.JSON));
        }
    }
}
