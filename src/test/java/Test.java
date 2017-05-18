import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.kangbiao.weiboReporter.entity.WeiboComment;
import org.kangbiao.weiboReporter.util.JsonUtil;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Json;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bradykang on 4/18/2017.
 *
 */
public class Test {

    public static void main(String[] args){
//        String a="{\"like_counts\":\"\",\"created_at\":\"2016-06-13 01:00\",\"id\":3985710291677868,\"source\":\"iPhone 5s\",\"text\":\"你这是犯罪啊\",\"user\":{\"mbtype\":0,\"screen_name\":\"Jupiter04\",\"profile_url\":\"http:\\/\\/m.weibo.cn\\/u\\/2817763537?uid=2817763537\",\"verified\":false,\"profile_image_url\":\"https:\\/\\/tva1.sinaimg.cn\\/crop.0.2.640.640.180\\/a7f3a8d1jw8fb6pd8hn13j20hs0hwt9h.jpg\",\"id\":2817763537,\"verified_type\":-1},\"liked\":false}";
//        WeiboComment weiboComment= JsonUtil.fromJson(a, WeiboComment.class);
        System.out.print("sadsa".contains(""));
    }


    private static int countUserFile(String path) {
        File file=new File(path);
        File[] files=file.listFiles();
        int i=0;
        for (File f:files){
            if (f.isFile()){
                String content= null;
                try {
                    content = FileUtils.readFileToString(f,"UTF-8");
                    System.out.println(content);
                    content= (String) JSON.parseObject(content, Map.class).get("response");
                    if (content.contains("msg")) {
                        Json json = new Json(content);
                        System.out.println(json.jsonPath("$.msg").get());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (content.contains("USER_PROFILE")){
                    i++;
                }
            }
        }
        return i;
    }




    private static void getRightUrls(){
        String path="C:\\Users\\I337077\\Projects\\weiboReporter\\src\\test\\java\\errorCommentPage.txt";
        List<Request> requests=getRequests(path);
        int i=0,j=0;
        for (Request request:requests){
            String response=sendGet(request.getUrl());
            if (!response.startsWith("{\"ok\":0,\"msg")){
                request.getExtras().remove("statusCode");
                System.out.println(JSON.toJSON(request));
                j++;
            }
            i++;
        }
        System.out.println(j+"/"+i);
    }

    private static List<Request> getRequests(String path)  {
        String line;
        List<Request> lines=new ArrayList<Request>();
        try {
            BufferedReader bufferedReader= new BufferedReader(new FileReader(path));
            while ((line=bufferedReader.readLine())!=null){
                Request request=JSON.parseObject(line,Request.class);
                if (request!=null)
                lines.add(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
