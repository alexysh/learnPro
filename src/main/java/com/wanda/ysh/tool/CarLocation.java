/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.tool;
/**
 * 类CarLocation.java的实现描述：
 * 车辆驶入停车场后的位置信息
 * @author yangshihong 2014-10-27 下午2:57:19
 */
public class CarLocation {
    
    /** 车牌号*/
    private String carLicense;
    /** 会员ID*/
    private String memberId;
    /** 停车场ID*/
    private Integer merchantId;
    /** 广场ID*/
    private Integer plazaId;
    /** 停车位楼层*/
    private String parkingSpaceFloor;
    /** 停车位号*/
    private String parkingSpaceNumber;
    
    /** 大厦ID */
    private String poiPlazaId;

    /** X坐标 */
    private String poiXCoord;

    /** Y坐标 */
    private String poiYCoord;
    
    public String getCarLicense() {
        return carLicense;
    }
    public void setCarLicense(String carLicense) {
        this.carLicense = carLicense;
    }
    public String getMemberId() {
        return memberId;
    }
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    public Integer getMerchantId() {
        return merchantId;
    }
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
    public Integer getPlazaId() {
        return plazaId;
    }
    public void setPlazaId(Integer plazaId) {
        this.plazaId = plazaId;
    }
    public String getParkingSpaceFloor() {
        return parkingSpaceFloor;
    }
    public void setParkingSpaceFloor(String parkingSpaceFloor) {
        this.parkingSpaceFloor = parkingSpaceFloor;
    }
    public String getParkingSpaceNumber() {
        return parkingSpaceNumber;
    }
    public void setParkingSpaceNumber(String parkingSpaceNumber) {
        this.parkingSpaceNumber = parkingSpaceNumber;
    }
    public String getPoiPlazaId() {
        return poiPlazaId;
    }
    public void setPoiPlazaId(String poiPlazaId) {
        this.poiPlazaId = poiPlazaId;
    }
    public String getPoiXCoord() {
        return poiXCoord;
    }
    public void setPoiXCoord(String poiXCoord) {
        this.poiXCoord = poiXCoord;
    }
    public String getPoiYCoord() {
        return poiYCoord;
    }
    public void setPoiYCoord(String poiYCoord) {
        this.poiYCoord = poiYCoord;
    }

}
