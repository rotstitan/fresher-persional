/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.backup;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import mto.billing.fe.main.Configuration;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.http.HttpMethod;
import zcore.utilities.CommonUtil;

/**
 * x
 *
 * @author huuloc.tran89
 */
public class ApiTest {

//    public static String DOMAIN = "https://dev-billing.mto.zing.vn";
    public static String DOMAIN = "https://sandbox-billing.mto.zing.vn";
//    public static String DOMAIN = "https://staging-billing.mto.zing.vn";
//    public static String DOMAIN = "https://billing.mto.zing.vn";

    public static void getInitConfig(String clientKey) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("clientKey", clientKey));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/config/getDisplayInfo", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void getLogin(String clientKey, String ppParams) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("clientKey", clientKey));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8) + "&" + ppParams;
        String response = CommonUtil.curl(DOMAIN + "/fe/api/auth/login", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void getServers(String jtoken) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/store/getServers", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void getBalance(String jtoken) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("serverID", ""));
        params.add(new BasicNameValuePair("userID", "945311695134375936"));
        params.add(new BasicNameValuePair("loginType", "14"));
        params.add(new BasicNameValuePair("roleID", ""));
        params.add(new BasicNameValuePair("roleName", ""));
        params.add(new BasicNameValuePair("country", "VN"));
        params.add(new BasicNameValuePair("currency", "VND"));
        params.add(new BasicNameValuePair("lang", "VIE"));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/store/getBalance", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void getRoles(String jtoken, String serverID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("userID", "945311695134375936"));
        params.add(new BasicNameValuePair("loginType", "14"));
        params.add(new BasicNameValuePair("roleID", ""));
        params.add(new BasicNameValuePair("roleName", ""));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/store/getRoles", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void getProducts(String jtoken) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("serverID", ""));
        params.add(new BasicNameValuePair("userID", "945311695134375936"));
        params.add(new BasicNameValuePair("loginType", "14"));
        params.add(new BasicNameValuePair("roleID", ""));
        params.add(new BasicNameValuePair("roleName", ""));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/store/getProducts", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void payCard(String jtoken, String userID, String roleID, String serverID, String roleName, String productID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("roleName", roleName));
        params.add(new BasicNameValuePair("productID", productID));
        params.add(new BasicNameValuePair("country", "VN"));
        params.add(new BasicNameValuePair("currency", "VND"));
        params.add(new BasicNameValuePair("lang", "VI"));
        params.add(new BasicNameValuePair("pmcID", "4"));
        params.add(new BasicNameValuePair("paymentGatewayID", "1"));
        params.add(new BasicNameValuePair("paymentPartnerID", "1"));
        params.add(new BasicNameValuePair("cardSerial", "67686330903"));
        params.add(new BasicNameValuePair("cardPassword", "8681846110831"));
//        params.add(new BasicNameValuePair("cardSerial", "57205742609"));
//        params.add(new BasicNameValuePair("cardPassword", "6536668981022"));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/pmt/payCard", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void payBank(String jtoken, String userID, String roleID, String serverID, String roleName, String productID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("roleName", roleName));
        params.add(new BasicNameValuePair("productID", productID));
        params.add(new BasicNameValuePair("country", "VN"));
        params.add(new BasicNameValuePair("currency", "VND"));
        params.add(new BasicNameValuePair("lang", "VI"));
        params.add(new BasicNameValuePair("pmcID", "4"));
        params.add(new BasicNameValuePair("paymentGatewayID", "1"));
        params.add(new BasicNameValuePair("paymentPartnerID", "1"));
        params.add(new BasicNameValuePair("isCredit", "1"));
        params.add(new BasicNameValuePair("amount", "100000"));
        params.add(new BasicNameValuePair("bankCode", "123PBIDV"));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/pmt/payBank", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void payZaloPay(String jtoken, String userID, String roleID, String serverID, String roleName, String productID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("roleName", roleName));
        params.add(new BasicNameValuePair("productID", productID));
        params.add(new BasicNameValuePair("country", "VN"));
        params.add(new BasicNameValuePair("currency", "VND"));
        params.add(new BasicNameValuePair("lang", "VI"));
        params.add(new BasicNameValuePair("pmcID", "4"));
        params.add(new BasicNameValuePair("paymentGatewayID", "1"));
        params.add(new BasicNameValuePair("paymentPartnerID", "1"));
        params.add(new BasicNameValuePair("amount", "100000"));
        params.add(new BasicNameValuePair("description", "description"));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/pmt/payZaloPay", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void paySmsOtp(String jtoken, String userID, String roleID, String serverID, String roleName, String productID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("roleName", roleName));
        params.add(new BasicNameValuePair("productID", productID));
        params.add(new BasicNameValuePair("country", "VN"));
        params.add(new BasicNameValuePair("currency", "VND"));
        params.add(new BasicNameValuePair("lang", "VI"));
        params.add(new BasicNameValuePair("pmcID", "4"));
        params.add(new BasicNameValuePair("paymentGatewayID", "1"));
        params.add(new BasicNameValuePair("paymentPartnerID", "1"));
        params.add(new BasicNameValuePair("mobileNumber", "09344829999"));
        params.add(new BasicNameValuePair("amount", "10000"));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/pmt/paySmsOtp", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void verifySmsOtp(String jtoken, String orderNumber) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("orderNumber", orderNumber));
        params.add(new BasicNameValuePair("smsOtp", "2366"));
        params.add(new BasicNameValuePair("smsOtpToken", "180312000000015"));
        params.add(new BasicNameValuePair("country", "VN"));
        params.add(new BasicNameValuePair("lang", "VI"));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/pmt/verifySmsOtp", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void payMtoWallet(String jtoken, String userID, String roleID, String serverID, String roleName, String productID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("roleName", roleName));
        params.add(new BasicNameValuePair("productID", productID));
        params.add(new BasicNameValuePair("pmcID", "0"));
        params.add(new BasicNameValuePair("paymentGatewayID", "0"));
        params.add(new BasicNameValuePair("paymentPartnerID", "0"));
        params.add(new BasicNameValuePair("country", "VN"));
        params.add(new BasicNameValuePair("currency", "VND"));
        params.add(new BasicNameValuePair("lang", "VI"));
        params.add(new BasicNameValuePair("amount", "100000"));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/pmt/payMtoWallet", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void paySeaCard(String jtoken, String userID, String roleID, String serverID, String roleName, String productID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("roleName", roleName));
        params.add(new BasicNameValuePair("productID", productID));

        // molglobal
//        params.add(new BasicNameValuePair("paymentPartnerID", "4")); 
//        params.add(new BasicNameValuePair("pmcID", "prepaidcard"));
//        params.add(new BasicNameValuePair("paymentProviderID", "zgoldmolpoints"));
//        params.add(new BasicNameValuePair("country", "TH"));
//        params.add(new BasicNameValuePair("currency", "THB"));
        // bluepay
//        params.add(new BasicNameValuePair("paymentPartnerID", "1"));
//        params.add(new BasicNameValuePair("pmcID", "cashcardapi"));
//        params.add(new BasicNameValuePair("paymentProviderID", "12call"));
//        params.add(new BasicNameValuePair("cardSerial", ""));
//        params.add(new BasicNameValuePair("cardPassword", "14247627016439718259"));
//        params.add(new BasicNameValuePair("country", "TH"));
//        params.add(new BasicNameValuePair("currency", "THB"));
        // unipin
        params.add(new BasicNameValuePair("paymentPartnerID", "5"));
        params.add(new BasicNameValuePair("pmcID", "unipinexpress"));
        params.add(new BasicNameValuePair("paymentProviderID", "unipinexpress"));
        params.add(new BasicNameValuePair("cardSerial", "IDMB6S00206778"));
        params.add(new BasicNameValuePair("cardPassword", "6721971395543426"));
        params.add(new BasicNameValuePair("country", "IN"));
        params.add(new BasicNameValuePair("currency", "IDR"));

        params.add(new BasicNameValuePair("paymentGatewayID", "2"));
        params.add(new BasicNameValuePair("description", "mua qua"));
        params.add(new BasicNameValuePair("lang", "TH"));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/seagw/payCard", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void paySeaSms(String jtoken, String userID, String roleID, String serverID, String roleName, String productID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("roleName", roleName));
        params.add(new BasicNameValuePair("productID", productID));

        // bluepay
//        params.add(new BasicNameValuePair("paymentPartnerID", "1"));
//        params.add(new BasicNameValuePair("pmcID", "smsapi"));
//        params.add(new BasicNameValuePair("currency", "THB"));
//        params.add(new BasicNameValuePair("lang", "THAI"));
//        params.add(new BasicNameValuePair("country", "TH"));
//
        //coda
//        params.add(new BasicNameValuePair("paymentPartnerID", "2"));
//        params.add(new BasicNameValuePair("pmcID", "dcb"));
//        params.add(new BasicNameValuePair("mobileNumber", "085814727310"));
//        params.add(new BasicNameValuePair("amount", "15000"));
//        params.add(new BasicNameValuePair("paymentProviderID", "indosat"));
//        params.add(new BasicNameValuePair("currency", "IDR"));
//        params.add(new BasicNameValuePair("lang", "IN"));
//        params.add(new BasicNameValuePair("country", "IN"));

        //metranet
        params.add(new BasicNameValuePair("paymentPartnerID", "6"));
        params.add(new BasicNameValuePair("pmcID", "mobilecharging"));
        params.add(new BasicNameValuePair("mobileNumber", "081286533606"));
        params.add(new BasicNameValuePair("amount", "15000"));
        params.add(new BasicNameValuePair("paymentProviderID", "telkomsel"));
        params.add(new BasicNameValuePair("currency", "IDR"));
        params.add(new BasicNameValuePair("lang", "IN"));
        params.add(new BasicNameValuePair("country", "IN"));
        
        //
        params.add(new BasicNameValuePair("paymentGatewayID", "2"));
        params.add(new BasicNameValuePair("description", "mua qua"));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/seagw/paySms", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void paySeaBank(String jtoken, String userID, String roleID, String serverID, String roleName, String productID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("roleName", roleName));
        params.add(new BasicNameValuePair("productID", productID));

        //coda
//        params.add(new BasicNameValuePair("paymentPartnerID", "2"));
//        params.add(new BasicNameValuePair("pmcID", "bank"));
//        params.add(new BasicNameValuePair("mobileNumber", "085814727310"));
//        params.add(new BasicNameValuePair("amount", "150000"));
//        params.add(new BasicNameValuePair("paymentProviderID", "bca"));
//        params.add(new BasicNameValuePair("currency", "IDR"));
//        params.add(new BasicNameValuePair("lang", "IN"));
//        params.add(new BasicNameValuePair("country", "IN"));
        // mol thai
        params.add(new BasicNameValuePair("paymentPartnerID", "3"));
        params.add(new BasicNameValuePair("pmcID", "bankweb"));
        params.add(new BasicNameValuePair("amount", "100"));
        params.add(new BasicNameValuePair("paymentProviderID", "bay"));
        params.add(new BasicNameValuePair("currency", "THB"));
        params.add(new BasicNameValuePair("lang", "TH"));
        params.add(new BasicNameValuePair("country", "TH"));

        params.add(new BasicNameValuePair("paymentGatewayID", "2"));
        params.add(new BasicNameValuePair("description", "mua qua"));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/seagw/payBank", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void paySeaOtc(String jtoken, String userID, String roleID, String serverID, String roleName, String productID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("roleName", roleName));
        params.add(new BasicNameValuePair("productID", productID));

        //coda
        params.add(new BasicNameValuePair("paymentPartnerID", "2"));
        params.add(new BasicNameValuePair("pmcID", "otc"));
        params.add(new BasicNameValuePair("mobileNumber", "085814727310"));
        params.add(new BasicNameValuePair("amount", "150000"));
        params.add(new BasicNameValuePair("paymentProviderID", "alfamart"));
        params.add(new BasicNameValuePair("currency", "IDR"));
        params.add(new BasicNameValuePair("lang", "IN"));
        params.add(new BasicNameValuePair("country", "IN"));

        //
        params.add(new BasicNameValuePair("paymentGatewayID", "2"));

        params.add(new BasicNameValuePair("description", "mua qua"));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/seagw/payOtc", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void paySeaWallet(String jtoken, String userID, String roleID, String serverID, String roleName, String productID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("roleName", roleName));
        params.add(new BasicNameValuePair("productID", productID));

        //molglobal
//        params.add(new BasicNameValuePair("paymentPartnerID", "4")); // molglobal
//        params.add(new BasicNameValuePair("pmcID", "ewallet"));
//        params.add(new BasicNameValuePair("paymentProviderID", "paypal")); // payPal
//        // params.add(new BasicNameValuePair("paymentProviderID", "zgoldmolpoints")); // zgoldmolpoints
//        params.add(new BasicNameValuePair("country", "TH"));
//        params.add(new BasicNameValuePair("currency", "THB"));
//        params.add(new BasicNameValuePair("lang", "THAI"));
        // coda
        params.add(new BasicNameValuePair("paymentPartnerID", "2"));
        params.add(new BasicNameValuePair("pmcID", "ewallet"));
        params.add(new BasicNameValuePair("paymentProviderID", "doku"));
        params.add(new BasicNameValuePair("amount", "50000"));
        params.add(new BasicNameValuePair("country", "IN"));
        params.add(new BasicNameValuePair("currency", "IDR"));
        params.add(new BasicNameValuePair("lang", "IN"));

        params.add(new BasicNameValuePair("paymentGatewayID", "2"));
        params.add(new BasicNameValuePair("description", "mua qua"));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/seagw/payWallet", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void getOrderResult(String clientKey, String orderNumber) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("clientKey", clientKey));
        params.add(new BasicNameValuePair("orderNumber", orderNumber));
        params.add(new BasicNameValuePair("country", "VN"));
        params.add(new BasicNameValuePair("lang", "VI"));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(DOMAIN + "/fe/api/order/getResult", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public static void main(String[] args) throws Exception {
        Configuration.init();

        /*
            IREAD
         */
//        String clientKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjIjoxLCJhIjoxLCJzIjoxfQ.8wMN1ezPecyNmtVJ2i60YXSwPXt-V9znUt2s8RA6j4M";
//        String params = "success=0&gameID=iread&session=iread_eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJNVE8tUFBUIiwiZXhwIjoxNTIwODAwNjE2LCJhcHBJRCI6ImlyZWFkIiwidXNlcklEIjoiOTQ1MzExNjk1MTM0Mzc1OTM2IiwibG9naW5UeXBlIjoiZmFjZWJvb2siLCJhY2Nlc3NUb2tlbiI6IkVBQWFmVVdhcU81UUJBQXJwWkNZMTA0ZXl5NURFS2tra0NBRER6T1F1Y0wza29hQ0xYY05EVlpBVWJoSWgybnhtU0FSUlNvdkRmSEFTTnhJczV1YXk4MnNZOHRWQ0pROUZiajl4WkNDMU1zUkVyQzEyMThiNVpBeDMzY2dIUnBNd285U1F3RTJiSk5HZEZuTmVEOWN6Q0ZsMzBJQkNSWkF0QnQ4U2puem1PUndaRFpEIn0.PYffM6xI-lCgBTQOBW9s2wEjunTasSOP4rq_FP_ntJ4DTtnndudcchpU2TdtL3lffmW1djQhMxE9BGc9rwsQIg&userID=945311695134375936&userName=Tran+Loc&loginType=14&ts=1520793415750&sig=cb67d0cb78165f65a37289cce0944412&appID=iread&socialID=1705494462803169&muid=0&maccesstoken=0";
//        String jtoken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJXZWJQYXltZW50IiwiZXhwIjoxNTIzMjM1MTUyLCJhcHBJRCI6ImppM2RnbHAzajJxd0pTZEk4Q0ZjYVEiLCJjbGllbnRJRCI6ImppM2RnbHAzajJxd0pTZEk4Q0ZjYVEiLCJ1c2VySUQiOiJTV2NVdlRlWHA0T2dyVWxWVEFvbTl2ZG9XcXJJaEJFczctdFlPRVF6VzdVIiwibG9naW5UeXBlIjoiTmY0dThDVmN4TFl1bEphbmphZW42USIsImxvZ2luTWV0aG9kIjoiM2xnQzItYUQ5SERtMk9DRGhqLVM5USIsInJvbGVJRCI6Ino3ME1acnhWZXNVWW1IM1g0cTZXQXciLCJyb2xlTmFtZSI6Ino3ME1acnhWZXNVWW1IM1g0cTZXQXciLCJzZXJ2ZXJJRCI6Ino3ME1acnhWZXNVWW1IM1g0cTZXQXcifQ.u4LhfwzDtq7wXuHM16m-I0YE2owjAZ5ap79OcyOwa98qkGWkn0vo71fW5jW9ZeL8wQ8EZJwdatgbesLpDRm5Fg";
//        String serverID = "";
//        String orderNumber = "1077343750037970944";
//        String userID = "945311695134375936";
//        String roleID = "95";
//        String roleName = "fb.tranloc";
//        String productID = "1";
//
//        getInitConfig(clientKey);
//        getLogin(clientKey, params);
//        getServers(jtoken);
//        getBalance(jtoken);
//        getRoles(jtoken, serverID);
//        getProducts(jtoken);
//        payCard(jtoken, userID, roleID, serverID, roleName, productID);
//        payBank(jtoken, userID, roleID, serverID, roleName, productID);
//        payZaloPay(jtoken, userID, roleID, serverID, roleName, productID);
//        paySmsOtp(jtoken, userID, roleID, serverID, roleName, productID);
//        verifySmsOtp(jtoken, orderNumber);
//        payMtoWallet(jtoken, userID, roleID, serverID, roleName, productID);
//        getOrderResult(clientKey, orderNumber);
        /*
            END IREAD
         */
        //
        /*
            360LIVE
         */
        String clientKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjIjoyLCJhIjoyLCJzIjoxfQ.4fJDmu0vc4mZabFDYAQSJpPWqg6bYO16XrcOy2ag4WE";
        String params = "success=0&gameID=360live&session=360live_eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJNVE8tUFBUIiwiZXhwIjoxNTIyMjI3NjE1LCJhcHBJRCI6IjM2MGxpdmUiLCJ1c2VySUQiOiIxNDQwNTY2Mjk1OTYyNjU1IiwibG9naW5UeXBlIjoiZmFjZWJvb2siLCJhY2Nlc3NUb2tlbiI6IkVBQUZKdE05SzRqNEJBQ2hiVGNDV2xLSVJjSlJuZkNNODVMWkNpNm41azhHWEhkNUdUbURYOWp6SlpDYTkxRVR6UzVoZmw3WkM3SVlzQTh6Z3U3ZUcxN0JGOGdmS213bERVR0V4N09XeU50bnBtZ3IyYnFBc3Zlb3BWcDZ4N1k0V1pBT0ZNVWl1ZlZaQlpDOFdlRDc3QzAxNDYxTlF0SWxKWUIwMXNTRERYWkNnQVpEWkQifQ.gpmCYydOKh26bnqBQM-9Ym_4RYSs0zL7TlRa0oKaEJOoCS2VGOVDRIyfT82GOzxatrJmwFju6_u321wBXLQICw&userID=1440566295962655&userName=Tran+Loc&loginType=14&ts=1522220414838&sig=50555ef133299e21c1a40b14b27c6d2b&appID=360live&socialID=1440566295962655&muid=0&maccesstoken=0";
        String jtoken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJXZWJQYXltZW50IiwiZXhwIjoxNTI0NjQwNzQzLCJhcHBJRCI6IjJ0MEEzYUs1MDl5dTdQOTJrYnU2MEEiLCJjbGllbnRJRCI6IjJ0MEEzYUs1MDl5dTdQOTJrYnU2MEEiLCJ1c2VySUQiOiIyc3AzblNDU01RWG1uMXMyVVVXUVJXSGo5c2c2RmJyWl9MbDRubUppSFJ3IiwibG9naW5UeXBlIjoiTmY0dThDVmN4TFl1bEphbmphZW42USIsImxvZ2luTWV0aG9kIjoiM2xnQzItYUQ5SERtMk9DRGhqLVM5USIsInJvbGVJRCI6Ino3ME1acnhWZXNVWW1IM1g0cTZXQXciLCJyb2xlTmFtZSI6Ino3ME1acnhWZXNVWW1IM1g0cTZXQXciLCJzZXJ2ZXJJRCI6Ino3ME1acnhWZXNVWW1IM1g0cTZXQXciLCJ1c2VySVAiOiJ6NzBNWnJ4VmVzVVltSDNYNHE2V0F3In0.3GketaR2u0nQC_-kZxxKiPvMjQLSHAw3trxgloVvLNdWPFwdNuJlHjqMhp--cCDAYnXvepZVgZXGnqLUtbQOgg";
        String serverID = "360live";
        String orderNumber = "1066285142542131200";
        String userID = "1440566295962655";
//        String roleID = "6480"; // live
        String roleID = "110";
        String roleName = "";
        String productID = "com.vng.live360.20w";
//
//        getInitConfig(clientKey);
//        getLogin(clientKey, params);
//        getServers(jtoken);
//        getRoles(jtoken, serverID);
//        getBalance(jtoken);
//        getProducts(jtoken);
        payCard(jtoken, userID, roleID, serverID, roleName, productID);
//        payBank(jtoken, userID, roleID, serverID, roleName, productID);
//        payZaloPay(jtoken, userID, roleID, serverID, roleName, productID);
//        paySmsOtp(jtoken, userID, roleID, serverID, roleName, productID);
//        verifySmsOtp(jtoken, orderNumber);
//        payMolGlobal(jtoken, userID, roleID, serverID, roleName, productID);
//        paySeaCard(jtoken, userID, roleID, serverID, roleName, productID);
//        paySeaWallet(jtoken, userID, roleID, serverID, roleName, productID);
//        paySeaSms(jtoken, userID, roleID, serverID, roleName, productID);
//        paySeaBank(jtoken, userID, roleID, serverID, roleName, productID);
//        paySeaOtc(jtoken, userID, roleID, serverID, roleName, productID);
//        getOrderResult(clientKey, orderNumber);
        /*
           360LIVE
         */
        //
        /*
            THIENNU
         */
//        String clientKey = "eyJhbGciOiJIUzI1NiJ9.eyJjIjozLCJhIjozLCJzIjoxfQ.-Nf3jjO3HJZi6R_DtDOU-m7ea_Mo5iEbU3hen8vvJ8w";
//        String params = "success=0&gameID=thiennu&session=thiennu_eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJNVE8tUFBUIiwiZXhwIjoxNTIwOTk4NzkxLCJhcHBJRCI6InRoaWVubnUiLCJ1c2VySUQiOiI4MjY1NjQzMTc2NTc3ODQzMjAiLCJsb2dpblR5cGUiOiJ6aW5nIiwiYWNjZXNzVG9rZW4iOiIifQ.UQYJ17jrSkrb0hgpYeeMTnJHEEvmiWaT-svPiEnqRtDZA8HktGtRxsAO9q_FDPSEtpiEtUk4mVLQaC86jqFLRw&userID=826564317657784320&userName=vien_dan_chi&loginType=11&ts=1520991591177&sig=61667c2a0b54154d77d5618487c2d1ce&appID=thiennu&muid=0&maccesstoken=0";
//        String jtoken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJXZWJQYXltZW50IiwiZXhwIjoxNTIxNTk2NDUzLCJhcHBJRCI6Im5oWm1EcGRnOFZjcmdONnZiUjhSZEEiLCJjbGllbnRJRCI6IlFVcU83T0hwNTVNYXJNTFJyTElCc3ciLCJ1c2VySUQiOiJ6TlRha3Z5eFFSTTJYdG45WFBzcHhVdkFQV01LbDYtR1NzRDZCS1RYcjUwIiwibG9naW5UeXBlIjoiQUI0R3lOa2RPX0I1RXVUM3pLZzdiUSIsImxvZ2luTWV0aG9kIjoiVXVBTWFjMFdHWi1XSU54RUltYVNjdyIsInJvbGVJRCI6ImhxTXpZbXpxS1lqeEVUQ3A5bTBiV0EiLCJzZXJ2ZXJJRCI6ImhxTXpZbXpxS1lqeEVUQ3A5bTBiV0EifQ.2gDEnPjskjjFbi183H9MRKKfGsQMDLncR7B2BWNGKG4Anop8Q2KLzQ80ph4gMtji93mHlHTVSOigrwuyYy_bpg";
//        String serverID = "4101";
//        String orderNumber = "";
//        getInitConfig(clientKey);
//        getLogin(clientKey, params);
//        getServers(jtoken);
//        getRoles(jtoken, serverID);
//        getProducts(jtoken);
//        payCard(jtoken);
//        payBank(jtoken);
//        payZaloPay(jtoken);
//        paySmsOtp(jtoken);
//        getOrderResult(clientKey, orderNumber);
        /*
           THIENNU
         */
    }
}
