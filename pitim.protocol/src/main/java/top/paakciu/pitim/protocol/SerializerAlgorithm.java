package top.paakciu.pitim.protocol;

import top.paakciu.pitim.protocol.serializer.JSONSerializer;
import top.paakciu.pitim.protocol.serializer.Serializer;


/**
 * 指示用什么序列化方法的常量类（工厂类）
 * SerializerAlgorithm
 * @author paakciu
 * @date 2020/12/21 10:04
 */
public final class SerializerAlgorithm {
    /**
     * json序列化标识
     */
    public final static byte JSON=1;

    /**
     * 默认使用JSON序列化
     */
    public final static byte DEFAULT=JSON;

    /**
     * 取出序列器
     * @param serializeAlgorithm
     * @return
     */
    public static Serializer getSerializer(byte serializeAlgorithm)
    {
        if(JSON==serializeAlgorithm)
            return new JSONSerializer();
        return new JSONSerializer();
    }

}
