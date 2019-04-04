package org.thingsboard.server.transport.goldcard.core.protocolextend.goldcard1;


import org.thingsboard.server.transport.goldcard.core.dto.BaseFrame;
import org.thingsboard.server.transport.goldcard.core.dto.BusinessMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thingsboard.server.transport.goldcard.util.ByteUtil;
import org.thingsboard.server.transport.goldcard.util.CheckUtil;
import org.thingsboard.server.transport.goldcard.util.CryptUtil;
import org.thingsboard.server.transport.goldcard.util.StringUtil;

/**
 * @author rym
 */

public class FrameGoldCard1 extends BaseFrame {

    private static Logger logger = LoggerFactory.getLogger(FrameGoldCard1.class);

    private String head;

    private String dataLength;

    private String funCode;

    private String destAddr;

    private String deviceNo;

    private String totalFrame;

    private String currentFrame;

    private String data;

    private String check;

    private String tail;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDataLength() {
        return dataLength;
    }

    public void setDataLength(String dataLength) {
        this.dataLength = dataLength;
    }

    public String getFunCode() {
        return funCode;
    }

    public void setFunCode(String funCode) {
        this.funCode = funCode;
    }

    public String getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(String destAddr) {
        this.destAddr = destAddr;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getTotalFrame() {
        return totalFrame;
    }

    public void setTotalFrame(String totalFrame) {
        this.totalFrame = totalFrame;
    }

    public String getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(String currentFrame) {
        this.currentFrame = currentFrame;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getTail() {
        return tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }

    /**
     * 封装帧
     *
     * @return
     */
    @Override
    public String toHexString() {
        StringBuilder sb = new StringBuilder("");
        sb.append(head);
        sb.append(makeLength());
        sb.append(funCode);
        sb.append(destAddr);
        sb.append(deviceNo);
        sb.append(totalFrame);
        sb.append(currentFrame);
        sb.append(data);
        sb.append(check);
        sb.append(tail);
        return sb.toString();
    }

    @Override
    public String makeLength() {
        return calculationLength(data.length() / 2 + 17);
    }

    /**
     * 长度计算
     *
     * @return
     */
    @Override
    public String calculationLength(int length) {
        return ByteUtil.byteToHex((byte) (length));
    }


    /**
     * 报文解析
     *
     * @param bytes
     */
    @Override
    public void resolveFrame(byte[] bytes) {
        setOriginal(ByteUtil.byteToHex(bytes));
        setHead(getOriginal().substring(0, 2 * 2));
        setDataLength(getOriginal().substring(2 * 2, 3 * 2));
        setFunCode(getOriginal().substring(3 * 2, 5 * 2));
        setDestAddr(getOriginal().substring(5 * 2, 11 * 2));
        setDeviceNo(getOriginal().substring(11 * 2, 17 * 2));
        setTotalFrame(getOriginal().substring(17 * 2, 18 * 2));
        setCurrentFrame(getOriginal().substring(18 * 2, 19 * 2));
        setData(getOriginal().substring(19 * 2, 19 * 2 + (ByteUtil.hexToOneByte(getDataLength()) & 0xFF - 17) * 2));
        setCheck(getOriginal().substring(19 * 2 + getData().length(), 20 * 2 + getData().length()));
        setTail(getOriginal().substring(20 * 2 + getData().length()));
        decodeCheck();
    }


    @Override
    public void analysisData(BusinessMessage businessMessage) {
        businessMessage.set("deviceNo", getDeviceNo());
        decodeDateByFuncode(getFunCode(), getData(), businessMessage);
    }

    @Override
    public void encodeFrame(BusinessMessage businessMessage) {
        setFunCode(businessMessage.getFunCode());
        encodeDateByFuncode(getFunCode(), businessMessage);
        encodeCheck();
    }

    @Override
    public void decodeCheck() {
        Boolean flag1 = CheckUtil.check(ByteUtil.hexToByte(getOriginal().substring(2 * 2, getOriginal().length() - 2)), "cs_1");
        byte[] pub = CryptUtil.decrypt(ByteUtil.hexToByte(getData().substring(0 * 2, 8 * 2)), "des", "C83E7386FA4DB629");
        setData(ByteUtil.byteToHex(pub) + getData().substring(8 * 2));
        Boolean flag2 = CheckUtil.check(ByteUtil.hexToByte(getData()), "crc16-ccitt_2_1021");
        System.out.println(flag1);
        System.out.println(getData());
        System.out.println(flag2);
    }

    @Override
    public void encodeCheck() {
        setData(getData() + CheckUtil.getCheck(ByteUtil.hexToByte(getData()), "crc16-ccitt_2_1021"));
        byte[] pub = CryptUtil.encrypt(ByteUtil.hexToByte(getData().substring(0 * 2, 8 * 2)), "des", "C83E7386FA4DB629");
        setData(ByteUtil.byteToHex(pub) + getData().substring(8 * 2));
        String hstr = toHexString();
        setCheck(CheckUtil.getCheck(ByteUtil.hexToByte(hstr.substring(2 * 2, hstr.length() - 2 * 2)), "cs_1"));
    }

    private void decodeDate1F00(String data, BusinessMessage businessMessage) {
        //msg.set(this.getName() + count, value);
        //msg.setStandardCode(this.getName() + count, this.getStandardCode());标准功能码
        businessMessage.set("deviceTime", data.substring(0, 6 * 2));
        businessMessage.setStandardCode("deviceTime", "SP0");
        businessMessage.set("year", Integer.toString(ByteUtil.hexToOneByte(data.substring(6 * 2, 7 * 2)) & 0xFF));
        businessMessage.setStandardCode("year", "SP110");
        businessMessage.set("month", Integer.toString(ByteUtil.hexToOneByte(data.substring(7 * 2, 8 * 2)) & 0xFF));
        businessMessage.setStandardCode("month", "SP110");
        businessMessage.set("readingLastMonthLast", String.valueOf(Integer.parseInt(data.substring(8 * 2, 12 * 2)) / 10.0));
        businessMessage.setStandardCode("readingLastMonthLast", "SP110");
        String reading = "";
        for (int i = 0; i < 31; i++) {
            String temp = data.substring(12 * 2 + i * 2 * 4, 16 * 2 + i * 2 * 4);
            String reading_str = "";
            for (int j = 0; j < 4; j++) {
                reading_str += ByteUtil.byteToHex((byte) (ByteUtil.hexToOneByte(temp.substring(j * 2, (j + 1) * 2)) - 0x33));
            }
            reading += String.valueOf((Integer.parseInt(reading_str) / 10.0)) + ",";
        }
        businessMessage.set("reading", reading);
        businessMessage.setStandardCode("reading", "SP110");
        //跳过14 无用字节
        businessMessage.set("meterStatus", data.substring(150 * 2, 155 * 2));
        businessMessage.setStandardCode("meterStatus", "SP0");
        Integer signal = ByteUtil.hexToOneByte(data.substring(155 * 2, 156 * 2)) & 0xFF;
        String signal_str = "异常";
        if (signal >= 0 && signal <= 31) {
            signal_str = (signal + 1) / 32 + "";
        }
        if (signal == 99) {
            signal_str = "未知";
        }
        businessMessage.set("signalStrength", signal_str);
        businessMessage.setStandardCode("signalStrength", "SP146");
        businessMessage.set("currentCellVoltage", String.valueOf(Integer.parseInt(data.substring(156 * 2, 158 * 2)) / 100.0));
        businessMessage.setStandardCode("currentCellVoltage", "SP148");

        businessMessage.set("day", Integer.toString(ByteUtil.hexToOneByte(data.substring(158 * 2, 159 * 2)) & 0xFF));
        businessMessage.setStandardCode("day", "SP110");
        //跳过7 无用字节
        //businessMessage.set("check", data.substring(166 * 2, 168 * 2));
    }


    private void decodeDate1501_02_1B(String data, BusinessMessage businessMessage) {
        businessMessage.set("meterStatus", data.substring(0 * 2, 5 * 2));
        businessMessage.setStandardCode("meterStatus", "SP142");
    }


    private void decodeDateByFuncode(String funCode, String data, BusinessMessage businessMessage) {
        switch (funCode.toUpperCase()) {
            case "1F00": {
                decodeDate1F00(data, businessMessage);
                break;
            }
            case "1501": {
                decodeDate1501_02_1B(data, businessMessage);
                break;
            }
            case "1502": {
                decodeDate1501_02_1B(data, businessMessage);
                break;
            }
            case "151B": {
                decodeDate1501_02_1B(data, businessMessage);
                break;
            }
            default:
                break;
        }
    }

    private void encodeDate11FF(BusinessMessage businessMessage) {
        StringBuffer sb = new StringBuffer();
        sb.append(businessMessage.get("deviceTime"));
        sb.append(StringUtil.leftPad("", 5 * 2, "0"));
        String price = businessMessage.get("price");
        if (price.contains(".")) {
            String currentPriceInt = price.substring(0, price.indexOf("."));
            currentPriceInt = StringUtil.leftPad(currentPriceInt, 4, "0");//前补0 按照指定的长度
            String currentPriceFloat = price.substring(price.indexOf(".") + 1);
            currentPriceFloat = StringUtil.rightPad(currentPriceFloat, 4, "0");//后补0 按照指定的长度
            price = currentPriceInt + currentPriceFloat;
        } else {
            price = StringUtil.leftPad(price, 4, "0") + StringUtil.rightPad("", 4, "0");
        }
        sb.append(price);
        String checkTime = businessMessage.get("checkTime");
        if (checkTime == null) {
            sb.append("01");
        } else {
            sb.append(ByteUtil.byteToHex(Byte.parseByte(checkTime)));
        }
        sb.append(businessMessage.get("serverInfo"));
        sb.append(StringUtil.balance(businessMessage.get("balance")));
        sb.append(StringUtil.leftPad("", 12 * 2, "0"));
        setData(sb.toString());
    }

    private void encodeDate1A01_02_1B(BusinessMessage businessMessage) {
        StringBuffer sb = new StringBuffer();
        sb.append(StringUtil.leftPad("", 6 * 2, "0"));//无用字段6
        setData(sb.toString());
    }


    private void encodeDateByFuncode(String funCode, BusinessMessage businessMessage) {
        switch (funCode.toUpperCase()) {
            case "11FF": {
                encodeDate11FF(businessMessage);
                break;
            }
            case "1A01": {
                encodeDate1A01_02_1B(businessMessage);
                break;
            }
            case "1A02": {
                encodeDate1A01_02_1B(businessMessage);
                break;
            }
            case "1A1B": {
                encodeDate1A01_02_1B(businessMessage);
                break;
            }
            default:
                break;
        }
    }
}
