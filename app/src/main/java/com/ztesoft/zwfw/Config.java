package com.ztesoft.zwfw;

/**
 * Created by BaoChengchen on 2017/8/6.
 */

public class Config {

    public static final String BASE_URL= "http://192.168.50.109:8080/";
    public static final String URL_LOGIN="portal/login";
    //http://192.168.50.109:8080/portal/rest/supervision/supervisionwork/queryMySupervisionList?page=0&size=20
    //http://192.168.50.109:8080/portal/rest/sgsp/mywork/qryMyWorkList?page=0&size=20
    public static final String URL_QRYWORKLIST="portal/rest/sgsp/mywork/qryMyWorkList";

    public static final String URL_QRYINTERACTION="portal/rest/web/websiteinteraction/queryInterAction";

    public static final String URL_QRYSUPERVISE="portal/rest/supervision/supervisionwork/queryMySupervisionList";

    public static final String IS_LOGIN = "islogin";
}
