/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.fe.service;

import mto.billing.fe.entity.MbEntity.MbJtoken;
import mto.billing.fe.main.Configuration;
import org.apache.log4j.Logger;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import zcore.utilities.AES256;
import zcore.utilities.CommonUtil;

/**
 *
 * @author huuloc.tran89
 */
public class JwtService {

    private static final Logger logger = Logger.getLogger(JwtService.class);
    private static final AES256 AES256 = new AES256();

    private static String encryptJwtData(String data) {
        String encStr = null;
        try {
            encStr = AES256.encrypt(Configuration.JTOKEN_SECRET_KEY, data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
            logger.warn("Excetion encrypt data: " + ex.getMessage(), ex);
        }
        return encStr;
    }

    private static String decryptJwtData(String encData) {
        String oriStr = null;
        try {
            oriStr = AES256.decrypt(Configuration.JTOKEN_SECRET_KEY, encData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
            logger.warn("Excetion encrypt data: " + ex.getMessage(), ex);
        }
        return oriStr;
    }

    public static String createJwtSession(MbJtoken mbJtoken) {
        try {
            String jtoken = Jwts.builder()
                    .setSubject("MB")
                    .setExpiration(new Date(System.currentTimeMillis() + (Configuration.JTOKEN_EXPIRE_TIME_IN_SECOND * 1000L)))
                    .claim("appID", encryptJwtData(String.valueOf(mbJtoken.appID)))
                    .claim("clientID", encryptJwtData(String.valueOf(mbJtoken.clientID)))
                    .claim("userID", encryptJwtData(mbJtoken.userID))
                    .claim("loginType", encryptJwtData(mbJtoken.loginType))
                    .claim("loginMethod", encryptJwtData(mbJtoken.loginMethod))
                    .claim("roleID", encryptJwtData(mbJtoken.roleID))
                    .claim("roleName", encryptJwtData(mbJtoken.roleName))
                    .claim("serverID", encryptJwtData(mbJtoken.serverID))
                    .claim("userIP", encryptJwtData(mbJtoken.userIP))
                    .signWith(SignatureAlgorithm.HS512, Configuration.JTOKEN_SECRET_KEY)
                    .compact();

            return jtoken;
        } catch (Exception ex) {
            logger.error(ex.getMessage() + " - mbJtoken: " + CommonUtil.objectToString(mbJtoken), ex );
        }
        return null;
    }

    public static MbJtoken getDataFromJwtSession(String jtoken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(Configuration.JTOKEN_SECRET_KEY).parseClaimsJws(jtoken);

            String appIDEnc = claims.getBody().get("appID").toString();
            String clientIDEnc = claims.getBody().get("clientID").toString();
            String userIDEnc = claims.getBody().get("userID").toString();
            String loginTypeEnc = claims.getBody().get("loginType").toString();
            String loginMethodEnc = claims.getBody().get("loginMethod").toString();
            String roleIDEnc = claims.getBody().get("roleID").toString();
            String roleNameEnc = claims.getBody().get("roleName").toString();
            String serverIDEnc = claims.getBody().get("serverID").toString();
            String userIPEnc = claims.getBody().get("userIP").toString();
            return new MbJtoken(Integer.parseInt(decryptJwtData(appIDEnc)),
                    Integer.parseInt(decryptJwtData(clientIDEnc)),
                    decryptJwtData(loginMethodEnc),
                    decryptJwtData(userIDEnc),
                    decryptJwtData(loginTypeEnc),
                    decryptJwtData(roleIDEnc),
                    decryptJwtData(roleNameEnc),
                    decryptJwtData(serverIDEnc),
                    decryptJwtData(userIPEnc)
            );
        } catch (Exception ex) {
            logger.warn(ex.getMessage() + " - jtoken: " + jtoken);
        }
        return null;
    }

    public static void deleteJwtSession(String jtoken, int appID) {

    }

    public static void main(String[] args) {
        String a = "SyrYeUAUbGc3AYPc";
        byte[] bytes = a.getBytes();
        System.out.println(Byte.SIZE);
        System.out.println(bytes.length * Byte.SIZE);
    }
}
