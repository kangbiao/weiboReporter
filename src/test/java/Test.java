import com.alibaba.fastjson.JSON;
import us.codecraft.webmagic.Request;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bradykang on 4/18/2017.
 *
 */
public class Test {

    public static void main(String[] args){
        getRightUrls();
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
