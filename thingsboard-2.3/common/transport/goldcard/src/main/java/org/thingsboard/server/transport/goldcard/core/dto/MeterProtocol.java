package org.thingsboard.server.transport.goldcard.core.dto;

import java.io.Serializable;

/**
 * protocol
 * @author 
 */
public class MeterProtocol implements Serializable {
    /**
     * 协议id
     */
    private Integer protocolId;

    /**
     * 协议名称
     */
    private String protocolName;

    /**
     * 协议模板
     */
    private String protocolTemplate;

    /**
     * 备注
     */
    private String remarks;

    private String protocolType;

    private static final long serialVersionUID = 1L;

    public Integer getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Integer protocolId) {
        this.protocolId = protocolId;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getProtocolTemplate() {
        return protocolTemplate;
    }

    public void setProtocolTemplate(String protocolTemplate) {
        this.protocolTemplate = protocolTemplate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }
}