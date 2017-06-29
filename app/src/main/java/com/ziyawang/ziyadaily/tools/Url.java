package com.ziyawang.ziyadaily.tools;

import java.io.File;

/**
 * Created by 牛海丰 on 2016/07/19 0029.
 */
public class Url {

    //测试
    //private static final String IP = "http://paper.zerdream.com/api/" ;
    //正式
    private static final String IP = "http://dailyapi.ziyawang.com/api/" ;

    public static final String HomeData = IP + "index?token=%s" ;
    public static final String login = IP + "login" ;
    public static final String getSmsCode = IP + "getSmsCode" ;
    public static final String register = IP + "register" ;
    public static final String resetPassword = IP + "resetPassword" ;
    public static final String auth = IP + "auth?token=%s" ;
    public static final String upload = IP + "upload?token=%s" ;
    public static final String ChangeNickName = IP + "changeName?token=%s" ;
    public static final String ChangePwd = IP + "changePwd?token=%s" ;
    public static final String mySystem = IP + "mySystem?token=%s" ;
    public static final String myPublish = IP + "myPublish?token=%s" ;
    public static final String publishList = IP + "publishList?token=%s" ;
    public static final String myMessage = IP + "myMessage?token=%s" ;
    public static final String note = IP + "note?token=%s" ;
    public static final String getLabel = IP + "getLabel?token=%s" ;
    public static final String publish = IP + "publish?token=%s" ;
    public static final String collect = IP + "collect?token=%s" ;
    public static final String collectList = IP + "collectList?token=%s" ;

    public static final String details = IP + "getDetail/%s?token=%s" ;
    public static final String getMessage = IP + "getMessage?token=%s" ;
    public static final String sendMessage = IP + "message?token=%s" ;
    public static final String returnType = IP + "returnType?token=%s" ;
    public static final String ShareInfo = IP + "detail/" ;
    public static final String CheckVersion = IP + "update" ;


    //头像路径
    public static final String IconPath = SDUtil.getSDPath() + File.separator + "ziya"+ File.separator + "icon.png" ;

    //图片
    public static final String FileIP =  "http://images.ziyawang.com";


    public static final String Rule = "http://files.ziyawang.com/law.html" ;


}
