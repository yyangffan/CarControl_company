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
      private List<Map<String,Object>> imageBeans;

      public List<Map<String,Object>> getImageBeans() {
         return imageBeans;
      }

      public void setImageBeans(List<Map<String,Object>> imageBeas) {
         imageBeans = imageBeas;
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
         private String checkItemCode;
         private String checkItemName;
         private List<LocalMedia>  picLists = new ArrayList<>();
         private int type;          //0是上半部分  1为改装部分
         private int typeone;
         private int typetwo;
         private String reason;

         public int getTypetwo() {
            return typetwo;
         }

         public void setTypetwo(int typetwo) {
            this.typetwo = typetwo;
         }

         public String getReason() {
            return reason;
         }

         public void setReason(String reason) {
            this.reason = reason;
         }

         public int getTypeone() {
            return typeone;
         }

         public void setTypeone(int typeone) {
            this.typeone = typeone;
         }

         public int getType() {
            return type;
         }

         public void setType(int type) {
            this.type = type;
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


         public List<LocalMedia> getPicLists() {
            return picLists;
         }

         public void setPicLists(List<LocalMedia> picLists) {
            this.picLists = picLists;
         }
      }


   }
}
