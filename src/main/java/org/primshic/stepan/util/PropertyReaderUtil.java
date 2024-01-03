package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyReaderUtil {

    public static String read(String fileName, String key){
        Properties properties = new Properties();
        try(FileInputStream in = new FileInputStream(fileName)) {
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e); //todo
        }
        return properties.getProperty(key);
    }
}
