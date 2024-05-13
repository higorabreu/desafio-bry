package com.bry.crud.util;

import java.util.Base64;

public class PictureConverter {

  public static byte[] base64ToByteArray(String data) {
     byte[] decodedBytes = Base64.getDecoder().decode(data);
        return decodedBytes;
  }

  public static String byteArrayToBase64(byte[] data) {
    byte[] encodedBytes = Base64.getEncoder().encode(data);
      String encodedString = new String(encodedBytes);
        return encodedString;
  }

}
