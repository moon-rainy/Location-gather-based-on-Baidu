package com.example.maplocationdemo.DataBase;

public class Address {
    private String code;
    private String phone;
    private String time;
    private Integer locType;
    private String locTypeDescription;
    private Double latitude;
    private Double longitude;
    private Double radius;
    private String province;
    private String cityCode;
    private String city;
    private String district;
    private String town;
    private String street;
    private String addr;
    private String streetNumber;
    private Integer userIndoorState;
    private String direction;
    private String locationDescribe;
    private double height;

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Address(String code, String phone, String time, Integer locType, String locTypeDescription, Double latitude, Double longitude, Double radius, String province, String cityCode, String city, String district, String town, String street, String addr, String streetNumber, Integer userIndoorState, String direction, String locationDescribe) {
        this.code = code;
        this.phone = phone;
        this.time = time;
        this.locType = locType;
        this.locTypeDescription = locTypeDescription;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.province = province;
        this.cityCode = cityCode;
        this.city = city;
        this.district = district;
        this.town = town;
        this.street = street;
        this.addr = addr;
        this.streetNumber = streetNumber;
        this.userIndoorState = userIndoorState;
        this.direction = direction;
        this.locationDescribe = locationDescribe;
    }


    public Address() {
    }


    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "Address{" +
                "code'" + code + '\'' +
                "phone='" + phone + '\'' +
                ", time='" + time + '\'' +
                ", locType=" + locType +
                ", locTypeDescription='" + locTypeDescription + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                ", province='" + province + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", town='" + town + '\'' +
                ", street='" + street + '\'' +
                ", addr='" + addr + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", userIndoorState=" + userIndoorState +
                ", direction='" + direction + '\'' +
                ", locationDescribe='" + locationDescribe + '\'' +
                '}';
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getLocType() {
        return locType;
    }

    public void setLocType(Integer locType) {
        this.locType = locType;
    }

    public String getLocTypeDescription() {
        return locTypeDescription;
    }

    public void setLocTypeDescription(String locTypeDescription) {
        this.locTypeDescription = locTypeDescription;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public Integer getUserIndoorState() {
        return userIndoorState;
    }

    public void setUserIndoorState(Integer userIndoorState) {
        this.userIndoorState = userIndoorState;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getLocationDescribe() {
        return locationDescribe;
    }

    public void setLocationDescribe(String locationDescribe) {
        this.locationDescribe = locationDescribe;
    }
}
