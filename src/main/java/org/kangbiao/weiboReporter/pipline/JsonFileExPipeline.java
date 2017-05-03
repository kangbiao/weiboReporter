package org.kangbiao.weiboReporter.pipline;

/**
 * Created by bradykang on 5/3/2017.
 *
 */
import com.alibaba.fastjson.JSON;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.commons.codec.digest.DigestUtils;
import org.kangbiao.weiboReporter.entity.PageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

public class JsonFileExPipeline extends FilePersistentBase implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public JsonFileExPipeline() {
        this.setPath("/data/webmagic");
    }

    public JsonFileExPipeline(String path) {
        this.setPath(path);
    }

    public void process(ResultItems resultItems, Task task) {
        String path = this.path + PATH_SEPERATOR + task.getUUID() + PATH_SEPERATOR;

        try {
            PageType pageType=resultItems.get("type");
            PrintWriter e = new PrintWriter(new FileWriter(this.getFile(path + pageType+ "-" + DigestUtils.md5Hex(resultItems.getRequest().getUrl()) + ".json")));
            e.write(JSON.toJSONString(resultItems.getAll()));
            e.close();
        } catch (IOException var5) {
            this.logger.warn("write file error", var5);
        }

    }
}
