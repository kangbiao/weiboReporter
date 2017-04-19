package org.kangbiao.weiboReporter.schduler;

import com.alibaba.fastjson.JSON;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

/**
 * Created by bradykang on 4/19/2017.
 *
 */
public class FileQueueScheduler extends FileCacheQueueScheduler{
    public FileQueueScheduler(String filePath) {
        super(filePath);
    }
}
