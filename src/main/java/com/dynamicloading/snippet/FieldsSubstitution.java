package com.dynamicloading.snippet;

import ch.qos.logback.core.joran.sanity.Pair;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * This is a snippet code that helps substitute fields.
 */
public class FieldsSubstitution {
    /**
     *
     * @param object The object of which fields to be changed.
     * @param fieldObjectPath The path in the object.
     *                        e.g. There is a pojo.
     *                        class Test {
     *                              private String id;
     *                              private AddressResponse address;
     *                        };
     *                        class AddressResponse {
     *                              private String street.
     *                        };
     *                        If you want to change street, the value of fieldObjectPath will be "address".
     *                        If you want to change id, the value fieldObjectPath will be "" or null.
     * @param substitutionFields The fields that are to be changed. Key: the field name, Value: the changed value;
     * @return Changed object.
     * @throws NoSuchFieldException The passed fields are not in the object of fieldObjectPath.
     * @throws IllegalAccessException Failed to access the field.
     */
    public static <T> T substituteSpecifiedFiled(T object, String fieldObjectPath, List<Pair<String, Object>> substitutionFields) throws NoSuchFieldException, IllegalAccessException {
        if (object == null) {
            return null;
        }

        Object specifiedFieldObject;
        if (!StringUtils.hasLength(fieldObjectPath)) {
            specifiedFieldObject = object;
        } else {
            String[] pos = fieldObjectPath.split("\\.");
            specifiedFieldObject = getSpecifiedFiledObject(object, pos, 0);
        }

        for (Pair<String, Object> pair : substitutionFields) {
            Field field = specifiedFieldObject.getClass().getDeclaredField(pair.first);
            field.setAccessible(true);
            field.set(specifiedFieldObject, pair.second);
        }

        return object;
    }

    private static Object getSpecifiedFiledObject(Object fieldObject, String[] pos, int posIdx) throws IllegalAccessException, NoSuchFieldException {
        if (posIdx == pos.length) {
            return fieldObject;
        } else {
            Field field = fieldObject.getClass().getDeclaredField(pos[posIdx]);
            field.setAccessible(true);
            Object o = field.get(fieldObject);
            return getSpecifiedFiledObject(o, pos, posIdx + 1);
        }
    }
}
