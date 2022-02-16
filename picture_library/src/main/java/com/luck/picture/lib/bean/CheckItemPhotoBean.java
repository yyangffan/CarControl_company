package com.luck.picture.lib.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.luck.picture.lib.entity.LocalMedia;

public class CheckItemPhotoBean implements Parcelable {
    private String checkItemCode;
    private String checkItemName;
    private String photoPath;
    private boolean hasTake;
    private int imgPos;
    private String itemCfgId;
    private String businessTypeCode;
    private String carTypeCode;
    private String countryInOutCode;
    private String useTypeCode;
    private String checkItemSimpleName;
    private String attention;
    private String requiredFlag;
    private String enableFlag;
    private String defVal;
    private String orderName;
    private String edcCheckLogOutPhotoId;
    private String itemCfgPhotoId;
    private String createTime;

    public String getEdcCheckLogOutPhotoId() {
        return edcCheckLogOutPhotoId;
    }

    public void setEdcCheckLogOutPhotoId(String edcCheckLogOutPhotoId) {
        this.edcCheckLogOutPhotoId = edcCheckLogOutPhotoId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getItemCfgPhotoId() {
        return itemCfgPhotoId;
    }

    public void setItemCfgPhotoId(String itemCfgPhotoId) {
        this.itemCfgPhotoId = itemCfgPhotoId;
    }

    public String getCheckItemCode() {
        return checkItemCode;
    }

    public void setCheckItemCode(String checkItemCode) {
        this.checkItemCode = checkItemCode;
    }

    public String getCheckItemName() {
        return checkItemName;
    }

    public void setCheckItemName(String checkItemName) {
        this.checkItemName = checkItemName;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public boolean isHasTake() {
        return hasTake;
    }

    public void setHasTake(boolean hasTake) {
        this.hasTake = hasTake;
    }

    public int getImgPos() {
        return imgPos;
    }

    public void setImgPos(int imgPos) {
        this.imgPos = imgPos;
    }

    public String getItemCfgId() {
        return itemCfgId;
    }

    public void setItemCfgId(String itemCfgId) {
        this.itemCfgId = itemCfgId;
    }

    public String getBusinessTypeCode() {
        return businessTypeCode;
    }

    public void setBusinessTypeCode(String businessTypeCode) {
        this.businessTypeCode = businessTypeCode;
    }

    public String getCarTypeCode() {
        return carTypeCode;
    }

    public void setCarTypeCode(String carTypeCode) {
        this.carTypeCode = carTypeCode;
    }

    public String getCountryInOutCode() {
        return countryInOutCode;
    }

    public void setCountryInOutCode(String countryInOutCode) {
        this.countryInOutCode = countryInOutCode;
    }

    public String getUseTypeCode() {
        return useTypeCode;
    }

    public void setUseTypeCode(String useTypeCode) {
        this.useTypeCode = useTypeCode;
    }

    public String getCheckItemSimpleName() {
        return checkItemSimpleName;
    }

    public void setCheckItemSimpleName(String checkItemSimpleName) {
        this.checkItemSimpleName = checkItemSimpleName;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getRequiredFlag() {
        return requiredFlag;
    }

    public void setRequiredFlag(String requiredFlag) {
        this.requiredFlag = requiredFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getDefVal() {
        return defVal;
    }

    public void setDefVal(String defVal) {
        this.defVal = defVal;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.checkItemCode);
        dest.writeString(this.checkItemName);
        dest.writeString(this.photoPath);
        dest.writeByte(this.hasTake ? (byte) 1 : (byte) 0);
        dest.writeInt(this.imgPos);
        dest.writeString(this.itemCfgId);
        dest.writeString(this.businessTypeCode);
        dest.writeString(this.carTypeCode);
        dest.writeString(this.countryInOutCode);
        dest.writeString(this.useTypeCode);
        dest.writeString(this.checkItemSimpleName);
        dest.writeString(this.attention);
        dest.writeString(this.requiredFlag);
        dest.writeString(this.enableFlag);
        dest.writeString(this.defVal);
        dest.writeString(this.orderName);
        dest.writeString(this.edcCheckLogOutPhotoId);
        dest.writeString(this.createTime);
        dest.writeString(this.itemCfgPhotoId);
    }

    public void readFromParcel(Parcel source) {
        this.checkItemCode = source.readString();
        this.checkItemName = source.readString();
        this.photoPath = source.readString();
        this.hasTake = source.readByte() != 0;
        this.imgPos = source.readInt();
        this.itemCfgId = source.readString();
        this.businessTypeCode = source.readString();
        this.carTypeCode = source.readString();
        this.countryInOutCode = source.readString();
        this.useTypeCode = source.readString();
        this.checkItemSimpleName = source.readString();
        this.attention = source.readString();
        this.requiredFlag = source.readString();
        this.enableFlag = source.readString();
        this.defVal = source.readString();
        this.orderName = source.readString();
        this.edcCheckLogOutPhotoId = source.readString();
        this.createTime = source.readString();
        this.itemCfgPhotoId = source.readString();
    }

    public CheckItemPhotoBean() {
    }

    protected CheckItemPhotoBean(Parcel in) {
        this.checkItemCode = in.readString();
        this.checkItemName = in.readString();
        this.photoPath = in.readString();
        this.hasTake = in.readByte() != 0;
        this.imgPos = in.readInt();
        this.itemCfgId = in.readString();
        this.businessTypeCode = in.readString();
        this.carTypeCode = in.readString();
        this.countryInOutCode = in.readString();
        this.useTypeCode = in.readString();
        this.checkItemSimpleName = in.readString();
        this.attention = in.readString();
        this.requiredFlag = in.readString();
        this.enableFlag = in.readString();
        this.defVal = in.readString();
        this.orderName = in.readString();
        this.edcCheckLogOutPhotoId = in.readString();
        this.createTime = in.readString();
        this.itemCfgPhotoId = in.readString();
    }

    public static final Parcelable.Creator<CheckItemPhotoBean> CREATOR = new Parcelable.Creator<CheckItemPhotoBean>() {
        @Override
        public CheckItemPhotoBean createFromParcel(Parcel source) {
            return new CheckItemPhotoBean(source);
        }

        @Override
        public CheckItemPhotoBean[] newArray(int size) {
            return new CheckItemPhotoBean[size];
        }
    };
}
