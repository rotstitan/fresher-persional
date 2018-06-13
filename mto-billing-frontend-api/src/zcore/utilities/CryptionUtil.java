package zcore.utilities;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;

public class CryptionUtil {

    public static String encrypt(int prefixNumChar, String key, String strPlain)
            throws GeneralSecurityException, UnsupportedEncodingException, DecoderException {
        byte[] raw = key.getBytes(Charset.forName("UTF-8"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
        byte[] doFinal = cipher.doFinal((RandomStringUtils.randomAlphabetic(prefixNumChar) + strPlain).getBytes(Charset.forName("UTF-8")));
//        System.out.println("decodeHexString : " + encodeHexString);
//        return new String(Base64.encodeBase64(cipher.doFinal((RandomStringUtils.randomAlphabetic(prefixNumChar) + strPlain).getBytes(Charset.forName("UTF-8")))), Charset.forName("UTF-8"));
        return Hex.encodeHexString(
                cipher.doFinal((RandomStringUtils.randomAlphabetic(prefixNumChar) + strPlain)
                        .getBytes(Charset.forName("UTF-8"))));
    }

    public static String decrypt(int prefixNumChar, String key, String strEncrypted)
            throws GeneralSecurityException, DecoderException {
//        byte[] encrypted = Base64.decodeBase64(strEncrypted.getBytes(Charset.forName("UTF-8")));
        byte[] encrypted = Hex.decodeHex(strEncrypted.toCharArray());

        byte[] raw = key.getBytes(Charset.forName("UTF-8"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));

        byte[] original = cipher.doFinal(encrypted);
        return new String(original, Charset.forName("UTF-8")).substring(prefixNumChar);
    }

    public static void main(String[] args) throws GeneralSecurityException, UnsupportedEncodingException, DecoderException {
        String key = "hahahahahahahaha";
        System.out.println(key.length());
        String encrypt = encrypt(32, key, "huhuhuhuhuhuhu");
        System.out.println("encrypt: " + encrypt + " - len: " + encrypt.length());
        System.out.println("decrypt: " + decrypt(32, key, encrypt));
    }
}
