package org.thingsboard.server.transport.goldcard.core.dto;

import io.netty.util.AttributeKey;

import java.util.Date;

public class ChannelPocket {
    public static final AttributeKey<ChannelPocket> KEY = AttributeKey.valueOf("channelPocket");

    private int port;

    private String deviceNo;//确保每一个channel都有设备编号(DTU，集中器编号)
    private MeterProtocol protocol;
    private String funCode;//用于是否启用上下文
    private boolean isWaitingFlag;// 是否等待发送
    private String originalSendStr; //原始指令json串
    private int timeOutCount; //当前通道 超时次数
    private Date registerTime; //设备上线时间
    private String meterId;//表具台帐ID
    private String cmdId;// 待发指令id
    private String reqCmdId;// 请求指令id
    // 注册帧延时响应的抄表请求 是否可以下发。若延时时间之内表端上传了数据则不下发抄表请求. 默认false可以下发
    private boolean isSendReadingFlag;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getFunCode() {
        return funCode;
    }

    public void setFunCode(String funCode) {
        this.funCode = funCode;
    }

    public boolean isWaitingFlag() {
        return isWaitingFlag;
    }

    public void setWaitingFlag(boolean isWaitingFlag) {
        this.isWaitingFlag = isWaitingFlag;
    }

    public String getOriginalSendStr() {
        return originalSendStr;
    }

    public void setOriginalSendStr(String originalSendStr) {
        this.originalSendStr = originalSendStr;
    }

    public int getTimeOutCount() {
        return timeOutCount;
    }

    public void setTimeOutCount(int timeOutCount) {
        this.timeOutCount = timeOutCount;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    public String getReqCmdId() {
        return reqCmdId;
    }

    public void setReqCmdId(String reqCmdId) {
        this.reqCmdId = reqCmdId;
    }

    public boolean isSendReadingFlag() {
        return isSendReadingFlag;
    }

    public void setSendReadingFlag(boolean isSendReadingFlag) {
        this.isSendReadingFlag = isSendReadingFlag;
    }

    public MeterProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(MeterProtocol protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


}
