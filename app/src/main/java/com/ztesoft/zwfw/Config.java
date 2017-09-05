package com.ztesoft.zwfw;

/**
 * Created by BaoChengchen on 2017/8/6.
 */

public class Config {

    public static final String BASE_URL = "http://192.168.50.109:8080/";
    public static final String URL_LOGIN = "portal/login";

    public static final String URL_USER_CURRENT = "portal/user/current";

    public static final String URL_QRYWORKLIST = "portal/rest/sgsp/mywork/qryMyWorkList";

    public static final String URL_QRYINTERACTION = "portal/rest/web/websiteinteraction/queryInterAction";

    public static final String URL_QRYSUPERVISE = "portal/rest/supervision/supervisionwork/queryMySupervisionList";

    public static final String URL_SEARCHFRONTSTARTINFO ="portal/rest/commons/searchFrontStartInfo";

    public static final String URL_SEARCHFRONTEXCUTELIST ="portal/rest/commons/searchFrontExcuteList";

    public static final String URL_ATTACHMENT = "portal/rest/attachment";

    public static final String URL_LOGOUT = "portal/logout";


    public static final String IS_LOGIN = "islogin";
    public static final String CURRENT_ROLE = "currentRole";



    public static enum  RoleType{
        OSP("OSP",1), OJD("OJD",2);

        private String name;
        private int index;
        RoleType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }
    }
}
