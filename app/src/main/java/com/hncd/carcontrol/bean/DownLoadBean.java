package com.hncd.carcontrol.bean;

public class DownLoadBean extends BaseBean{
   private DataBean data;

   public DataBean getData() {
      return data;
   }

   public void setData(DataBean data) {
      this.data = data;
   }

   public static class DataBean {
      private String id;
      private String cfgType;
      private String currentVersion;
      private String downLoadPath;
      private Integer status;

      public String getId() {
         return id;
      }

      public void setId(String id) {
         this.id = id;
      }

      public String getCfgType() {
         return cfgType;
      }

      public void setCfgType(String cfgType) {
         this.cfgType = cfgType;
      }

      public String getCurrentVersion() {
         return currentVersion;
      }

      public void setCurrentVersion(String currentVersion) {
         this.currentVersion = currentVersion;
      }

      public String getDownLoadPath() {
         return downLoadPath;
      }

      public void setDownLoadPath(String downLoadPath) {
         this.downLoadPath = downLoadPath;
      }

      public Integer getStatus() {
         return status;
      }

      public void setStatus(Integer status) {
         this.status = status;
      }
   }
}
