package com.sunday.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * IO 工具类
 */
public class IOUtil {

    private static final int BUFFER_SIZE = 1024 * 8;

    public static byte[] readAllBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copyAllBytes(in, out);
        return out.toByteArray();
    }

    public static byte[] readAllBytesAndClose(InputStream in) throws IOException {
        try {
            return readAllBytes(in);
        } finally {
            safeClose(in);
        }
    }

    public static String readAllChars(Reader reader) throws IOException {
        char[] buffer = new char[BUFFER_SIZE / 2];
        StringBuilder builder = new StringBuilder();
        while (true) {
            int read = reader.read(buffer);
            if (read == -1) {
                break;
            }
            builder.append(buffer, 0, read);
        }
        return builder.toString();
    }

    public static String readAllCharsAndClose(Reader reader) throws IOException {
        try {
            return readAllChars(reader);
        } finally {
            safeClose(reader);
        }
    }

    public static void writeAllCharsAndClose(Writer writer, CharSequence text) throws IOException {
        try {
            writer.append(text);
        } finally {
            safeClose(writer);
        }
    }

    public static int copyAllBytes(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        while (true) {
            int read = in.read(buffer);
            if (read == -1) {
                break;
            }
            out.write(buffer, 0, read);
            byteCount += read;
        }
        return byteCount;
    }


    public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

}
