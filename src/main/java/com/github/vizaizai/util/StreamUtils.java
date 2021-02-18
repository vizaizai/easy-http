package com.github.vizaizai.util;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author liaochongwei
 * @date 2021/1/11 17:07
 */
public class StreamUtils {
    private StreamUtils() {
    }
    /**
     * The default buffer size used why copying bytes.
     */
    public static final int BUFFER_SIZE = 4096;
    /**
     * Copy the contents of the given InputStream into a new byte array.
     * Leaves the stream open when done.
     * @param in the stream to copy from (may be {@code null} or empty)
     * @return the new byte array that has been copied to (possibly empty)
     * @throws IOException in case of I/O errors
     */
    public static byte[] copyToByteArray(InputStream in) throws IOException {
        if (in == null) {
            return new byte[0];
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
        copy(in, out);
        return out.toByteArray();
    }

    /**
     * Copy the contents of the given InputStream into a String.
     * Leaves the stream open when done.
     * @param in the InputStream to copy from (may be {@code null} or empty)
     * @return the String that has been copied to (possibly empty)
     * @throws IOException in case of I/O errors
     */
    public static String copyToString(InputStream in, Charset charset) throws IOException {
        if (in == null) {
            return "";
        }

        try {
            StringBuilder out = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(in, charset);
            char[] buffer = new char[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = reader.read(buffer)) != -1) {
                out.append(buffer, 0, bytesRead);
            }
            return out.toString();
        }finally {
            ensureClosed(in);
        }
    }

    /**
     * Copy the contents of the given byte array to the given OutputStream.
     * Leaves the stream open when done.
     * @param in the byte array to copy from
     * @param out the OutputStream to copy to
     * @throws IOException in case of I/O errors
     */
    public static void copy(byte[] in, OutputStream out) throws IOException {
        Assert.notNull(in, "No input byte array specified");
        Assert.notNull(out, "No OutputStream specified");
        out.write(in);
    }

    /**
     * Copy the contents of the given String to the given output OutputStream.
     * Leaves the stream open when done.
     * @param in the String to copy from
     * @param charset the Charset
     * @param out the OutputStream to copy to
     * @throws IOException in case of I/O errors
     */
    public static void copy(String in, Charset charset, OutputStream out) throws IOException {
        Assert.notNull(in, "No input String specified");
        Assert.notNull(charset, "No charset specified");
        Assert.notNull(out, "No OutputStream specified");

        Writer writer = new OutputStreamWriter(out, charset);
        writer.write(in);
        writer.flush();
    }

    /**
     * Copy the contents of the given InputStream to the given OutputStream.
     * Leaves both streams open when done.
     * @param in the InputStream to copy from
     * @param out the OutputStream to copy to
     * @return the number of bytes copied
     * @throws IOException in case of I/O errors
     */
    public static int copy(InputStream in, OutputStream out) throws IOException {
        Assert.notNull(in, "No InputStream specified");
        Assert.notNull(out, "No OutputStream specified");

        try {
            int byteCount = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();
            return byteCount;
        }finally {
            ensureClosed(in);
        }
    }

    public static void ensureClosed(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) { // NOPMD
            }
        }
    }

    
}
