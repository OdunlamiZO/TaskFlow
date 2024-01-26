package odunlamizo.taskflow.auth.util;

import java.lang.reflect.Field;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public final class Objects {

    private Objects() {}

    public static MultiValueMap<String, String> convertObjectToParams(Object object) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Class<?> clazz = object.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                if (value != null) {
                    params.add(field.getName(), value.toString());
                }
            } catch (IllegalAccessException e) {
                // Handle exception
            }
        }

        return params;
    }
    
}
