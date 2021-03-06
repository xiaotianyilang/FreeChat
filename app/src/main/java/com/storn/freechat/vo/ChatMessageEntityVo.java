package com.storn.freechat.vo;

/**
 * 聊天实体类
 * Created by tianshutong on 2017/1/9.
 */
public class ChatMessageEntityVo {

    //对方的jid
    public String fromJid;

    //登录用户的Jid, 避免切换账号查询聊天记录时混乱
    public String myJid;

    public String content;

    public long time;

    public int type;

}
