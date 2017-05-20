package org.kangbiao.weiboReporter.formatter;

import java.util.regex.Pattern;

/**
 * Created by bradykang on 5/17/2017.
 *
 */
abstract class BaseFormatter {

    String getDateTime(String rawTime){
        String reg="^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}$";
        if(Pattern.compile(reg).matcher(rawTime).matches())
            return rawTime;
        reg="^\\d{2}-\\d{2}\\s\\d{2}:\\d{2}$";
        if (Pattern.compile(reg).matcher(rawTime).matches())
            return "2017-"+rawTime;
        reg="^\\d{2}-\\d{2}$";
        if (Pattern.compile(reg).matcher(rawTime).matches())
            return "2017-"+rawTime;
        return rawTime;
    }

    String getTime(String rawTime){
        String[] arr=rawTime.split("\\s");
        return arr.length>1?arr[1]:rawTime;
    }
}
