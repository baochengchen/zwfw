package com.ztesoft.zwfw.moudle;

/**
 * Created by BaoChengchen on 2017/8/6.
 */

public class Config {

    
    //http://220.163.118.118
    //http://192.168.50.109:8080/portal
    //http://192.168.50.110
    //http://192.168.0.113:8080/portal

   // public static final String BASE_URL = "http://192.168.0.111:8080/portal";

    public static final String BASE_URL = "http://192.168.50.109:8080/portal";

    public static final String URL_LOGIN = "/rest/login";

    public static final String URL_USER_CURRENT = "/rest/user/current";

    public static final String URL_MESSAGE_LIST = "/rest/message/qryList";

    public static final String URL_QRYWORKLIST = "/rest/sgsp/mywork/qryMyWorkList";

    public static final String URL_QUERYMYWORKTASKS = "/rest/sgsp/myworktasks/queryMyWorkTasks";

    public static final String URL_SEARCHFLOWBUTTONDTO = "/rest/commons/searchFlowButtonDto";

    public static final String URL_EXCUTEBIZPROCESS="/rest/commons/excuteBizProcess";

    public static final String URL_QRYINTERACTION = "/rest/web/websiteinteraction/queryInterAction";

    public static final String URL_QRYSUPERVISE = "/rest/supervision/supervisionwork/queryMySupervisionList";

    public static final String URL_SEARCHFRONTSTARTINFO = "/rest/commons/searchFrontStartInfo";

    public static final String URL_SEARCHFRONTEXCUTELIST = "/rest/commons/searchFrontExcuteList";

    public static final String URL_QUERYBIZINFOBYID = "/rest/supervision/supervisionwork/queryBizInfoById";

    public static final String URL_QUERYAPPWORKS ="/rest/app/queryappworks";

    public static final String URL_ATTACHMENT = "/rest/attachment";

    public static final String URL_USERS = "/rest/users";

    public static final String URL_LOGOUT = "/rest/logout";

    public static final String URL_SELF = "/rest/staffs/self";
    
    public static final String URL_SELF_PWD = "/rest/users/self/pwd";

    public static final String USERINFO = "userinfo";
    public static final String IS_LOGIN = "islogin";
    public static final String CURRENT_ROLE = "currentRole";


    public static enum RoleType {
        OSP("OSP", 1), OJD("OJD", 2);

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
