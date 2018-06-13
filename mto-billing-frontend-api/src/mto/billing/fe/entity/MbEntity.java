/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.billing.fe.entity;

import java.util.HashMap;
import mto.billing.fe.entity.MbDeclaredEntity.MB_LANGUAGE;

/**
 *
 * @author anonymous
 */
public class MbEntity {

    public static class MbErrorMessage {

        public int error;
        public HashMap<MB_LANGUAGE, String> message;

    }

    public static class MbOrderStatusMessage {

        public int error;
        public HashMap<MB_LANGUAGE, String> message;

    }
}
