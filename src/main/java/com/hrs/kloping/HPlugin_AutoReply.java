package com.hrs.kloping;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @version 0.1
 * @Author HRS 3474006766@qq.com
 * @Date 21/9/16
 */
public final class HPlugin_AutoReply extends JavaPlugin {
    public static final HPlugin_AutoReply INSTANCE = new HPlugin_AutoReply();
    public static String key = "开始添加";
    public static String selectKey = "查询词";
    public static String deleteKey = "删除词";
    public static Long host = -1L;
    public static List<Long> followers = new LinkedList<>();
    public static final String splitK = ":==>";
    public static final ExecutorService threads = Executors.newFixedThreadPool(10);
    public static final Map<Number, entity> list2e = new ConcurrentHashMap<>();
    public static final Map<String, MessageChain> k2v = new ConcurrentHashMap<>();
    public static String thisPath = System.getProperty("user.dir");
    public static String OneComAddStr = "/添加";
    public static String OneComAddSplit = " ";

    private static void Init() {
        thisPath = thisPath == null ? "." : thisPath;
        Initer.Init();
    }

    private HPlugin_AutoReply() {
        super(new JvmPluginDescriptionBuilder("com.hrs.kloping.h_plugin_AutoReply", "0.12")
                .name("插件_3 Author => HRS")
                .info("自定义回话插件")
                .author("HRS")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("HRS's SImg Plugin loaded!");
        Init();
        if (host == -1) {
            System.err.println("请在/conf/auto_reply/host设置您的QQ以控制你的机器人");
        }
        GlobalEventChannel.INSTANCE.registerListenerHost(new SimpleListenerHost() {
            @Override
            public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
                super.handleException(context, exception);
            }

            @EventHandler
            public void handleMessage(GroupMessageEvent event) {
                threads.execute(() -> {
                    OnCommand.onHandler(event);
                });
            }

            @EventHandler
            public void handleMessage(FriendMessageEvent event) {
            }
        });
    }

    public static synchronized void flushMap(entity entity) {
        String k = entity.getK();
        MessageChain message = entity.getV();
        String v = message.serializeToMiraiCode();
        String line = k + splitK + v;
        MyUtils.appendStringInFile(thisPath + "/conf/auto_reply/data.data", line, true);
        k2v.put(k, message);
    }

    public static synchronized void resourceMap() {
        List<String> list = new LinkedList<>();
        for (String k : k2v.keySet()) {
            MessageChain message = k2v.get(k);
            String v = message.serializeToMiraiCode();
            String line = k + splitK + v;
            list.add(line);
        }
        MyUtils.putStringInFile(thisPath + "/conf/auto_reply/data.data", list.toArray(new String[0]));
    }
}