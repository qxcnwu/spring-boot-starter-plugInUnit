package com.qxc.utiles.reflectancetools;

import com.qxc.threadexector.eventbus.Subscribe;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * @Author qxc
 * @Date 2023 2023/7/12 23:53
 * @Version 1.0
 * @PACKAGE com.qxc.utiles
 */
@Slf4j
public final class ClassUtiles {
    private final static String SUFFIX = ".class";

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public static @NotNull ArrayList<Class<?>> getAllClassWithAnnotation(String packageName, Class<? extends Annotation> clazz) throws ClassNotFoundException, IOException {
        ArrayList<Class<?>> arr = new ArrayList<>();
        packageName = packageName.replaceAll("\\.", "/");
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageName);
        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            if ("file".equals(url.getProtocol())) {
                List<File> classes = new ArrayList<>();
                // 递归 变量路径下面所有的 class文件
                listFiles(new File(url.getFile()), classes);
                // 加载我们所有的 class文件 就行了
                arr.addAll(loadClasses(classes, packageName, clazz));
            }
        }
        return arr;
    }

    /**
     * 获取所有具有Subscriber方法的类
     *
     */
    @Contract(pure = true)
    public static @NotNull ArrayList<ClazzWithMethod> getAllMethodWithAnnotation(String packageName) throws ClassNotFoundException, IOException {
        ArrayList<ClazzWithMethod> arr = new ArrayList<>();
        packageName = packageName.replaceAll("\\.", "/");
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageName);
        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            if ("file".equals(url.getProtocol())) {
                List<File> classes = new ArrayList<>();
                // 递归 变量路径下面所有的 class文件
                listFiles(new File(url.getFile()), classes);
                // 加载我们所有的 class文件 就行了
                arr.addAll(loadClasses(classes, packageName));
            }
        }
        return arr;
    }

    /**
     * 获取所有包含Subscriber的方法
     *
     */
    public static @NotNull List<ClazzWithMethod> loadClasses(@NotNull List<File> classes, String scan) throws ClassNotFoundException {
        List<ClazzWithMethod> clazzes = new ArrayList<>();
        for (File file : classes) {
            String fPath = file.getAbsolutePath().replaceAll("\\\\", "/");
            String packageName = fPath.substring(fPath.lastIndexOf(scan));
            packageName = packageName.replace(".class", "").replaceAll("/", ".");
            // 根据名称加载类
            final Class<?> aClass = Class.forName(packageName);
            ClazzWithMethod clazzWithMethod = new ClazzWithMethod(aClass);
            if (clazzWithMethod.isAnnotation()) {
                clazzes.add(clazzWithMethod);
            }
        }
        return clazzes;
    }

    @SuppressWarnings("MissingJavadoc")
    public static @NotNull List<Class<?>> loadClasses(@NotNull List<File> classes, String scan, Class<? extends Annotation> clazz) throws ClassNotFoundException {
        List<Class<?>> clazzes = new ArrayList<>();
        for (File file : classes) {
            String fPath = file.getAbsolutePath().replaceAll("\\\\", "/");
            String packageName = fPath.substring(fPath.lastIndexOf(scan));
            packageName = packageName.replace(".class", "").replaceAll("/", ".");
            // 根据名称加载类
            final Class<?> aClass = Class.forName(packageName);
            if (aClass.isAnnotationPresent(clazz) && !aClass.isInterface()) {
                clazzes.add(aClass);
            }
        }
        return clazzes;
    }

    @SuppressWarnings("MissingJavadoc")
    public static void listFiles(@NotNull File dir, List<File> fileList) {
        if (dir.isDirectory()) {
            for (File f : Objects.requireNonNull(dir.listFiles())) {
                listFiles(f, fileList);
            }
        } else {
            if (dir.getName().endsWith(SUFFIX)) {
                fileList.add(dir);
            }
        }
    }


    /**
     * 包含所有Subscriber注解的类
     */
    @Data
    public static class ClazzWithMethod {
        Class<?> clazz;
        boolean isAnnotation;
        ArrayList<Method> arr;

        @SuppressWarnings("MissingJavadoc")
        public ClazzWithMethod(@NotNull Class<?> clazz) {
            this.clazz = clazz;
            arr = new ArrayList<>();
            final Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method m : declaredMethods) {
                if (m.isAnnotationPresent(Subscribe.class)) {
                    arr.add(m);
                }
            }
            isAnnotation = arr.size() != 0;
        }
    }
}
