package com.example.Contribute;

import com.google.firebase.Timestamp;

public class model {
    String name;
    String type;

    public String getTimeID() {
        return timeID;
    }

    public void setTimeID(String timeID) {
        this.timeID = timeID;
    }

    String timeID;

    public model(String name, String type, String timeID, String description, String userid, String pincode, String address, String phone, String latitude, String longitude, String isOpen, String foodDurationTime, Timestamp timestamp, String thingstoDonate) {
        this.name = name;
        this.type = type;
        this.timeID = timeID;
        this.description = description;
        this.userid = userid;
        this.pincode = pincode;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isOpen = isOpen;
        this.foodDurationTime = foodDurationTime;
        this.timestamp = timestamp;
        this.thingstoDonate = thingstoDonate;
    }

    String description;
    String userid;
    String pincode;
    String address;
    String phone;
    String latitude;
    String longitude;
    String isOpen;
    String foodDurationTime;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public model(String name, String type, String description, String userid, String pincode, String address, String phone, String latitude, String longitude, String isOpen, String foodDurationTime, Timestamp timestamp, String thingstoDonate) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.userid = userid;
        this.pincode = pincode;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isOpen = isOpen;
        this.foodDurationTime = foodDurationTime;
        this.timestamp = timestamp;
        this.thingstoDonate = thingstoDonate;
    }

    Timestamp timestamp;

    public String getFoodDurationTime() {
        return foodDurationTime;
    }

    public void setFoodDurationTime(String foodDurationTime) {
        this.foodDurationTime = foodDurationTime;
    }

    public model(String name, String type, String description, String userid, String pincode, String address, String phone, String latitude, String longitude, String isOpen, String foodDurationTime, String thingstoDonate) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.userid = userid;
        this.pincode = pincode;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isOpen = isOpen;
        this.foodDurationTime = foodDurationTime;
        this.thingstoDonate = thingstoDonate;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public model(String name, String type, String description, String userid, String pincode, String address, String phone, String latitude, String longitude, String isOpen, String thingstoDonate) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.userid = userid;
        this.pincode = pincode;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isOpen = isOpen;
        this.thingstoDonate = thingstoDonate;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getThingstoDonate() {
        return thingstoDonate;
    }

    public void setThingstoDonate(String thingstoDonate) {
        this.thingstoDonate = thingstoDonate;
    }

    public model(String name, String type, String description, String userid, String pincode, String address, String phone, String latitude, String longitude, String thingstoDonate) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.userid = userid;
        this.pincode = pincode;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.thingstoDonate = thingstoDonate;
    }

    String thingstoDonate;

    public model() {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public model(String name, String type, String description, String userid, String pincode, String address, String phone) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.userid = userid;
        this.pincode=pincode;
        this.address=address;
        this.phone=phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
