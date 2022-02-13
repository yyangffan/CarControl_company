package com.hncd.carcontrol.bean;

import com.google.gson.annotations.SerializedName;

public class LoginBean extends BaseBean{
   private DataBean data;
   public DataBean getData() {
      return data;
   }

   public void setData(DataBean data) {
      this.data = data;
   }


   public static class DataBean {
      /**
       * name : 陈世杰
       * userId : 陈世杰
       */

      private String name;
      @SerializedName("userId")
      private String userIdX;

      public String getName() {
         return name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public String getUserIdX() {
         return userIdX;
      }

      public void setUserIdX(String userIdX) {
         this.userIdX = userIdX;
      }
   }
}
