/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.config;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import mto.billing.config.MbConfigDeclaredEntity.MB_COREAPI_DEVILERY_TYPE;
import mto.billing.config.MbConfigDeclaredEntity.MB_COREAPI_WALLET_PROVIDER_TYPE;
import mto.billing.config.MbConfigDeclaredEntity.MB_SELLINGPRODUCT_PAYINGAMOUNT_TYPE;
import mto.billing.config.MbConfigDeclaredEntity.MB_SELLINGPRODUCT_PRODUCT_TYPE;

/**
 *
 * @author huuloc.tran89
 */
public class MbConfigEntity {

    public static class MbClientKey {

        public MbClientKey(int clientID, int appID, int shopfrontID) {
            this.c = clientID;
            this.a = appID;
            this.s = shopfrontID;
        }

        public String toJString() {
            return new Gson().toJson(this);
        }

        public int c;
        public int a;
        public int s;
    }

    public static class MbConfig {

        public MbConfig(MbClientConfig clientConfig, MbAppConfig appConfig) {
            this.clientConfig = clientConfig;
            this.appConfig = appConfig;
        }

        public MbClientConfig clientConfig;
        public MbAppConfig appConfig;

        public String toJString() {
            return new Gson().toJson(this);
        }
    }

    public static class MbShopfrontConfig { // shopfront

        public int shopfrontID = -1;
        public String name = null;
        public String description = null;
        public String domain = null;
        public int enable = 0;
    }

    public static class MaintainanceConfig { // Bảo trì 

        public long from = -1;
        public long to = -1;
        public String message = ""; // hiển thị thông báo 
        public int enable = 0;
    }

    public static class MbClientConfig { // mỗi shopfront + appID có 1 bộ key 

        // general info
        public int clientID = -1;
        public String clientKey = null;
        public int appID = -1;
        public int cate = 0;
        public int paymentGatewayID = 0;
        public int shopfrontID = -1;
        public String description = "";
        public String redirectUrl = null;

        // sign
        public String secretKey = null; // key 
        public String privateKey = null; // key 
        public String publicKey = null; // key 
        public String signMethod = "md5"; // md5 or digital signature

        // display info for shopfront
        public DisplayInfo displayInfo = new DisplayInfo();
        // store 
        public MbStoreInfo storeInfo = new MbStoreInfo();
        // products
        public HashMap<String, SellingProduct> sellingProducts = new HashMap();

        public HashMap<String, VngPaymentGwInfo> paymentGwInfo = new HashMap<>();

        // tracking info
        public int enable = 0;
        public String createdBy = "";
        public long createdDate = -1;
        public String modifiedBy = "";
        public long modifiedDate = -1;

        // maintanane
        public MaintainanceConfig maintainance = new MaintainanceConfig();
    }

    public static class MbAppConfig { // App = game

        // general info
        public int appID = -1;
        public String appAlias = "";
        public String appName = "";
        public String appType = "";
        public String description = "";

        // api info for integration
        public CoreApiInfo coreApiInfo = new CoreApiInfo();

        public String[] sellingProducts = new String[0];

        // tracking info
        public int enable = 0;
        public String createdBy = "";
        public long createdDate = -1;
        public String modifiedBy = "";
        public long modifiedDate = -1;

        // maintanane
        public MaintainanceConfig maintainance = new MaintainanceConfig();
    }

    public static class MbStoreInfo {

        //
        public ArrayList<String> currencies = new ArrayList<>();
        public ArrayList<String> languages = new ArrayList<>();
        // 
        public int loginType = 0; // Ex: 0 là ko sử dụng login, 1 là sử dụng
        public HashMap<String, String> loginChannels = new HashMap<>();
        public int quickType = 0; // Ex: 0 là ko sử dụng quick, 1 là sử dụng quick
        public int directType; // Ex: 0 là ko sử dụng direct, 1 là sử dụng direct

        // 
        public int serverType = 0; // Ex: 0 là ko có server , 1 là có server 
        public int roleType = 0; // Ex: 0 là ko có role , 1 là có role 
        public int balanceType = 0; // Ex: 0 là ko hiển thị balance, 1 là có hiển thị balance
        public int payWalletType = 0; // Ex: 0 là ko có pay wallet, 1 là có pay wallet
        public int productType = 0; // Ex: 0 là ko có product (convertAll), 1 là sử dụng products
    }

    public static class VngPaymentGwInfo {

        public int paymentGatewayID = 0;
        public String paymentGatewayName = "";
        public int enable = 0; // 1: enable , 0: disable
        VngPaymentMethodGroup[] methodGroups;
        VngPaymentMethod[] methods;
    }

    public static class VngPaymentMethodGroup {

        public VngPaymentMethodGroup(String groupName, String groupID, int enable) {
            this.groupName = groupName;
            this.groupID = groupID;
            this.enable = enable;
        }

        public String groupName;
        public String groupID;
        public int enable;

    }

    public static class VngPaymentCountry {

        String country;
        VngPaymentMethodGroup[] methodGroups;
        VngPaymentMethod[] methods;
        public int enable; //1: enable , 0: disable
    }

    public static class VngPaymentMethod {

        public String paymentPartnerID;
        public String groupID;
        public HashMap<String, VngPaymentProvider> providers;
        public String pmcID;
        public String pmcName;
        public int enable;
    }

    public static class VngPaymentProvider {

        public String providerID;
        public String providerName;
        public int enable;
        public HashMap<String, String> required;
        public HashMap<String, VngPaymentCurrency> currencies;
    }

    public static class VngPaymentCurrency {

        public String country;
        public String currency;
        public int enable;
        public double min;
        public double max;
        public double multiply;
        public double discount;
        public double amount[];  // trường hợp sms 5000, 10000, 15000
    }

    public static class DisplayInfo {

        public int appID = -1;
        public String appAlias = "";
        public String appName = "";
        public String description = "";
        public String icon = "";
        public String header = "";
        public String banner = "";
        public String url = "";
        public String country = "";
        public String language = "";
        public String currency = "";

    }

    public static class SellingProduct {

        public int enable = 0;
        public int hidden = -1;
        public String sellingProductID = "";
        public String productName;
        public String productGroup;
        public String description;
        public String image;

        public MB_SELLINGPRODUCT_PRODUCT_TYPE productType; // "CONVERT" là gói CONVERT , "PACKAGE" là gói thường, "CONDITIONAL" 1 là gói có điều kiện v.v.v.
        public MB_SELLINGPRODUCT_PAYINGAMOUNT_TYPE payingAmountType;

        public HashMap<String, SellingProductCurrencyPrice> prices;
        public HashMap<String, HashMap<String, HashMap<String, String[]>>> pmcProviderAllows;

        public String inGameCurrency;
        public int orderDisplay = -1;
        public String title = "";
        public String url = "";
        public String[] htmlTags = new String[1];
        public SellingProductDynamicInfo dynamicInfo = new SellingProductDynamicInfo();
        public long fromDate = -1;
        public long toDate = -1;
        public String createdBy = "";
        public long createdDate = -1;
        public String modifiedBy = "";
        public long modifiedDate = -1;

        public void setDynamicInfo(SellingProductDynamicInfo dynamicInfo) {
            this.dynamicInfo = dynamicInfo;
        }
    }

    public static class SellingProductCurrencyPrice {

        public String currency;
        public double price;
        public float convertionRate;
    }

    public static class SellingProductDynamicInfo {

        public SellingProductDynamicInfo() {
        }

        public SellingProductDynamicInfo(String value, String bonus, String description, boolean enable, Object info) {
            this.value = value;
            this.bonus = bonus;
            this.description = description;
            this.info = info;
            this.enable = enable;
        }

        public String value = "";
        public String bonus = "";
        public String description = "";
        public boolean enable = false;
        public Object info = new Object();
    }

    public static class CoreApiInfo {

        // Login
        public LoginGwApi loginGwApi = new LoginGwApi();

        // Payment
        public VngPaymentGwApi vngPaymentGwApi = new VngPaymentGwApi();
        public VngPaymentSeaGwApi vngPaymentSeaGwApi = new VngPaymentSeaGwApi();

        // Fulfillment
        public DeliveryApi deliveryApi = new DeliveryApi();

        // Store
        public StoreApi storeApi = new StoreApi();

        public static class LoginGwApi {

            public int loginGwType = -1; // 0 VN, 1 SEA
            public String loginAppID = ""; // id mà phía login đặt cho app nếu có
            public String loginMapID = ""; // id mà phía login đặt cho app nếu có
            public String secretKey = ""; // để gọi api nếu cần 
            public String callbackKey = ""; // để check sum dữ liệu callback về 
            public int enable = 0;
        }

        public static class VngPaymentGwApi {

            public int paymentGatewayID = 0;
            public String pmtAppID = "";
            public String pmtSecretKey = "";
            public String pmtCallbackKey = "";
            public String pmtSignMethod = "";
        }

        public static class VngPaymentSeaGwApi {

            public int paymentGatewayID = 1; // pmt sea
            public String pmtAppID = "";
            public String pmtSecretKey = "";
            public String pmtCallbackKey = "";
            public String pmtSignMethod = "";
        }

        public static class PayWalletApi {

            public int payWalletType = -1; // 
            public String payWalletAppID = "";
            public String url = "";
            public String secretKey;
        }

        public static class DeliveryApi {

            public MB_COREAPI_DEVILERY_TYPE deliveryType; // "MTOWALLET" & PMTGW
            public String deliveryAppID = "";
            public String url = "";
            public String secretKey = "";
            public String signMethod = "md5";
        }

        public static class StoreApi {

            public int wpStoreType = 0; // 0: ko dùng wpstoreproxy, 1: sử dụng store proxy 
            public String wpStoreProxyAppID = "";

            public MB_COREAPI_WALLET_PROVIDER_TYPE walletType; // NONE ko su dung, MTOWALLET: ko dùng wallet 

            public AppConfig appConfig = new AppConfig();
            public MtoWalletConfig mtoWalletApiConfig = new MtoWalletConfig();

            public int rolesType = -1;
            public int serversType = -1;
            public int balancesType = -1; // 0: khong su dung api getBalance, 1: sử dụng api get balance MTO wallet, 2: sử dụng 
            public int productsType = -1; // 0: ko sử dụng api getItems, 1: sử dụng api getItems để filter
            public int transType = -1; // 0: empty, 1: sử dụng appTrans, 2: sử dụng orderID 

            public static class AppConfig {

                public String appID = "";

                public CustomeApiConfig getServersApi = new CustomeApiConfig();

                public CustomeApiConfig getRolesApi = new CustomeApiConfig();

                public CustomeApiConfig getBalancesApi = new CustomeApiConfig();

                public CustomeApiConfig getProductsApi = new CustomeApiConfig();

                public CustomeApiConfig getTransApi = new CustomeApiConfig();
            }

            public static class MtoWalletConfig {

                public String walletAppID = ""; // id mà phía wallet tự đặt cho mình nếu có
                public String walletAddInfo = "";
                public String secretKey = "";
                public String getBalanceUrl = "";
                public String getTransUrl = "";
                public String getResultUrl = "";
            }

            public static class CustomeApiConfig {

                public String url = "";
                public String secretKey = "";
                public HashMap<String, String> extParams = new HashMap();
            }

        }
    }
}
