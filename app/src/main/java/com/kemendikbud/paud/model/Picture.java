package com.kemendikbud.paud.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

/**
 * Created by akbar on 09/08/17.
 */

public class Picture implements Parcelable {
    private String imageId;
    private String imageUrl;
    private String formattedTime;
    private String timestamp;
    private Double longitude;
    private Double latitude;
    private String keterangan;
    private Integer kategoriId;

    public Picture(){
    }

    public Picture(String imageId, String imageUrl, String timestamp, Double longitude, Double latitude, String keterangan) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.longitude = longitude;
        this.latitude = latitude;
        this.keterangan = keterangan;
    }

    protected Picture(Parcel in) {
        imageId = in.readString();
        imageUrl = in.readString();
        formattedTime = in.readString();
        timestamp = in.readString();
        keterangan = in.readString();
    }

    public static final Creator<Picture> CREATOR = new Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel in) {
            return new Picture(in);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getKategoriId() {
        return kategoriId;
    }

    public void setKategoriId(Integer kategoriId) {
        this.kategoriId = kategoriId;
    }

    public void setTime(long time) {
//        this.timestamp = time;

        long oneDayInMillis = 24 * 60 * 60 * 1000;
        long timeDifference = System.currentTimeMillis() - time;

        if(timeDifference < oneDayInMillis){
            formattedTime = DateFormat.format("hh:mm a", time).toString();
        }else{
            formattedTime = DateFormat.format("dd MMM - hh:mm a", time).toString();
        }
    }

//    public String getFormattedTime() {
        long oneDayInMillis = 24 * 60 * 60 * 1000;
//        long timeDifference = System.currentTimeMillis() - timestamp;

//        if(timeDifference < oneDayInMillis){
//            return DateFormat.format("hh:mm a", timestamp).toString();
//        }else{
//            return DateFormat.format("dd MMM - hh:mm a", timestamp).toString();
//        }
//    }

//    public CharSequence getTimeAgo(){
//        return DateUtils.getRelativeTimeSpanString(
//                timestamp,
//                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
//    }

    public void setValues(Picture newPicture){
        imageUrl = newPicture.imageUrl;
        formattedTime = newPicture.formattedTime;
        timestamp = newPicture.timestamp;
        longitude = newPicture.longitude;
        latitude = newPicture.latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageId);
        parcel.writeString(imageUrl);
        parcel.writeString(formattedTime);
        parcel.writeString(timestamp);
        parcel.writeString(keterangan);
    }
}
