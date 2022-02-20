package com.hncd.carcontrol.bean;

import java.util.List;

public class DisassemablVideo extends BaseBean{

   private DataBean data;

   public DataBean getData() {
      return data;
   }

   public void setData(DataBean data) {
      this.data = data;
   }

   public static class DataBean {
      private String lineNo;
      private String id;
      private String status;
      private List<CheckAllBean.DataBean.CheckLineBean> nvrLine;

      public String getlineNo() {
         return lineNo;
      }

      public void setlineNo(String lineNo) {
         this.lineNo = lineNo;
      }

      public String getId() {
         return id;
      }

      public void setId(String id) {
         this.id = id;
      }

      public String getStatus() {
         return status;
      }

      public void setStatus(String status) {
         this.status = status;
      }

      public List<CheckAllBean.DataBean.CheckLineBean> getNvrLine() {
         return nvrLine;
      }

      public void setNvrLine(List<CheckAllBean.DataBean.CheckLineBean> nvrLine) {
         this.nvrLine = nvrLine;
      }

      public static class NvrLineBean {
         private String id;
         private String lineNo;

         public String getId() {
            return id;
         }

         public void setId(String id) {
            this.id = id;
         }


         public String getLineNo() {
            return lineNo;
         }

         public void setLineNo(String lineNo) {
            this.lineNo = lineNo;
         }

      }
   }
}
