package com.integrasolusi.velocity;

import com.integrasolusi.pusda.intranet.utils.RoleUtils;
import com.opensymphony.util.TextUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Programmer   : pancara
 * Date         : Feb 8, 2011
 * Time         : 6:11:07 PM
 */
public class RoleTool {

    public void configure(java.util.Map params) {
        // do nothing
    }

    public String getRoleName(int role) {
        return RoleUtils.getInstance().getRoleName(role);
    }

}