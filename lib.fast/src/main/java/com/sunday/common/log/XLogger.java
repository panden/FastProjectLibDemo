package com.sunday.common.log;

import com.sunday.common.activity.BaseApplication;
import com.sunday.common.log.xlog.LogConfiguration;
import com.sunday.common.log.xlog.LogLevel;
import com.sunday.common.log.xlog.Logger;
import com.sunday.common.log.xlog.XLog;
import com.sunday.common.log.xlog.printer.AndroidPrinter;
import com.sunday.common.log.xlog.printer.ConsolePrinter;
import com.sunday.common.log.xlog.printer.Printer;
import com.sunday.common.log.xlog.printer.file.FilePrinter;
import com.sunday.common.log.xlog.printer.file.backup.FileSizeBackupStrategy;
import com.sunday.common.log.xlog.printer.file.backup.NeverBackupStrategy;
import com.sunday.common.log.xlog.printer.file.naming.DateFileNameGenerator;
import com.sunday.common.utils.FileUtils;

public class XLogger extends Lg.Logger {

    private Logger mLogger;

    public XLogger(String tag, boolean isDebug) {
        super(tag, isDebug);
    }

    @Override
    protected void initLogger() {
        String logDirName = BaseApplication.getInstance().getBuildConfig().getAppName(BaseApplication.getContext());
        String logDir = FileUtils.getAppFileDir(BaseApplication.getContext(), logDirName).getPath();
        LogConfiguration config = new LogConfiguration.Builder()
                .logLevel(isDebug() ? LogLevel.ALL             // 指定日志级别，低于该级别的日志将不会被打印，默认为 LogLevel.ALL
                        : LogLevel.NONE)
                .tag(getTag())                                         // 指定 TAG，默认为 "X-LOG"
                .t()                                                   // 允许打印线程信息，默认禁止
                .st(2)                                                 // 允许打印深度为2的调用栈信息，默认禁止
                .b()                                                   // 允许打印日志边框，默认禁止
                .build();

        Printer androidPrinter = new AndroidPrinter();             // 通过 android.util.Log 打印日志的打印器
        Printer consolePrinter = new ConsolePrinter();             // 通过 System.out 打印日志到控制台的打印器
        Printer filePrinter = new FilePrinter                      // 打印日志到文件的打印器
                .Builder(logDir)                              // 指定保存日志文件的路径
                .fileNameGenerator(new DateFileNameGenerator())        // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
                .backupStrategy(isDebug() ? new FileSizeBackupStrategy(1024 * 1024) : new NeverBackupStrategy())             // 指定日志文件备份策略，默认为 FileSizeBackupStrategy(1024 * 1024)
                .build();

        XLog.init(                                                 // 初始化 XLog
                config,                                                // 指定日志配置，如果不指定，会默认使用 new LogConfiguration.Builder().build()
                androidPrinter,                                        // 添加任意多的打印器。如果没有添加任何打印器，会默认使用 AndroidPrinter(Android)/ConsolePrinter(java)
                consolePrinter,
                filePrinter);
    }

    @Override
    protected void createLogger() {
        mLogger = XLog.tag(getTag()).build();
    }

    @Override
    public void i(String msg, Object... args) {
        mLogger.i(msg, args);
    }

    @Override
    public void d(String msg, Object... args) {
        mLogger.d(msg, args);
    }

    @Override
    public void e(String msg, Object... args) {
        mLogger.e(msg, args);
    }

    @Override
    public void w(String msg, Object... args) {
        mLogger.w(msg, args);
    }

    @Override
    public void v(String msg, Object... args) {
        mLogger.v(msg, args);
    }
}
