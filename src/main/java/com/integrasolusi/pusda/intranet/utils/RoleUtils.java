package com.integrasolusi.pusda.intranet.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Programmer   : pancara
 * Date         : Feb 16, 2011
 * Time         : 10:58:44 PM
 */
public class RoleUtils {
    public static final int GUEST = 1;
    public static final int MEMBER = 2;
    public static final int ADMINISTRATOR = 3;

    private Map<Integer, String> roleNames = new HashMap<Integer, String>();

    private static RoleUtils instance;

    private RoleUtils() {
        getRoleNames().put(GUEST, "Tamu");
        getRoleNames().put(MEMBER, "Anggota");
        getRoleNames().put(ADMINISTRATOR, "Administrator");
    }

    public static RoleUtils getInstance() {
        if (instance == null)
            instance = new RoleUtils();
        return instance;
    }

    public String getRoleName(int role) {
        return getRoleNames().get(role);
    }

    public Map<Integer, String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(Map<Integer, String> roleNames) {
        this.roleNames = roleNames;
    }
}
