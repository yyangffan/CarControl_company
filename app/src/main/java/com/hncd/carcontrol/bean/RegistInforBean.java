package com.hncd.carcontrol.bean;

import java.util.List;

public class RegistInforBean extends BaseBean{

   private DataBean data;

   public DataBean getData() {
      return data;
   }

   public void setData(DataBean data) {
      this.data = data;
   }

   public static class DataBean {
      private Integer auditStatus;
      private List<RegInfoBean> regInfo;

      public Integer getAuditStatus() {
         return auditStatus;
      }

      public void setAuditStatus(Integer auditStatus) {
         this.auditStatus = auditStatus;
      }

      public List<RegInfoBean> getRegInfo() {
         return regInfo;
      }

      public void setRegInfo(List<RegInfoBean> regInfo) {
         this.regInfo = regInfo;
      }

      public static class RegInfoBean {
         private String name;
         private String value;

         public String getName() {
            return name;
         }

         public void setName(String name) {
            this.name = name;
         }

         public String getValue() {
            return value;
         }

         public void setValue(String value) {
            this.value = value;
         }
      }
   }
}
