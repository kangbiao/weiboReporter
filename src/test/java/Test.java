import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.digest.DigestUtils;
import org.kangbiao.weiboReporter.entity.PageType;
import org.kangbiao.weiboReporter.util.JsonUtil;

import java.util.Map;
import java.util.Random;

/**
 * Created by bradykang on 4/18/2017.
 *
 */
public class Test {

    public static void main(String[] args){
        System.out.println(PageType.CONTAINER_ID.toString());
        System.out.print(DigestUtils.md5Hex("http://m.weibo.cn/api/container/getIndex?type=uid&value=2241191261"));
    }
}
