package com.hncd.carcontrol.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageListBean extends BaseBean{

   private List<DataBean> data;

   public List<DataBean> getData() {
      return data;
   }

   public void setData(List<DataBean> data) {
      this.data = data;
   }

   public static class DataBean {
      @SerializedName("TITILE")
      private String titile;
      @SerializedName("NUM")
      private Integer num;
      @SerializedName("ID")
      private String id;
      @SerializedName("CONTENT")
      private String content;
      @SerializedName("CREATETIME")
      private Long createtime;

      public String getTitile() {
         return titile;
      }

      public void setTitile(String titile) {
         this.titile = titile;
      }

      public Integer getNum() {
         return num;
      }

      public void setNum(Integer num) {
         this.num = num;
      }

      public String getId() {
         return id;
      }

      public void setId(String id) {
         this.id = id;
      }

      public String getContent() {
         return content;
      }

      public void setContent(String content) {
         this.content = content;
      }

      public Long getCreatetime() {
         return createtime;
      }

      public void setCreatetime(Long createtime) {
         this.createtime = createtime;
      }
   }
}
