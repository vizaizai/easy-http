package com.github.vizaizai.entity.form;

import com.github.vizaizai.util.StreamUtils;
import com.github.vizaizai.util.Utils;
import com.github.vizaizai.util.value.NameValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import static com.github.vizaizai.util.Utils.ASCII;

/**
 * form-data请求体编码
 * @author liaochongwei
 * @date 2021/2/10 11:08
 */
public class FormDataEncoder {

    private long length;
    private static final ByteBuffer FIELD_SEP = encode(ASCII,": ");
    private static final ByteBuffer ITEM_SEP = encode(ASCII,"; ");
    private static final ByteBuffer CR_LF = encode(ASCII,"\r\n");
    private static final ByteBuffer TWO_DASHES = encode(ASCII,"--");
    private static final ByteBuffer CONTENT_DISPOSITION = encode(ASCII,"Content-Disposition");
    private static final ByteBuffer CONTENT_TYPE = encode(ASCII,"Content-Type");
    private static final ByteBuffer FORM_DATA = encode(ASCII,"form-data");

    private static ByteBuffer encode(
            final Charset charset, final String string) {
        return charset.encode(CharBuffer.wrap(string));
    }

    private  void writeBytes(
            final ByteBuffer b, final OutputStream out) throws IOException {
        length = length + b.remaining();
        out.write(b.array(), 0, b.remaining());
    }

    private  void writeBytes(
            final String s, final Charset charset, final OutputStream out) throws IOException {
        final ByteBuffer b = encode(charset, s);
        writeBytes(b, out);
    }

    private void write(
            final InputStream is, final OutputStream out) throws IOException {
        length = length + is.available();
        StreamUtils.copy(is, out);
    }

    public void encode(FormBodyParts parts, OutputStream os, Charset charset) throws IOException {

        for (NameValue<String, BodyContent> nameValue : parts) {
            BodyContent value = nameValue.getValue();
            if (value == null) {
                continue;
            }
            /* ----------boundary start--------*/
            writeBytes(TWO_DASHES, os); // --
            writeBytes(parts.getBoundary(), charset, os); // boundary
            writeBytes(CR_LF, os); // 换行
            /* ----------boundary end--------*/

            /* -----------Content-Disposition start--------*/
            writeBytes(CONTENT_DISPOSITION, os); // Content-Disposition
            writeBytes(FIELD_SEP, os); // :
            writeBytes(FORM_DATA, os); // form-data
            writeBytes(ITEM_SEP, os); // ;
            writeBytes("name=\"" + nameValue.getName() + "\"", charset, os); // name=""
            // 文件
            if (value.isFile()) {
                String filename = value.getFilename() == null ? Utils.uuid() : value.getFilename();
                writeBytes(ITEM_SEP, os); // ;
                writeBytes("filename=\"" + filename + "\"", charset, os); // filename=""
            }
            writeBytes(CR_LF, os); // 换行
            /* -----------Content-Disposition end--------*/

            /* -----------Content-Type start--------*/
            if (value.isFile() && value.getContentType() != null) {
                writeBytes(CONTENT_TYPE, os); // Content-Type
                writeBytes(FIELD_SEP, os); // :
                writeBytes(value.getContentType(), charset, os);
                writeBytes(CR_LF, os); // 换行
            }
            /* -----------Content-Type end--------*/

            /* -----------空行 start--------*/
            writeBytes(CR_LF, os); // 换行
            /* -----------空行 end--------*/

            /* -----------值 start--------*/
            write(value.getInputStream(), os);
            writeBytes(CR_LF, os); // 换行
            /* -----------值 end--------*/
        }
        /* ----------boundary start--------*/
        writeBytes(TWO_DASHES, os); // --
        writeBytes(parts.getBoundary(), charset, os); // boundary
        writeBytes(TWO_DASHES, os); // --
        writeBytes(CR_LF, os); // 换行
        /* ----------boundary end--------*/

    }

    public long getLength() {
        return length;
    }
}
