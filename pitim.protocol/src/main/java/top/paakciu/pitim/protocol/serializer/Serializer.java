package top.paakciu.pitim.protocol.serializer;

/**
 * 2021/7/27 23:17 wubaizhao1
 * @date: 2021/7/27 23:13
 */
public interface Serializer {
    /**
     * 序列化算法，获取具体的序列化算法标识
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * java对象转成二进制
     * @param object
     * @return
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成java对象
     * @param clazz
     * @param bytes
     * @param <M>
     * @return
     */
    <M> M deserialize(Class<M> clazz,byte[] bytes);
}
