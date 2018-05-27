/*
 * Copyright 2016 Elvis Hew
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sunday.common.log.xlog.internal;

import android.content.Intent;
import android.os.Bundle;

import com.sunday.common.log.xlog.flattener.DefaultFlattener;
import com.sunday.common.log.xlog.flattener.Flattener;
import com.sunday.common.log.xlog.formatter.border.BorderFormatter;
import com.sunday.common.log.xlog.formatter.border.DefaultBorderFormatter;
import com.sunday.common.log.xlog.formatter.message.json.DefaultJsonFormatter;
import com.sunday.common.log.xlog.formatter.message.json.JsonFormatter;
import com.sunday.common.log.xlog.formatter.message.object.BundleFormatter;
import com.sunday.common.log.xlog.formatter.message.object.IntentFormatter;
import com.sunday.common.log.xlog.formatter.message.object.ObjectFormatter;
import com.sunday.common.log.xlog.formatter.message.throwable.DefaultThrowableFormatter;
import com.sunday.common.log.xlog.formatter.message.throwable.ThrowableFormatter;
import com.sunday.common.log.xlog.formatter.message.xml.DefaultXmlFormatter;
import com.sunday.common.log.xlog.formatter.message.xml.XmlFormatter;
import com.sunday.common.log.xlog.formatter.stacktrace.DefaultStackTraceFormatter;
import com.sunday.common.log.xlog.formatter.stacktrace.StackTraceFormatter;
import com.sunday.common.log.xlog.formatter.thread.DefaultThreadFormatter;
import com.sunday.common.log.xlog.formatter.thread.ThreadFormatter;
import com.sunday.common.log.xlog.printer.Printer;
import com.sunday.common.log.xlog.printer.file.FilePrinter;
import com.sunday.common.log.xlog.printer.file.backup.BackupStrategy;
import com.sunday.common.log.xlog.printer.file.backup.FileSizeBackupStrategy;
import com.sunday.common.log.xlog.printer.file.naming.ChangelessFileNameGenerator;
import com.sunday.common.log.xlog.printer.file.naming.FileNameGenerator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for providing default implementation.
 */
public class DefaultsFactory {

  private static final String DEFAULT_LOG_FILE_NAME = "log";

  private static final long DEFAULT_LOG_FILE_MAX_SIZE = 1024 * 1024; // 1M bytes;

  private static final Map<Class<?>, ObjectFormatter<?>> BUILTIN_OBJECT_FORMATTERS;

  static {
    Map<Class<?>, ObjectFormatter<?>> objectFormatters = new HashMap<>();
    objectFormatters.put(Bundle.class, new BundleFormatter());
    objectFormatters.put(Intent.class, new IntentFormatter());
    BUILTIN_OBJECT_FORMATTERS = Collections.unmodifiableMap(objectFormatters);
  }

  /**
   * Create the default JSON formatter.
   */
  public static JsonFormatter createJsonFormatter() {
    return new DefaultJsonFormatter();
  }

  /**
   * Create the default XML formatter.
   */
  public static XmlFormatter createXmlFormatter() {
    return new DefaultXmlFormatter();
  }

  /**
   * Create the default throwable formatter.
   */
  public static ThrowableFormatter createThrowableFormatter() {
    return new DefaultThrowableFormatter();
  }

  /**
   * Create the default thread formatter.
   */
  public static ThreadFormatter createThreadFormatter() {
    return new DefaultThreadFormatter();
  }

  /**
   * Create the default stack trace formatter.
   */
  public static StackTraceFormatter createStackTraceFormatter() {
    return new DefaultStackTraceFormatter();
  }

  /**
   * Create the default border formatter.
   */
  public static BorderFormatter createBorderFormatter() {
    return new DefaultBorderFormatter();
  }

  /**
   * Create the default log flattener.
   */
  public static Flattener createFlattener() {
    return new DefaultFlattener();
  }

  /**
   * Create the default printer.
   */
  public static Printer createPrinter() {
    return Platform.get().defaultPrinter();
  }

  /**
   * Create the default file name generator for {@link FilePrinter}.
   */
  public static FileNameGenerator createFileNameGenerator() {
    return new ChangelessFileNameGenerator(DEFAULT_LOG_FILE_NAME);
  }

  /**
   * Create the default backup strategy for {@link FilePrinter}.
   */
  public static BackupStrategy createBackupStrategy() {
    return new FileSizeBackupStrategy(DEFAULT_LOG_FILE_MAX_SIZE);
  }

  /**
   * Get the builtin object formatters.
   *
   * @return the builtin object formatters
   */
  public static Map<Class<?>, ObjectFormatter<?>> builtinObjectFormatters() {
    return BUILTIN_OBJECT_FORMATTERS;
  }
}
