package top.paakciu.pitim.constant;

import java.util.concurrent.TimeUnit;

/**
 * @author paakciu
 * @ClassName: IMConfig
 * @date: 2021/7/17 15:31
 */
public class IMConfig {
    /**
     * 客户端服务端通用的配置
     */
    //服务器的地址和端口
    public static final String HOST="localhost";
    public static final int PORT=4396;
    //魔数的字节流
    public static final byte[] MAGIC=new byte[]{'P','a','a','k'};
    //如果是为了读4个字节方便，可以使用魔数对应的int来比较结果，但是跟上者一定是要一一对应的。
    //1348559211
    public static final int MAGICINT=getIntFromBytes(MAGIC);
    public static int getIntFromBytes(byte[] by) {
        if(by.length!=4)
            return 1348559211;//这个“Paak”的字节流
        return (by[0] & 0xff) << 24 | (by[1] & 0xff) << 16 | (by[2] & 0xff) << 8 | by[3] & 0xff;
    }


    /**
     * 客户端部分配置参数
     */
    //客户端连接的重试次数
    public static final int ClientConnectionRetry=5;
//    //规定客户端处理事件的线程池大小
//    public static final int CLIENT_THREAD_POOL_NUM=3;
//    //客户端连接空闲时间,一般定为服务器的3分之一
//    public static final int CLIENT_IDLE_TIME=20;
//    //客户端心跳包的发送间隔
//    public static final int CLIENT_HEARTBEAT_INTERVAL=3;
//    //时间单位
//    public static final TimeUnit CLIENT_TIME_UNIT=TimeUnit.SECONDS;

    /**
     * 服务端部分配置参数
     */
    //mybatis的配置文件名
//    public static final String MYBATISCONFIGURATION="configuration.xml";
//    //这是规定某些包的字符串长度如果超过某个值，就会拒绝服务，防止数据包攻击或者注入攻击
//    public static final int LENGTH_LIMIT=30;
//    //规定服务端处理事件的线程池大小
//    public static final int SERVER_THREAD_POOL_NUM=4;
//    //数据库分页每页请求的大小限制
//    public static final int SERVER_DB_PAGE_LIMIT=1000;
//    //规定服务端最大连接数
//    //服务端端连接空闲时间
//    public static final int SERVER_IDLE_TIME=60;
//    //时间单位
//    public static final TimeUnit SERVER_TIME_UNIT=TimeUnit.SECONDS;

}
