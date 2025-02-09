package com.hrs.kloping;

import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.MessageChain;

import static com.hrs.kloping.HPlugin_AutoReply.*;

public class Initer {
    private static boolean inited = false;

    public static final synchronized void Init() {
        if (!inited) {
            inited = true;
            key = init("#在这里写上触发添加的关键词,默认:开始添加", "key", key);
            host = init("#在这里写上你的的QQ号", "host", host, Long.class);
            selectKey = init("#在这里写上查询时关键词,默认:查询词", "selectKey", selectKey);
            deleteKey = init("#在这里写上删除时关键词,默认:删除词", "deleteKey", deleteKey);
            OneComAddSplit = init("#一次添加命令分割符,注意:不得使用以下字符:[]:英语,数字,=,>,", "OneComAddSplit", OneComAddSplit);
            OneComAddStr = init("#一次添加命令触发默认:/添加", "OneComAddStr", OneComAddStr);
            String[] sss = MyUtils.getStringsFromFile(thisPath + "/conf/auto_reply/followers");
            if (sss == null)
                MyUtils.appendStringInFile(thisPath + "/conf/auto_reply/followers", "#在这里添加所有能添加和查询消息的人的QQ号", false);
            else for (String s : sss) {
                try {
                    followers.add(Long.valueOf(s));
                } catch (Exception e) {
                    continue;
                }
            }
            sss = MyUtils.getStringsFromFile(thisPath + "/conf/auto_reply/data.data");
            if (sss != null)
                for (String ss : sss) {
                    try {
                        String[] ss2 = ss.split(splitK);
                        MessageChain chain = MiraiCode.deserializeMiraiCode(ss2[1]);
                        k2v.put(ss2[0], chain);
                    } catch (Exception e) {
                        continue;
                    }
                }
        }
    }

    private static synchronized String init(String tips, String fileName, String defaultStr) {
        String str = MyUtils.getStringFromFile(thisPath + "/conf/auto_reply/" + fileName);
        if (str == null || str.trim().isEmpty()) {
            MyUtils.putStringInFile(thisPath + "/conf/auto_reply/" + fileName, tips);
            return defaultStr;
        } else return str.trim();
    }

    private static synchronized <T> T init(String tips, String fileName, T defaultt, Class<T> clas) {
        String str = MyUtils.getStringFromFile(thisPath + "/conf/auto_reply/" + fileName);
        if (str == null || str.trim().isEmpty()) {
            MyUtils.putStringInFile(thisPath + "/conf/auto_reply/" + fileName, tips);
        } else {
            if (clas == Long.class)
                return (T) Long.valueOf(str);
        }
        return defaultt;
    }
}
