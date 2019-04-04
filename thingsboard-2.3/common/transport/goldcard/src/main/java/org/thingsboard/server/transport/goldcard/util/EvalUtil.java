package org.thingsboard.server.transport.goldcard.util;

import javax.tools.*;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;

public class EvalUtil {
    private static final WeakReference<EvalUtil> mWeakReference = new WeakReference<>(new EvalUtil());

    private static final String flag = "-d";

    private static final String outDir = PropertyUtil.getProperty("eval.out.dir");

    private static final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private static final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

    public static Object eval() {
        EvalUtil eval = mWeakReference.get();

        String method = "main";

        String codes = "public static void main(String[]args){" +
                "System.out.print(\"hello world\"); }";

        eval.run(method, codes);
        return null;
    }


    public static void main(String[] args) {
        eval();
    }


    private Object run(String method, String codes) {

        String className = "com.test.Eval";
        StringBuilder sb = new StringBuilder();
        sb.append("package com.test;");
        sb.append("\n public class Eval{\n ");
        sb.append(codes);
        sb.append("\n}");

        Class<?> clazz = compile(className, sb.toString());
        try {
            // 生成对象
            Object obj = clazz.newInstance();
            Class<? extends Object> cls = obj.getClass();
            // 调用sayHello方法
            Method m = clazz.getMethod(method, String[].class);
            Object invoke = m.invoke(obj, new Object[]{new String[]{}});
            return invoke;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 装载字符串成为java可执行文件
     *
     * @param className className
     * @param javaCodes javaCodes
     * @return Class
     */
    public static Class<?> compile(String className, String javaCodes) {
        StrSrcJavaObject srcObject = new StrSrcJavaObject(className, javaCodes);
        Iterable<? extends JavaFileObject> fileObjects = Arrays.asList(srcObject);
//        String outDir = "";
//        try {
//            File classPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI());
//            outDir = classPath.getAbsolutePath() + File.separator;
//        } catch (URISyntaxException e1) {
//            e1.printStackTrace();
//        }
        Iterable<String> options = Arrays.asList(flag, outDir );
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, fileObjects);
        boolean result = task.call();
        if (result == true) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private static class StrSrcJavaObject extends SimpleJavaFileObject {
        private String content;

        StrSrcJavaObject(String name, String content) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.content = content;
        }

        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return content;
        }
    }
}
