/**
 * Autogenerated by Thrift Compiler (0.9.0-dev)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package vng.ppt.captchaservice.thrift;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum TCaptchaResponseCode implements org.apache.thrift.TEnum {
  VALID_SUCCESS(0),
  INVALID_REQUEST(1),
  DECRYPT_FAILED(2),
  IP_INVALID(3),
  TOKEN_EXPIRED(4),
  TOKEN_INBLACKLIST(5),
  PRIVATEKEY_NOTFOUND(6),
  CAPTCHA_NOTMATCH(7),
  ERROR(8);

  private final int value;

  private TCaptchaResponseCode(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static TCaptchaResponseCode findByValue(int value) { 
    switch (value) {
      case 0:
        return VALID_SUCCESS;
      case 1:
        return INVALID_REQUEST;
      case 2:
        return DECRYPT_FAILED;
      case 3:
        return IP_INVALID;
      case 4:
        return TOKEN_EXPIRED;
      case 5:
        return TOKEN_INBLACKLIST;
      case 6:
        return PRIVATEKEY_NOTFOUND;
      case 7:
        return CAPTCHA_NOTMATCH;
      case 8:
        return ERROR;
      default:
        return null;
    }
  }
}