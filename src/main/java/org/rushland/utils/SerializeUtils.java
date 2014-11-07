package org.rushland.utils;

import lombok.SneakyThrows;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;

/**
 * Managed by romain on 01/11/2014.
 */
//Was used for plugin_messages
public class SerializeUtils {
    @SneakyThrows
    public static String serialize(Serializable o) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return new String(Base64Coder.encode(baos.toByteArray()));
    }

    @SneakyThrows
    public static Object deserialize(String s) {
        byte [] data = Base64Coder.decode(s);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o  = ois.readObject();
        ois.close();
        return o;
    }
}
