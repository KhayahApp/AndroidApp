package com.khayah.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lawer {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("name")
@Expose
private String name;
@SerializedName("address")
@Expose
private String address;
@SerializedName("city_id")
@Expose
private Integer cityId;
@SerializedName("township_id")
@Expose
private Integer townshipId;
@SerializedName("phone")
@Expose
private String phone;
@SerializedName("office_address")
@Expose
private String officeAddress;
@SerializedName("service_fees")
@Expose
private Integer serviceFees;
@SerializedName("remark")
@Expose
private Object remark;
@SerializedName("status")
@Expose
private Object status;
@SerializedName("hotline_numbers")
@Expose
private String hotlineNumbers;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("updated_at")
@Expose
private String updatedAt;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getAddress() {
return address;
}

public void setAddress(String address) {
this.address = address;
}

public Integer getCityId() {
return cityId;
}

public void setCityId(Integer cityId) {
this.cityId = cityId;
}

public Integer getTownshipId() {
return townshipId;
}

public void setTownshipId(Integer townshipId) {
this.townshipId = townshipId;
}

public String getPhone() {
return phone;
}

public void setPhone(String phone) {
this.phone = phone;
}

public String getOfficeAddress() {
return officeAddress;
}

public void setOfficeAddress(String officeAddress) {
this.officeAddress = officeAddress;
}

public Integer getServiceFees() {
return serviceFees;
}

public void setServiceFees(Integer serviceFees) {
this.serviceFees = serviceFees;
}

public Object getRemark() {
return remark;
}

public void setRemark(Object remark) {
this.remark = remark;
}

public Object getStatus() {
return status;
}

public void setStatus(Object status) {
this.status = status;
}

public String getHotlineNumbers() {
return hotlineNumbers;
}

public void setHotlineNumbers(String hotlineNumbers) {
this.hotlineNumbers = hotlineNumbers;
}

public String getCreatedAt() {
return createdAt;
}

public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

public String getUpdatedAt() {
return updatedAt;
}

public void setUpdatedAt(String updatedAt) {
this.updatedAt = updatedAt;
}

}