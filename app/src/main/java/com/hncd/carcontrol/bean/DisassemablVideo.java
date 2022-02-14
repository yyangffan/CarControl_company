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
      private String lineId;
      private String id;
      private String status;
      private List<NvrLineBean> nvrLine;

      public String getLineId() {
         return lineId;
      }

      public void setLineId(String lineId) {
         this.lineId = lineId;
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

      public List<NvrLineBean> getNvrLine() {
         return nvrLine;
      }

      public void setNvrLine(List<NvrLineBean> nvrLine) {
         this.nvrLine = nvrLine;
      }

      public static class NvrLineBean {
         private String id;
         private Object deptCode;
         private Object deptName;
         private Object nvrCfgId;
         private Object lineName;
         private Object lineChannel;
         private String lineNo;
         private Object status;
         private Object remark;
         private Object stopReason;

         public String getId() {
            return id;
         }

         public void setId(String id) {
            this.id = id;
         }

         public Object getDeptCode() {
            return deptCode;
         }

         public void setDeptCode(Object deptCode) {
            this.deptCode = deptCode;
         }

         public Object getDeptName() {
            return deptName;
         }

         public void setDeptName(Object deptName) {
            this.deptName = deptName;
         }

         public Object getNvrCfgId() {
            return nvrCfgId;
         }

         public void setNvrCfgId(Object nvrCfgId) {
            this.nvrCfgId = nvrCfgId;
         }

         public Object getLineName() {
            return lineName;
         }

         public void setLineName(Object lineName) {
            this.lineName = lineName;
         }

         public Object getLineChannel() {
            return lineChannel;
         }

         public void setLineChannel(Object lineChannel) {
            this.lineChannel = lineChannel;
         }

         public String getLineNo() {
            return lineNo;
         }

         public void setLineNo(String lineNo) {
            this.lineNo = lineNo;
         }

         public Object getStatus() {
            return status;
         }

         public void setStatus(Object status) {
            this.status = status;
         }

         public Object getRemark() {
            return remark;
         }

         public void setRemark(Object remark) {
            this.remark = remark;
         }

         public Object getStopReason() {
            return stopReason;
         }

         public void setStopReason(Object stopReason) {
            this.stopReason = stopReason;
         }
      }
   }
}
