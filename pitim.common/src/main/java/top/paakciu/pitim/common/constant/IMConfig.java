package top.paakciu.pitim.common.constant;


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

}
