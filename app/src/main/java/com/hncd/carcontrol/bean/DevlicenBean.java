package com.hncd.carcontrol.bean;

public class DevlicenBean extends BaseBean{

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String drivingLicenseImg;

        public String getDrivingLicenseImg() {
            return drivingLicenseImg;
        }

        public void setDrivingLicenseImg(String drivingLicenseImg) {
            this.drivingLicenseImg = drivingLicenseImg;
        }
    }
}
