package com.rishabhmatharoo.blacklight.FirebaseCloudMessageService.Model;

public class PayloadData {

    public String Name="";
    public String Msg="";
    public String msgType="";

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

}
