package com.hncd.carcontrol.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DissVideoListBean extends BaseBean{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        @SerializedName("LSH")
        private String lSH;
        @SerializedName("DEPT_ID")
        private String dEPT_ID;
        @SerializedName("STATUS")
        private int sTATUS;
        @SerializedName("STARTTIME")
        private String sTARTTIME;
        @SerializedName("ENDTIME")
        private String eNDTIME;
        @SerializedName("NUM")
        private int nUM;
        @SerializedName("ID")
        private String iD;
        @SerializedName("LINE_NO")
        private String lINE_NO;

        public String getLSH() {
            return lSH;
        }

        public void setLSH(String lSH) {
            this.lSH = lSH;
        }

        public String getDEPT_ID() {
            return dEPT_ID;
        }

        public void setDEPT_ID(String dEPT_ID) {
            this.dEPT_ID = dEPT_ID;
        }

        public int getSTATUS() {
            return sTATUS;
        }

        public void setSTATUS(int sTATUS) {
            this.sTATUS = sTATUS;
        }

        public String getSTARTTIME() {
            return sTARTTIME;
        }

        public void setSTARTTIME(String sTARTTIME) {
            this.sTARTTIME = sTARTTIME;
        }

        public String getENDTIME() {
            return eNDTIME;
        }

        public void setENDTIME(String eNDTIME) {
            this.eNDTIME = eNDTIME;
        }

        public int getNUM() {
            return nUM;
        }

        public void setNUM(int nUM) {
            this.nUM = nUM;
        }

        public String getID() {
            return iD;
        }

        public void setID(String iD) {
            this.iD = iD;
        }

        public String getLINE_NO() {
            return lINE_NO;
        }

        public void setLINE_NO(String lINE_NO) {
            this.lINE_NO = lINE_NO;
        }
    }
}
