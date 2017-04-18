import com.google.gson.reflect.TypeToken;
import util.JsonUtil;

import java.util.Map;

/**
 * Created by bradykang on 4/18/2017.
 *
 */
public class Test {

    public static void main(String[] args){
        Map<String,String> map= JsonUtil.fromJson("{'data':1,'id':2}", new TypeToken<Map<String ,String >>(){}.getType());
        System.out.print("ss");
    }
}
