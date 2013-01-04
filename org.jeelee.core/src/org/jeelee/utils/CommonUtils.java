package org.jeelee.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CommonUtils
{
  public static <T extends Serializable> T depthClone(T object)
  {
    try
    {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(object);

      ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bis);
      return (T)ois.readObject();
    } catch (Exception e) {
      AppLogging.handleException(e);
    }
    return null;
  }
}

