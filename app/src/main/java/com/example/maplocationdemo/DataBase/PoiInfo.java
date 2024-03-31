package com.example.maplocationdemo.DataBase;

public class PoiInfo {
    String code;
    String poiName;
    String poiATag;


    @Override
    public String toString() {
        return "PoiInfo{" +
                "code'" + code + '\'' +
                "poiName='" + poiName + '\'' +
                ", poiATag='" + poiATag + '\'' +
                '}';
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public String getPoiATag() {
        return poiATag;
    }

    public void setPoiATag(String poiATag) {
        this.poiATag = poiATag;
    }

    public PoiInfo() {
    }
}
