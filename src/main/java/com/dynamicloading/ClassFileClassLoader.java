package com.dynamicloading;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClassFileClassLoader extends ClassLoader {
    public ClassFileClassLoader() {
        super(ClassFileClassLoader.class.getClassLoader());
    }

    public Class<?> defineClass(String name, InputStream source) throws IOException {
        byte[] definition;
        try(InputStream classFile = source) {
            byte[] buff = new byte[1024 * 4];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            while ((len = source.read(buff)) != -1) {
                baos.write(buff, 0, len);
            }
            definition = baos.toByteArray();
        }
        return super.defineClass(name, definition, 0, definition.length);
    }
}
