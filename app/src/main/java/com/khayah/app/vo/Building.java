package com.khayah.app.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Building implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_mm")
    @Expose
    private String nameMm;
    @SerializedName("images")
    @Expose
    private String images;
    @SerializedName("building_structure")
    @Expose
    private String buildingStructure;
    @SerializedName("building_structure_mm")
    @Expose
    private String buildingStructureMm;
    @SerializedName("building_type_id")
    @Expose
    private Integer buildingTypeId;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("city_id")
    @Expose
    private Integer cityId;
    @SerializedName("township_id")
    @Expose
    private Integer townshipId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("address_mm")
    @Expose
    private String addressMm;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("developed_yrs")
    @Expose
    private Integer developedYrs;
    @SerializedName("building_age")
    @Expose
    private Integer buildingAge;
    @SerializedName("is_got_blue_mark")
    @Expose
    private Integer isGotBlueMark;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("description_mm")
    @Expose
    private String descriptionMm;
    @SerializedName("key_note")
    @Expose
    private String keyNote;
    @SerializedName("key_note_mm")
    @Expose
    private String keyNoteMm;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("country")
   /* @Expose
    private Country country;
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("township")
    @Expose
    private Township township;
    @SerializedName("type")
    @Expose
    private Type type;*/

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

    public String getNameMm() {
        return nameMm;
    }

    public void setNameMm(String nameMm) {
        this.nameMm = nameMm;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getBuildingStructure() {
        return buildingStructure;
    }

    public void setBuildingStructure(String buildingStructure) {
        this.buildingStructure = buildingStructure;
    }

    public String getBuildingStructureMm() {
        return buildingStructureMm;
    }

    public void setBuildingStructureMm(String buildingStructureMm) {
        this.buildingStructureMm = buildingStructureMm;
    }

    public Integer getBuildingTypeId() {
        return buildingTypeId;
    }

    public void setBuildingTypeId(Integer buildingTypeId) {
        this.buildingTypeId = buildingTypeId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressMm() {
        return addressMm;
    }

    public void setAddressMm(String addressMm) {
        this.addressMm = addressMm;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getDevelopedYrs() {
        return developedYrs;
    }

    public void setDevelopedYrs(Integer developedYrs) {
        this.developedYrs = developedYrs;
    }

    public Integer getBuildingAge() {
        return buildingAge;
    }

    public void setBuildingAge(Integer buildingAge) {
        this.buildingAge = buildingAge;
    }

    public Integer getIsGotBlueMark() {
        return isGotBlueMark;
    }

    public void setIsGotBlueMark(Integer isGotBlueMark) {
        this.isGotBlueMark = isGotBlueMark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionMm() {
        return descriptionMm;
    }

    public void setDescriptionMm(String descriptionMm) {
        this.descriptionMm = descriptionMm;
    }

    public String getKeyNote() {
        return keyNote;
    }

    public void setKeyNote(String keyNote) {
        this.keyNote = keyNote;
    }

    public String getKeyNoteMm() {
        return keyNoteMm;
    }

    public void setKeyNoteMm(String keyNoteMm) {
        this.keyNoteMm = keyNoteMm;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    /*public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Township getTownship() {
        return township;
    }

    public void setTownship(Township township) {
        this.township = township;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }*/

}