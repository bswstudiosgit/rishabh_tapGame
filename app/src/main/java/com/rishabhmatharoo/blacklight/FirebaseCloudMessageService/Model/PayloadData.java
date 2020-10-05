package com.rishabhmatharoo.blacklight.FirebaseCloudMessageService.Model;

public class PayloadData {

    public String Name="";
    public String Msg="";
    public int msgType=0;

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
    public int getMsgType() {
        return msgType;
    }
    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
}
