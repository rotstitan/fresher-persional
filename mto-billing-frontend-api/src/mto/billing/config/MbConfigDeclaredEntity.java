/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.config;

/**
 *
 * @author huuloc.tran89
 */
public class MbConfigDeclaredEntity {

    public static enum MB_SELLINGPRODUCT_PRODUCT_TYPE {
        CONVERT(1), PACKAGE(2), CONDITIONAL(3);

        private MB_SELLINGPRODUCT_PRODUCT_TYPE(int type) {
            this.type = type;
        }
        public int type;
    }

    public static enum MB_SELLINGPRODUCT_PAYINGAMOUNT_TYPE {
        STATIC, DYNAMIC;
    }

    public static enum MB_CONFIG_ENABLE_TYPE {
        NO(0),
        YES(1);

        private MB_CONFIG_ENABLE_TYPE(int type) {
            this.type = type;
        }

        public int type;

        public static MB_CONFIG_ENABLE_TYPE getByID(int type) {
            for (MB_CONFIG_ENABLE_TYPE e : MB_CONFIG_ENABLE_TYPE.values()) {
                if (e.type == type) {
                    return e;
                }
            }
            return null;
        }
    }

    public static enum MB_STORE_QUICK_LOGIN_TYPE {
        NO(0),
        CAPTCHA(1),
        SIG(2);
        public int type;

        private MB_STORE_QUICK_LOGIN_TYPE(int type) {
            this.type = type;
        }

        public static MB_STORE_QUICK_LOGIN_TYPE getByID(int type) {
            for (MB_STORE_QUICK_LOGIN_TYPE e : MB_STORE_QUICK_LOGIN_TYPE.values()) {
                if (e.type == type) {
                    return e;
                }
            }
            return null;
        }
    }

    public static enum MB_STORE_PRODUCT_TYPE {
        CONVERT_CURRENCY(0),
        PRODUCT_PACKAGE(1);

        private MB_STORE_PRODUCT_TYPE(int type) {
            this.type = type;
        }
        public int type;

    }

    public static enum MB_COREAPI_PRODUCTS_TYPE {
        NO_PRODUCTS(0),
        STATIC_PRODUCTS(1),
        DYNAMIC_PRODUCTS(2);

        private MB_COREAPI_PRODUCTS_TYPE(int type) {
            this.type = type;
        }
        public int type;

        public static MB_COREAPI_PRODUCTS_TYPE getByID(int type) {
            for (MB_COREAPI_PRODUCTS_TYPE e : MB_COREAPI_PRODUCTS_TYPE.values()) {
                if (e.type == type) {
                    return e;
                }
            }
            return null;
        }

    }

    public static enum MB_COREAPI_LOGINGW_TYPE {
        PPLOGINGW(0),
        PPLOGINGWSEA(1);

        private MB_COREAPI_LOGINGW_TYPE(int type) {
            this.type = type;
        }
        public int type;

    }

    public static enum MB_COREAPI_STORE_PROXY_TYPE {
        MB_STANDARD(0),
        WP_PROXY(1);

        private MB_COREAPI_STORE_PROXY_TYPE(int type) {
            this.type = type;
        }
        public int type;

        public static MB_COREAPI_STORE_PROXY_TYPE getByID(int type) {
            for (MB_COREAPI_STORE_PROXY_TYPE e : MB_COREAPI_STORE_PROXY_TYPE.values()) {
                if (e.type == type) {
                    return e;
                }
            }
            return null;
        }
    }

    public static enum MB_COREAPI_STORE_BALANCE_TYPE {
        NONE(0),
        MTO_WALLET(1),
        GAME_WALLET(2);

        private MB_COREAPI_STORE_BALANCE_TYPE(int type) {
            this.type = type;
        }
        public int type;

        public static MB_COREAPI_STORE_BALANCE_TYPE getByID(int type) {
            for (MB_COREAPI_STORE_BALANCE_TYPE e : MB_COREAPI_STORE_BALANCE_TYPE.values()) {
                if (e.type == type) {
                    return e;
                }
            }
            return null;
        }
    }

    public static enum MB_COREAPI_STORE_APPCONFIG_TRANS_TYPE {
        EMPTY(0),
        GET_APPTRANS(1),
        IS_ORDERID(2);

        private MB_COREAPI_STORE_APPCONFIG_TRANS_TYPE(int type) {
            this.type = type;
        }
        public int type;

        public static MB_COREAPI_STORE_APPCONFIG_TRANS_TYPE getByID(int type) {
            for (MB_COREAPI_STORE_APPCONFIG_TRANS_TYPE e : MB_COREAPI_STORE_APPCONFIG_TRANS_TYPE.values()) {
                if (e.type == type) {
                    return e;
                }
            }
            return null;
        }
    }

    public static enum MB_COREAPI_WALLET_PROVIDER_TYPE {
        NONE, MTOWALLET;
    }

    public static enum MB_COREAPI_DEVILERY_TYPE {
        MTOWALLET, PMTGW;
    }

    public static enum MB_CONFIG_SEA_COUNTRY {
        VN, US, IN, TH,;
    }

    public static enum MB_CONFIG_SEA_CURRENCY {
        VND, USD, IRD, BATH,;
    }

    public static enum MB_CONFIG_SEA_PAYMENT_CHANNEL_GROUP {
        CASHCARD(1), DCB(2), WALLET(3), BANK(4),;
        public int id;

        private MB_CONFIG_SEA_PAYMENT_CHANNEL_GROUP(int id) {
            this.id = id;
        }

        public static MB_CONFIG_SEA_PAYMENT_CHANNEL_GROUP getByID(int id) {
            for (MB_CONFIG_SEA_PAYMENT_CHANNEL_GROUP e : MB_CONFIG_SEA_PAYMENT_CHANNEL_GROUP.values()) {
                if (e.id == id) {
                    return e;
                }
            }
            return null;
        }

    }

    public static enum MB_CONFIG_SEA_PAYMENT_PARTNER {
        BLUEPAY("bluepay"), CODA("coda"), MOLTH("molth"), MOLGLOBAL("molglobal"), UNIPIN("unipin"), METRANET("metranet");
        public String id;

        private MB_CONFIG_SEA_PAYMENT_PARTNER(String id) {
            this.id = id;
        }

        public static MB_CONFIG_SEA_PAYMENT_PARTNER getByID(String id) {
            for (MB_CONFIG_SEA_PAYMENT_PARTNER e : MB_CONFIG_SEA_PAYMENT_PARTNER.values()) {
                if (e.id.equals(id)) {
                    return e;
                }
            }
            return null;
        }
    }
}