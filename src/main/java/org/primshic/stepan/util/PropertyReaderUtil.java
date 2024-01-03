package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyReaderUtil {

    public static String read(String fileName, String key){
        Properties properties = new Properties();
        try (InputStream in = PropertyReaderUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            if (in == null) {
                throw new RuntimeException("Unable to find file: " + fileName);
            }
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty(key);
    }
}
