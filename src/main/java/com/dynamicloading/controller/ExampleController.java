package com.dynamicloading.controller;

import com.dynamicloading.ClassFileClassLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Controller
public class ExampleController {
    public void dynamicLoading(@RequestParam("class") MultipartFile classFile, @RequestParam String packagePath,
                               @RequestParam String methodName, @RequestParam Object[] args)
            throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        ClassFileClassLoader classFileClassLoader = new ClassFileClassLoader();
        String className = classFile.getOriginalFilename().split("\\.")[0];
        Class<?> clazz = classFileClassLoader.defineClass(packagePath + "." + className, classFile.getInputStream());
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                Object o = clazz.getConstructor().newInstance();
                m.invoke(o, args);
                return;
            }
        }
    }
}
