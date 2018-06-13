/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.backup;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.eclipse.jetty.http.HttpMethod;
import zcore.utilities.CommonUtil;

/**
 * x
 *
 * @author huuloc.tran89
 */
public class ApiTestService {

    private static final Logger logger = Logger.getLogger(ApiTestService.class);
    private static final Lock LOCK = new ReentrantLock();
    private static final Map<String, ApiTestService> _instances = new NonBlockingHashMap<String, ApiTestService>();

    private final String domain;
    private final String clientKey;
    private final String currency;
    private final String lang;

    public static ApiTestService getInstance(String domain, String clientKey, String currency, String lang) {

        String key = new StringBuilder().append(domain).append(clientKey).toString();

        if (!_instances.containsKey(key)) {
            LOCK.lock();
            try {
                if (_instances.get(key) == null) {
                    _instances.put(key, new ApiTestService(domain, clientKey, currency, lang));
                }
            } finally {
                LOCK.unlock();
            }
        }
        return _instances.get(key);

    }

    private ApiTestService(String domain, String clientKey, String currency, String lang) {
        this.domain = domain;
        this.clientKey = clientKey;
        this.currency = currency;
        this.lang = lang;
    }

    public void getDisplayInfo() {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("clientKey", clientKey));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/config/getDisplayInfo", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void getServerInfo() {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("clientKey", clientKey));
        params.add(new BasicNameValuePair("lang", lang));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/config/getServerInfo", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void getAuthLogin(String ppParams) {
        List<NameValuePair> params = new ArrayList();

        params.add(new BasicNameValuePair("clientKey", clientKey));
        params.add(new BasicNameValuePair("lang", lang));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8) + "&" + ppParams;
        String response = CommonUtil.curl(domain + "/fe/api/auth/login", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void getAuthQuick(String userID, String loginType, String serverID, String roleID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("clientKey", clientKey));
        params.add(new BasicNameValuePair("lang", lang));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("loginType", loginType));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("captcha", "1"));
        params.add(new BasicNameValuePair("token", "1"));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/auth/quick", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void getServers(String jtoken) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("lang", lang));
        params.add(new BasicNameValuePair("jtoken", jtoken));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/store/getServers", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void getBalance(String jtoken, String userID, String loginType, String roleID, String serverID) {
        getBalance(jtoken, currency, userID, loginType, roleID, serverID);
    }

    public void getBalance(String jtoken, String currency, String userID, String loginType, String roleID, String serverID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("lang", lang));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("loginType", loginType));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("currency", currency));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/store/getBalance", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void getRoles(String jtoken, String userID, String loginType, String serverID, String roleID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("lang", lang));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("loginType", loginType));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("roleID", roleID));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/store/getRoles", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void getProducts(String jtoken, String userID, String loginType, String roleID, String serverID,
            String partnerID, String pmcID, String providerID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("lang", lang));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("loginType", loginType));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("partnerID", partnerID));
        params.add(new BasicNameValuePair("pmcID", pmcID));
        params.add(new BasicNameValuePair("providerID", providerID));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/store/getProducts", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    /////////////////////////// PAYMENT GATEWAY VN
    public void payCard(String jtoken, String roleID, String serverID, String productID) {
        payCard(jtoken, "VI", roleID, serverID, productID, "1", "1", "VND", "VN", "4", "4", "67686330903", "8681846110831");
    }

    public void payCard(String jtoken, String lang, String roleID, String serverID, String productID,
            String paymentGatewayID, String paymentPartnerID, String currency, String country, String pmcID, String providerID,
            String cardSerial, String cardPassword) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("lang", lang));
        //
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("productID", productID));
        //
        params.add(new BasicNameValuePair("currency", currency));
        params.add(new BasicNameValuePair("country", country));
        //
        params.add(new BasicNameValuePair("paymentGatewayID", paymentGatewayID));
        params.add(new BasicNameValuePair("paymentPartnerID", paymentPartnerID));
        params.add(new BasicNameValuePair("pmcID", pmcID));
        params.add(new BasicNameValuePair("providerID", providerID));
        //
        params.add(new BasicNameValuePair("cardSerial", cardSerial));
        params.add(new BasicNameValuePair("cardPassword", cardPassword));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/pmt/payCard", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void payBank(String jtoken, String roleID, String serverID, String productID) {
        payBank(jtoken, "VI", roleID, serverID, productID, "1", "1", "VND", "VN", "36", "123PBIDV", "1", "100000");
    }

    public void payBank(String jtoken, String lang, String roleID, String serverID, String productID,
            String paymentGatewayID, String paymentPartnerID, String currency, String country, String pmcID, String providerID,
            String isCredit, String amount) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("lang", lang));
        //
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("productID", productID));
        //
        params.add(new BasicNameValuePair("country", country));
        params.add(new BasicNameValuePair("currency", currency));
        //
        params.add(new BasicNameValuePair("paymentGatewayID", paymentGatewayID));
        params.add(new BasicNameValuePair("paymentPartnerID", paymentPartnerID));
        params.add(new BasicNameValuePair("pmcID", pmcID));
        params.add(new BasicNameValuePair("providerID", providerID));
        //
        params.add(new BasicNameValuePair("isCredit", isCredit));
        params.add(new BasicNameValuePair("amount", amount));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/pmt/payBank", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void payZaloPay(String jtoken, String roleID, String serverID, String productID) {
        payZaloPay(jtoken, "VI", roleID, serverID, productID, "1", "1", "VND", "VN", "38", "38", "100000", "Mua vat pham: " + productID);
    }

    public void payZaloPay(String jtoken, String lang, String roleID, String serverID, String productID,
            String paymentGatewayID, String paymentPartnerID, String currency, String country, String pmcID, String providerID,
            String amount, String description) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("lang", lang));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("productID", productID));
        //
        params.add(new BasicNameValuePair("country", country));
        params.add(new BasicNameValuePair("currency", currency));
        //
        params.add(new BasicNameValuePair("paymentGatewayID", paymentGatewayID));
        params.add(new BasicNameValuePair("paymentPartnerID", paymentPartnerID));
        params.add(new BasicNameValuePair("pmcID", pmcID));
        params.add(new BasicNameValuePair("providerID", providerID));
        //
        params.add(new BasicNameValuePair("amount", amount));
        params.add(new BasicNameValuePair("description", description));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/pmt/payZaloPay", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void paySmsOtp(String jtoken, String roleID, String serverID, String productID) {
        paySmsOtp(jtoken, "VI", roleID, serverID, productID, "1", "1", "VND", "country", "42", "42", "10000", "0934482999");
    }

    public void paySmsOtp(String jtoken, String lang, String roleID, String serverID, String productID,
            String paymentGatewayID, String paymentPartnerID, String currency, String country, String pmcID, String providerID,
            String amount, String mobileNumber) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("lang", lang));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("productID", productID));
        //
        params.add(new BasicNameValuePair("country", country));
        params.add(new BasicNameValuePair("currency", currency));
        //
        params.add(new BasicNameValuePair("paymentGatewayID", paymentGatewayID));
        params.add(new BasicNameValuePair("paymentPartnerID", paymentPartnerID));
        params.add(new BasicNameValuePair("pmcID", pmcID));
        params.add(new BasicNameValuePair("providerID", providerID));
        //
        params.add(new BasicNameValuePair("amount", amount));
        params.add(new BasicNameValuePair("mobileNumber", mobileNumber));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/pmt/paySmsOtp", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

     public void paySms(String jtoken, String roleID, String serverID, String productID) {
        paySms(jtoken, "VI", roleID, serverID, productID, "1", "1", "VND", "VN", "5", "45201", "10000");
    }

    public void paySms(String jtoken, String lang, String roleID, String serverID, String productID,
            String paymentGatewayID, String paymentPartnerID, String currency, String country, String pmcID, String providerID,
            String amount) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("lang", lang));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("productID", productID));
        //
        params.add(new BasicNameValuePair("country", country));
        params.add(new BasicNameValuePair("currency", currency));
        //
        params.add(new BasicNameValuePair("paymentGatewayID", paymentGatewayID));
        params.add(new BasicNameValuePair("paymentPartnerID", paymentPartnerID));
        params.add(new BasicNameValuePair("pmcID", pmcID));
        params.add(new BasicNameValuePair("providerID", providerID));
        //
        params.add(new BasicNameValuePair("amount", amount));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/pmt/paySms", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void verifySmsOtp(String jtoken, String orderNumber, String smsOtp, String smsOtpToken) {
        verifySmsOtp(jtoken, "VI", orderNumber, smsOtp, smsOtpToken);
    }

    public void verifySmsOtp(String jtoken, String lang, String orderNumber, String smsOtp, String smsOtpToken) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("orderNumber", orderNumber));

        params.add(new BasicNameValuePair("lang", lang));

        params.add(new BasicNameValuePair("smsOtp", smsOtp));
        params.add(new BasicNameValuePair("smsOtpToken", smsOtpToken));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/pmt/verifySmsOtp", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void payMtoWallet(String jtoken, String lang, String userID, String roleID, String serverID, String roleName, String productID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("lang", lang));
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
        String response = CommonUtil.curl(domain + "/fe/api/pmt/payMtoWallet", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void paySeaCard(String jtoken, String userID, String roleID, String serverID, String roleName, String productID) {
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
//        params.add(new BasicNameValuePair("providerID", "zgoldmolpoints"));
//        params.add(new BasicNameValuePair("country", "TH"));
//        params.add(new BasicNameValuePair("currency", "THB"));
        // bluepay
        params.add(new BasicNameValuePair("paymentPartnerID", "bluepay"));
        params.add(new BasicNameValuePair("pmcID", "cashcardapi"));
        params.add(new BasicNameValuePair("providerID", "12call"));
        params.add(new BasicNameValuePair("cardSerial", ""));
        params.add(new BasicNameValuePair("cardPassword", "81533239943150046388"));
        params.add(new BasicNameValuePair("country", "TH"));
        params.add(new BasicNameValuePair("currency", "THB"));
        // unipin
//        params.add(new BasicNameValuePair("paymentPartnerID", "unipin"));
//        params.add(new BasicNameValuePair("pmcID", "unipinexpress"));
//        params.add(new BasicNameValuePair("providerID", "unipinexpress"));
//        params.add(new BasicNameValuePair("cardSerial", "IDMB6S00206778"));
//        params.add(new BasicNameValuePair("cardPassword", "6721971395543426"));
//        params.add(new BasicNameValuePair("country", "IN"));
//        params.add(new BasicNameValuePair("currency", "IDR"));

        params.add(new BasicNameValuePair("paymentGatewayID", "2"));
        params.add(new BasicNameValuePair("description", "mua qua"));
        params.add(new BasicNameValuePair("lang", "TH"));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/seagw/payCard", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void paySeaSms(String jtoken, String userID, String roleID, String serverID, String roleName, String productID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("roleName", roleName));
        params.add(new BasicNameValuePair("productID", productID));

        // bluepay
//        params.add(new BasicNameValuePair("paymentPartnerID", "bluepay"));
//        params.add(new BasicNameValuePair("pmcID", "smsapi"));
//        params.add(new BasicNameValuePair("providerID", "smsapi"));
//        params.add(new BasicNameValuePair("currency", "THB"));
//        params.add(new BasicNameValuePair("lang", "THAI"));
//        params.add(new BasicNameValuePair("country", "TH"));
//
        //coda
//        params.add(new BasicNameValuePair("paymentPartnerID", "coda"));
//        params.add(new BasicNameValuePair("pmcID", "dcb"));
//        params.add(new BasicNameValuePair("mobileNumber", "085814727310"));
//        params.add(new BasicNameValuePair("amount", "15000"));
//        params.add(new BasicNameValuePair("providerID", "indosat"));
//        params.add(new BasicNameValuePair("currency", "IDR"));
//        params.add(new BasicNameValuePair("lang", "IN"));
//        params.add(new BasicNameValuePair("country", "IN"));
        //metranet
        params.add(new BasicNameValuePair("paymentPartnerID", "metranet"));
        params.add(new BasicNameValuePair("pmcID", "mobilecharging"));
        params.add(new BasicNameValuePair("mobileNumber", "081286533606"));
        params.add(new BasicNameValuePair("amount", "30000"));
        params.add(new BasicNameValuePair("providerID", "telkomsel"));
        params.add(new BasicNameValuePair("currency", "IDR"));
        params.add(new BasicNameValuePair("lang", "IN"));
        params.add(new BasicNameValuePair("country", "IN"));

        //
        params.add(new BasicNameValuePair("paymentGatewayID", "2"));
        params.add(new BasicNameValuePair("description", "mua qua"));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/seagw/paySms", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void paySeaBank(String jtoken, String userID, String roleID, String serverID, String roleName, String productID) {
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
        // molthai
        params.add(new BasicNameValuePair("paymentPartnerID", "molth"));
        params.add(new BasicNameValuePair("pmcID", "bankweb"));
        params.add(new BasicNameValuePair("amount", "100"));
        params.add(new BasicNameValuePair("providerID", "bay"));
        params.add(new BasicNameValuePair("currency", "THB"));
        params.add(new BasicNameValuePair("lang", "TH"));
        params.add(new BasicNameValuePair("country", "TH"));

        params.add(new BasicNameValuePair("paymentGatewayID", "2"));
        params.add(new BasicNameValuePair("description", "mua qua"));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/seagw/payBank", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void paySeaOtc(String jtoken, String userID, String roleID, String serverID, String roleName, String productID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("roleName", roleName));
        params.add(new BasicNameValuePair("productID", productID));

        //coda
        params.add(new BasicNameValuePair("paymentPartnerID", "coda"));
        params.add(new BasicNameValuePair("pmcID", "otc"));
        params.add(new BasicNameValuePair("mobileNumber", "085814727310"));
        params.add(new BasicNameValuePair("amount", "150000"));
        params.add(new BasicNameValuePair("providerID", "alfamart"));
        params.add(new BasicNameValuePair("currency", "IDR"));
        params.add(new BasicNameValuePair("lang", "IN"));
        params.add(new BasicNameValuePair("country", "IN"));

        //
        params.add(new BasicNameValuePair("paymentGatewayID", "2"));

        params.add(new BasicNameValuePair("description", "mua qua"));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/seagw/payOtc", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void paySeaWallet(String jtoken, String userID, String roleID, String serverID, String roleName, String productID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("roleName", roleName));
        params.add(new BasicNameValuePair("productID", productID));

        //molglobal
//        params.add(new BasicNameValuePair("paymentPartnerID", "molglobal")); // molglobal
//        params.add(new BasicNameValuePair("pmcID", "ewallet"));
//        params.add(new BasicNameValuePair("providerID", "paypal")); // payPal
//        // params.add(new BasicNameValuePair("providerID", "zgoldmolpoints")); // zgoldmolpoints
//        params.add(new BasicNameValuePair("country", "TH"));
//        params.add(new BasicNameValuePair("currency", "THB"));
//        params.add(new BasicNameValuePair("lang", "THAI"));
        // coda
        params.add(new BasicNameValuePair("paymentPartnerID", "coda"));
        params.add(new BasicNameValuePair("pmcID", "ewallet"));
        params.add(new BasicNameValuePair("providerID", "doku"));
        params.add(new BasicNameValuePair("amount", "50000"));
        params.add(new BasicNameValuePair("country", "IN"));
        params.add(new BasicNameValuePair("currency", "IDR"));
        params.add(new BasicNameValuePair("lang", "IN"));

        params.add(new BasicNameValuePair("paymentGatewayID", "2"));
        params.add(new BasicNameValuePair("description", "mua qua"));

        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/seagw/payWallet", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void getOrderResult(String clientKey, String orderNumber) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("clientKey", clientKey));
        params.add(new BasicNameValuePair("orderNumber", orderNumber));
        params.add(new BasicNameValuePair("country", "VN"));
        params.add(new BasicNameValuePair("lang", "VI"));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/order/getResult", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    /*
    CATE 2 , CATE 2, CATE 2
     */
    private static final Comparator<NameValuePair> nameValuePairComp = (NameValuePair p1, NameValuePair p2) -> p1.getName().compareTo(p2.getName()); // solution than making method synchronized

    private String genSig(String secretKey, List<NameValuePair> params) {
        try {
            params.sort(nameValuePairComp);

            StringBuilder sb = new StringBuilder();
            sb.append(secretKey);
            params.forEach((param) -> {
                sb.append(param.getValue());
            });
            String sig = CommonUtil.md5(sb.toString());
            logger.info("genSig: md5(" + sb.toString() + ") : " + sig);
            return sig;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    public void getColServers(String secretKey, String userIP) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("clientKey", clientKey));
        params.add(new BasicNameValuePair("userIP", userIP));
        params.add(new BasicNameValuePair("ts", String.valueOf(System.currentTimeMillis())));
        params.add(new BasicNameValuePair("lang", "EN"));
        params.add(new BasicNameValuePair("sig", genSig(secretKey, params)));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/collector/getServers", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void getColVerify(String secretKey, String userIP, String userID, String loginType, String roleID, String serverID) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("clientKey", clientKey));
        params.add(new BasicNameValuePair("userIP", userIP));
        params.add(new BasicNameValuePair("ts", String.valueOf(System.currentTimeMillis())));
        params.add(new BasicNameValuePair("lang", "EN"));
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("loginType", loginType));
        params.add(new BasicNameValuePair("serverID", serverID));
        params.add(new BasicNameValuePair("roleID", roleID));
        params.add(new BasicNameValuePair("sig", genSig(secretKey, params)));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/collector/verifyUser", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void getColProducts(String secretKey, String userIP) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("clientKey", clientKey));
        params.add(new BasicNameValuePair("userIP", userIP));
        params.add(new BasicNameValuePair("ts", String.valueOf(System.currentTimeMillis())));
        params.add(new BasicNameValuePair("lang", "EN"));
        params.add(new BasicNameValuePair("sig", genSig(secretKey, params)));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/collector/getProducts", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void createColOrder(String secretKey, String userIP, String jtoken, String productID, String transID, String amount, String country, String currency) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("clientKey", clientKey));
        params.add(new BasicNameValuePair("userIP", userIP));
        params.add(new BasicNameValuePair("ts", String.valueOf(System.currentTimeMillis())));
        params.add(new BasicNameValuePair("lang", "EN"));
        params.add(new BasicNameValuePair("jtoken", jtoken));
        params.add(new BasicNameValuePair("transID", transID));
        params.add(new BasicNameValuePair("amount", amount));
        params.add(new BasicNameValuePair("productID", productID));
        params.add(new BasicNameValuePair("country", country));
        params.add(new BasicNameValuePair("currency", currency));
        params.add(new BasicNameValuePair("sig", genSig(secretKey, params)));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/collector/createOrder", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void getColOrderResult(String secretKey, String userIP, String orderNumber) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("clientKey", clientKey));
        params.add(new BasicNameValuePair("userIP", userIP));
        params.add(new BasicNameValuePair("ts", String.valueOf(System.currentTimeMillis())));
        params.add(new BasicNameValuePair("lang", "EN"));
        params.add(new BasicNameValuePair("orderNumber", orderNumber));
        params.add(new BasicNameValuePair("sig", genSig(secretKey, params)));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/fe/api/collector/getOrderResult", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

    public void callbackColOrder(String clientKey,String callbackKey, String orderNumber, String transID, String channel, String provider) {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("clientKey", clientKey));
        params.add(new BasicNameValuePair("transID", transID));
        params.add(new BasicNameValuePair("orderNumber", orderNumber));
        params.add(new BasicNameValuePair("channel", channel));
        params.add(new BasicNameValuePair("provider", provider));
        params.add(new BasicNameValuePair("country", "IN"));
        params.add(new BasicNameValuePair("grossAmount", "30"));
        params.add(new BasicNameValuePair("netAmount", "30"));
        params.add(new BasicNameValuePair("currency", "IDR"));
        params.add(new BasicNameValuePair("ts", String.valueOf(System.currentTimeMillis())));
        params.add(new BasicNameValuePair("sig", genSig(callbackKey, params)));
        String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        String response = CommonUtil.curl(domain + "/ff/cb/collector", HttpMethod.POST.name(), query);
        System.out.println(response);
    }

}
