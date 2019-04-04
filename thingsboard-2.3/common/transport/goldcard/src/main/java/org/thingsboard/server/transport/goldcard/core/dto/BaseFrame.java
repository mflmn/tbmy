package org.thingsboard.server.transport.goldcard.core.dto;


/**
 * @author rym
 */

public abstract class BaseFrame {

    private String original;


    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    /**
     * 封装帧
     *
     * @return
     */
    public abstract String toHexString();

    public abstract String makeLength();

    /**
     * 长度计算
     *
     * @return
     */
    public abstract String calculationLength(int length);


    /**
     * 报文解析
     *
     * @param bytes
     */
    public abstract void resolveFrame(byte[] bytes);


    /**
     * 数据域解析
     */
    public abstract void analysisData(org.thingsboard.server.transport.goldcard.core.dto.BusinessMessage businessMessage);


    /**
     * 帧编码
     */
    public abstract void encodeFrame(org.thingsboard.server.transport.goldcard.core.dto.BusinessMessage businessMessage);


    /**
     * 解密校验
     */
    public abstract void decodeCheck();

    /**
     * 编码校验
     */
    public abstract void encodeCheck();


}
