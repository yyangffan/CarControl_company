package com.luck.picture.lib.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.luck.picture.lib.entity.LocalMedia;

public class CheckItemPhotoBean implements Parcelable {
   private String checkItemCode;
   private String checkItemName;
   private boolean hasTake;
   private int imgPos;

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

   public boolean isHasTake() {
      return hasTake;
   }

   public void setHasTake(boolean hasTake) {
      this.hasTake = hasTake;
   }

   public int getImgPos() {
      return imgPos;
   }

   public void setImgPos(int imgPos) {
      this.imgPos = imgPos;
   }

   protected CheckItemPhotoBean (Parcel in){
      checkItemCode = in.readString();
      checkItemName = in.readString();
      imgPos = in.readInt();
      hasTake = in.readByte() != 0;
   }

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int i) {
      dest.writeString(checkItemCode);
      dest.writeString(checkItemName);
      dest.writeInt(imgPos);
      dest.writeByte((byte) (hasTake ? 1 : 0));

   }
   public static final Creator<CheckItemPhotoBean> CREATOR = new Creator<CheckItemPhotoBean>() {
      @Override
      public CheckItemPhotoBean createFromParcel(Parcel in) {
         return new CheckItemPhotoBean(in);
      }

      @Override
      public CheckItemPhotoBean[] newArray(int size) {
         return new CheckItemPhotoBean[size];
      }
   };


}
