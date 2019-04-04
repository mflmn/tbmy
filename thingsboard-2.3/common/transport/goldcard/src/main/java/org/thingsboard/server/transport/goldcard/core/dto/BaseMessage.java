package org.thingsboard.server.transport.goldcard.core.dto;

import org.thingsboard.server.transport.goldcard.core.dto.BaseFrame;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author rym
 */
public abstract class BaseMessage {
    private BaseFrame baseFrame;

    private Map<String, String> ext;

    public BaseFrame getBaseFrame() {
        return baseFrame;
    }

    public void setBaseFrame(BaseFrame baseFrame) {
        this.baseFrame = baseFrame;
    }

    public String get(String key) {
        return getValue(key, String.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key, Class<T> clazz) {
        String raw = ext.get(key);
        raw = raw == null ? "" : raw;

        T value = null;

        if (clazz == String.class) {
            value = (T) raw;
        } else if (clazz == Integer.class) {
            value = (T) (raw.isEmpty() ? Integer.valueOf(-1) : Integer.valueOf(raw));
        } else if (clazz == Double.class) {
            value = (T) (raw.isEmpty() ? Double.valueOf(-1) : Double.valueOf(raw));
        }

        return value;
    }

    public String set(String key, String value) {
        if (ext == null) {
            ext = new LinkedHashMap<>();
        }
        return ext.put(key, value);
    }

    public void putAll(Map<String, String> map) {
        if (ext == null) {
            ext = new LinkedHashMap<>();
        }
        ext.putAll(map);
    }

    @Override
    public String toString() {
        return "map:" + ext.toString();
    }

    public Map<String, String> getAttributeMap() {
        return ext;
    }

}
