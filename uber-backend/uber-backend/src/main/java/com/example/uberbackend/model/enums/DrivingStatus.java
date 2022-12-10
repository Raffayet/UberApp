package com.example.uberbackend.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum DrivingStatus {
    ONLINE(0),
    OFFLINE(1),
    ONLINE_BUSY(2);

    private int value;
    private static Map map = new HashMap<>();

    private DrivingStatus(int value) {
        this.value = value;
    }

    static {
        for (DrivingStatus pageType : DrivingStatus.values()) {
            map.put(pageType.value, pageType);
        }
    }

    public static DrivingStatus valueOf(int pageType) {
        return (DrivingStatus) map.get(pageType);
    }

    public int getValue() {
        return value;
    }

}
