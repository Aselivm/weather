package org.primshic.stepan.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenWeatherAPIUtil {
    private static final String API_KEY = System.getenv("API_KEY");
    public static String getApiKey(){
        return API_KEY;
    }
}
