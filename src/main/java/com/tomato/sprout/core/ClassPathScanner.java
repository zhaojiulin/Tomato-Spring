package com.tomato.sprout.core;

import com.tomato.sprout.TomatoApplicationContext;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/****
 * @Description: class扫描
 * @author zhaojiulin
 * @Date 2025/10/18 0:51
 * @version 1.0
 */
public class ClassPathScanner {

    public Set<Class<?>> scan(String packagePath) {
        packagePath = packagePath.replace(".", "/");
        Set<Class<?>> classSet = new HashSet<>();
        ClassLoader classLoader = TomatoApplicationContext.class.getClassLoader();
        URL loaderResource = classLoader.getResource(packagePath);
        File file = new File(loaderResource.getFile());
        extracted(packagePath, file, classLoader, classSet);
        return classSet;
    }

    private static void extracted(String packagePath, File file, ClassLoader classLoader, Set<Class<?>> classSet) {
        File[] files = file.listFiles();
        for (File f : files) {
            if (!f.isDirectory()) {
                String absolutePath = f.getAbsolutePath();
                if (absolutePath.endsWith(".class")) {
                    String packageName = absolutePath.substring(absolutePath.indexOf(packagePath.substring(0, packagePath.indexOf("/"))), absolutePath.lastIndexOf(".class"));
                    packageName = packageName.replace("\\", ".");
                    try {
                        Class<?> clazz = classLoader.loadClass(packageName);
                        classSet.add(clazz);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                extracted(packagePath + "/" + f.getName(), f, classLoader, classSet);
            }

        }
    }
}
