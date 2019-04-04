package org.thingsboard.server.transport.goldcard.core.dto;

import org.thingsboard.server.transport.goldcard.core.dto.BaseMessage;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author rym
 */
public class BusinessMessage extends BaseMessage {

    private String protocolName;
    //private String standardCode;
    private Map<String, String> standardCodeMap;//张东旭去掉标准功能码，增加标准功能码map
    private String funCode;

    public String getFunCode() {
        return funCode;
    }

    public void setFunCode(String funCode) {
        this.funCode = funCode;
    }

    public BusinessMessage(String protocolName) {
        this.protocolName = protocolName;
    }

//	public String getStandardCode() {
//		return standardCode;
//	}

//	public void setStandardCode(String standardCode) {
//		this.standardCode = standardCode;
//	}

    public String getStandardCode(String key) {
        if (standardCodeMap == null) {
            return "";
        }
        String raw = standardCodeMap.get(key);
        raw = raw == null ? "" : raw;
        return raw;
    }

    public String setStandardCode(String key, String value) {
        if (standardCodeMap == null) {
            standardCodeMap = new LinkedHashMap<>();
        }
        return standardCodeMap.put(key, value);
    }

    public void putAllStandardCode(Map<String, String> map) {
        if (map != null) {
            if (standardCodeMap == null) {
                standardCodeMap = new LinkedHashMap<>();
            }
            standardCodeMap.putAll(map);
        }
    }

    public Map<String, String> getStandardCodeMap() {
        return standardCodeMap;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("\n");
        str.append("----------------------------------\n");
        if (getBaseFrame() != null)
            str.append("baseFrame:" + getBaseFrame().toHexString() + "\n");
        str.append("data:" + get("data") + "\n");
        //str.append(String.format("protocolName=%s, standardCode=%s, funCode=%s\n", protocolName, standardCode, funCode));
        str.append(super.toString() + "\n");
        str.append("----------------------------------");

        return str.toString();
    }

}
