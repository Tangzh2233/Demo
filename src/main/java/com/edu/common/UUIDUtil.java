package com.edu.common;


import com.edu.util.IpUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static com.edu.common.UUIDUtil.getNextUid;

/**
 * @author tangzh
 * @create 2018/8/3
 */
public class UUIDUtil {
    private static AtomicInteger inc = new AtomicInteger(0);
    private static String ipHex;
    private static int max = 99999;

    static {
        ipHex = "";
        String ip = IpUtil.getLocalIpV4Addr();
        String[] split = ip.split("\\.");
        for (int i = 0; i < split.length; i++) {
            String str = split[i];
            ipHex = ipHex + Integer.toHexString(Integer.valueOf(str));
        }
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * @return seed + ip + currentTime + random(max);
     */
    public static String getNextUid(String seed) {
        int i = inc.getAndIncrement();
        if (i >= max) {
            inc.getAndSet(i % max + 1);
        }
        String current = String.valueOf(System.currentTimeMillis());
        String prefix = seed == null ? "" : seed;
        return prefix + ipHex + current + StringUtils.leftPad(String.valueOf(i), 5, '0');
    }

    public static String getNextUid(){
        return getNextUid(StringUtils.EMPTY);
    }

    public static void main(String[] args) {
        for(int i=0;i<10;i++){
            new Thread(new woker()).start();
        }
    }


}
class woker implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(getNextUid("MMP"));
        }
    }
}
