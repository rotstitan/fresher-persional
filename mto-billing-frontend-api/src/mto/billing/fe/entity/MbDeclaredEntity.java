/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.fe.entity;

/**
 *
 * @author huuloc.tran89
 */
public class MbDeclaredEntity {

    public static enum MB_LANGUAGE {
        VI, EN, IN, TH;

        public static MB_LANGUAGE getByID(String lang) {
            for (MB_LANGUAGE value : MB_LANGUAGE.values()) {
                if (value.name().equals(lang)) {
                    return value;
                }
            }
            return VI;
        }
    }
}
