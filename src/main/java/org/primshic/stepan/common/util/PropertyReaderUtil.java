package org.primshic.stepan.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.common.exception.ApplicationException;
import org.primshic.stepan.common.exception.ErrorMessage;

import java.io.*;
import java.util.Properties;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyReaderUtil {
    public static String read(String fileName, String key) {
        Properties properties = new Properties();
        try (InputStream in = PropertyReaderUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            if (in == null) {
                log.error("Unable to locate file '{}'", fileName);
                throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
            }
            properties.load(in);
        } catch (IOException e) {
            log.error("Error while reading properties from file '{}'", fileName, e);
            throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
        }

        String propertyValue = properties.getProperty(key);
        log.info("Read property '{}' with value '{}'", key, propertyValue);
        return propertyValue;
    }
}
