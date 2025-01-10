package com.memo.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {

    // i: 원본 비밀번호
    // o: 해싱된 비밀번호
    public static String md5(String message) {
        // static은 new를 안 해도 이미 메모리에 올라가 있어서 나중에 그냥 쓰면 되는 거임
        String encData = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] bytes = message.getBytes();
            md.update(bytes);
            byte[] digest = md.digest();

            for (int i = 0; i < digest.length; i++) {
                encData += Integer.toHexString(digest[i] & 0xff); // 16진수로 변환하는 과정
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encData;
    }
}
