package org.jeelee.filemanager.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelUtils
{
  public static byte[] readBytes(int offset, FileChannel fc, ByteBuffer buffer)
    throws IOException
  {
    fc.position(offset);
    buffer.clear();
    fc.read(buffer);
    return buffer.array();
  }

  public static byte readByte(int offset, FileChannel fc, ByteBuffer buffer) throws IOException
  {
    return readBytes(offset, fc, buffer)[0];
  }

  public static int bytesToWord(byte[] bytes, int off) {
    return (bytes[(off + 1)] & 0xFF) << 8 | bytes[off] & 0xFF;
  }

  public static String readString(int finalname_offset, FileChannel fc) throws IOException
  {
    fc.position(finalname_offset);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ByteBuffer buffer = ByteBuffer.allocate(1);
    while ((fc.read(buffer) > 0) && (buffer.get(0) != 0)) {
      bos.write(buffer.array());
      buffer.clear();
    }

    byte[] bys = bos.toByteArray();

    return new String(bys);
  }
}

