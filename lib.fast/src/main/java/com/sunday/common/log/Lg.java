package com.sunday.common.log;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;

public class Lg {

    private static boolean sIsDebug = true;
    private static String sTag = "FastLog";
    private static Logger sLogger;
    private static Class<? extends Logger> sLoggerClazz = DefaultLogger.class;

    private Lg(){
    }

    /**设置默认下log*/
    public static void setLog(Class<? extends Logger> logClass, String tag, boolean isDebug) {
        try {
            if(logClass == null || tag == null)return;
            sLogger = logClass.getDeclaredConstructor(new Class[]{String.class, boolean.class}).newInstance(tag, isDebug);
            sLogger.initLogger();
            sLogger.createLogger();
            sLoggerClazz = logClass;
            sTag = tag;
            sIsDebug = isDebug;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**创建一个指定tag的log,并单独设置是否开启debug*/
    public static Logger createTagLog(String tag, boolean isDebug){
        Logger logger = null;
        try {
            logger = sLoggerClazz.getDeclaredConstructor(new Class[]{String.class, boolean.class}).newInstance(tag, isDebug);
            logger.createLogger();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return logger;
    }

    /**创建一个指定tag的log*/
    public static Logger createTagLog(String tag){
        Logger logger = null;
        try {
            logger = sLoggerClazz.getDeclaredConstructor(new Class[]{String.class, boolean.class}).newInstance(tag, sIsDebug);
            logger.createLogger();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return logger;
    }

    private static Logger getLogger(){
        if(sLogger == null){
            synchronized (sLoggerClazz){
                sLogger = createTagLog(sTag, sIsDebug);
            }
        }
        return sLogger;
    }


    public static void i(String msg, Object... args){
        getLogger().i(msg, args);
    }

    public static void d(String msg, Object... args){
        getLogger().d(msg, args);
    }

    public static void e(String msg, Object... args){
        getLogger().e(msg, args);
    }

    public static void w(String msg, Object... args){
        getLogger().w(msg, args);
    }

    public static void v(String msg, Object... args){
        getLogger().v(msg, args);
    }


    public static abstract class Logger{

        private String tag;
        private boolean isDebug;

        public Logger(String tag, boolean isDebug){
            this.tag = tag;
            this.isDebug = isDebug;
        }

        public boolean isDebug() {
            return isDebug;
        }

        public String getTag() {
            return tag;
        }

        /**初始化全局logb并创建全局log*/
        protected abstract void initLogger();

        /**创建局部log*/
        protected abstract void createLogger();

        public abstract void i(String msg, Object... args);

        public abstract void d(String msg, Object... args);

        public abstract void e(String msg, Object... args);

        public abstract void w(String msg, Object... args);

        public abstract void v(String msg, Object... args);

    }

    private static class DefaultLogger extends Logger{

        public DefaultLogger(String tag, boolean isDebug) {
            super(tag, isDebug);
        }

        @Override
        protected void initLogger() {
        }

        @Override
        protected void createLogger() {
        }

        @Override
        public void i(String msg, Object... args) {
            Log.i(getTag(), String.format(msg, args));
        }

        @Override
        public void d(String msg, Object... args) {
            Log.d(getTag(), String.format(msg, args));
        }

        @Override
        public void e(String msg, Object... args) {
            Log.e(getTag(), String.format(msg, args));
        }

        @Override
        public void w(String msg, Object... args) {
            Log.w(getTag(), String.format(msg, args));
        }

        @Override
        public void v(String msg, Object... args) {
            Log.v(getTag(), String.format(msg, args));
        }
    }
}
