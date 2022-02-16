package com.hncd.carcontrol.bean;

import com.luck.picture.lib.bean.CheckItemPhotoBean;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckAllBean extends BaseBean{

   private DataBean data;
   public DataBean getData() {
      return data;
   }

   public void setData(DataBean data) {
      this.data = data;
   }

   public static class DataBean {
      private List<CheckLineBean> checkLine;
      private List<CheckItemPhotoBean> checkItemPhoto;
      private List<CheckItemBean> checkItem;
      private List<CheckItemBean> checkItemRefit;
      private CheckApprove checkApprove;
      private String opreatType; //0新增，1更新

      public String getOpreatType() {
         return opreatType;
      }

      public void setOpreatType(String opreatType) {
         this.opreatType = opreatType;
      }

      public CheckApprove getCheckApprove() {
         if(checkApprove == null){
            checkApprove = new CheckApprove();
         }
         return checkApprove;
      }

      public void setCheckApprove(CheckApprove checkApprove) {
         this.checkApprove = checkApprove;
      }

      public List<CheckLineBean> getCheckLine() {
         return checkLine;
      }

      public void setCheckLine(List<CheckLineBean> checkLine) {
         this.checkLine = checkLine;
      }

      public List<CheckItemPhotoBean> getCheckItemPhoto() {
         return checkItemPhoto;
      }

      public void setCheckItemPhoto(List<CheckItemPhotoBean> checkItemPhoto) {
         this.checkItemPhoto = checkItemPhoto;
      }

      public List<CheckItemBean> getCheckItem() {
         return checkItem;
      }

      public void setCheckItem(List<CheckItemBean> checkItem) {
         this.checkItem = checkItem;
      }

      public List<CheckItemBean> getCheckItemRefit() {
         return checkItemRefit;
      }

      public void setCheckItemRefit(List<CheckItemBean> checkItemRefit) {
         this.checkItemRefit = checkItemRefit;
      }

      public static class CheckLineBean {
         private String lineNo;
         private String id;

         public String getLineNo() {
            return lineNo;
         }

         public void setLineNo(String lineNo) {
            this.lineNo = lineNo;
         }

         public String getId() {
            return id;
         }

         public void setId(String id) {
            this.id = id;
         }
      }

      public static class CheckItemBean {
         private List<LocalMedia>  picLists = new ArrayList<>();
         private int type;          //0是上半部分  1为改装部分
         private int isOkFlag;
         private String reason;
         private String photoPath;
         private String edcCheckLogOutId;
         private String edcCheckLogOutRefitId;
         private String createTime;
         private Object itemCfgId;
         private Object itemCfgRefitId;
         private Object businessTypeCode;
         private Object carTypeCode;
         private Object countryInOutCode;
         private Object useTypeCode;
         private String checkItemCode;
         private String checkItemName;
         private Object checkItemSimpleName;
         private Object attention;
         private Object requiredFlag;
         private Object enableFlag;
         private Object defVal;
         private Object orderName;

         public String getCreateTime() {
            return createTime;
         }

         public void setCreateTime(String createTime) {
            this.createTime = createTime;
         }

         public Object getItemCfgRefitId() {
            return itemCfgRefitId;
         }

         public void setItemCfgRefitId(Object itemCfgRefitId) {
            this.itemCfgRefitId = itemCfgRefitId;
         }

         public String getEdcCheckLogOutRefitId() {
            return edcCheckLogOutRefitId;
         }

         public void setEdcCheckLogOutRefitId(String edcCheckLogOutRefitId) {
            this.edcCheckLogOutRefitId = edcCheckLogOutRefitId;
         }

         public String getEdcCheckLogOutId() {
            return edcCheckLogOutId;
         }

         public void setEdcCheckLogOutId(String edcCheckLogOutId) {
            this.edcCheckLogOutId = edcCheckLogOutId;
         }

         public int getIsOkFlag() {
            return isOkFlag;
         }

         public void setIsOkFlag(int isOkFlag) {
            this.isOkFlag = isOkFlag;
         }

         public String getPhotoPath() {
            return photoPath;
         }

         public void setPhotoPath(String photoPath) {
            this.photoPath = photoPath;
         }

         public Object getItemCfgId() {
            return itemCfgId;
         }

         public void setItemCfgId(Object itemCfgId) {
            this.itemCfgId = itemCfgId;
         }

         public Object getBusinessTypeCode() {
            return businessTypeCode;
         }

         public void setBusinessTypeCode(Object businessTypeCode) {
            this.businessTypeCode = businessTypeCode;
         }

         public Object getCarTypeCode() {
            return carTypeCode;
         }

         public void setCarTypeCode(Object carTypeCode) {
            this.carTypeCode = carTypeCode;
         }

         public Object getCountryInOutCode() {
            return countryInOutCode;
         }

         public void setCountryInOutCode(Object countryInOutCode) {
            this.countryInOutCode = countryInOutCode;
         }

         public Object getUseTypeCode() {
            return useTypeCode;
         }

         public void setUseTypeCode(Object useTypeCode) {
            this.useTypeCode = useTypeCode;
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

         public Object getCheckItemSimpleName() {
            return checkItemSimpleName;
         }

         public void setCheckItemSimpleName(Object checkItemSimpleName) {
            this.checkItemSimpleName = checkItemSimpleName;
         }

         public Object getAttention() {
            return attention;
         }

         public void setAttention(Object attention) {
            this.attention = attention;
         }

         public Object getRequiredFlag() {
            return requiredFlag;
         }

         public void setRequiredFlag(Object requiredFlag) {
            this.requiredFlag = requiredFlag;
         }

         public Object getEnableFlag() {
            return enableFlag;
         }

         public void setEnableFlag(Object enableFlag) {
            this.enableFlag = enableFlag;
         }

         public Object getDefVal() {
            return defVal;
         }

         public void setDefVal(Object defVal) {
            this.defVal = defVal;
         }

         public Object getOrderName() {
            return orderName;
         }

         public void setOrderName(Object orderName) {
            this.orderName = orderName;
         }

         public String getReason() {
            return reason;
         }

         public void setReason(String reason) {
            this.reason = reason;
         }


         public int getType() {
            return type;
         }

         public void setType(int type) {
            this.type = type;
         }

         public List<LocalMedia> getPicLists() {
            return picLists;
         }

         public void setPicLists(List<LocalMedia> picLists) {
            this.picLists = picLists;
         }
      }

   public static class CheckApprove{
      private String checkLogOutApproveId,lsh,deptCode,checkNum,checkPeople,checkStatus,checkRemark,nvrLineId,newEnergyFlag;
      private String checkDate,checkStartDate;

      public String getCheckLogOutApproveId() {
         return checkLogOutApproveId;
      }

      public void setCheckLogOutApproveId(String checkLogOutApproveId) {
         this.checkLogOutApproveId = checkLogOutApproveId;
      }

      public String getLsh() {
         return lsh;
      }

      public void setLsh(String lsh) {
         this.lsh = lsh;
      }

      public String getDeptCode() {
         return deptCode;
      }

      public void setDeptCode(String deptCode) {
         this.deptCode = deptCode;
      }

      public String getCheckNum() {
         return checkNum;
      }

      public void setCheckNum(String checkNum) {
         this.checkNum = checkNum;
      }

      public String getCheckPeople() {
         return checkPeople;
      }

      public void setCheckPeople(String checkPeople) {
         this.checkPeople = checkPeople;
      }

      public String getCheckStatus() {
         return checkStatus;
      }

      public void setCheckStatus(String checkStatus) {
         this.checkStatus = checkStatus;
      }

      public String getCheckRemark() {
         return checkRemark;
      }

      public void setCheckRemark(String checkRemark) {
         this.checkRemark = checkRemark;
      }

      public String getNvrLineId() {
         return nvrLineId;
      }

      public void setNvrLineId(String nvrLineId) {
         this.nvrLineId = nvrLineId;
      }

      public String getNewEnergyFlag() {
         return newEnergyFlag;
      }

      public void setNewEnergyFlag(String newEnergyFlag) {
         this.newEnergyFlag = newEnergyFlag;
      }

      public String getCheckDate() {
         return checkDate;
      }

      public void setCheckDate(String checkDate) {
         this.checkDate = checkDate;
      }

      public String getCheckStartDate() {
         return checkStartDate;
      }

      public void setCheckStartDate(String checkStartDate) {
         this.checkStartDate = checkStartDate;
      }
   }


}
}
