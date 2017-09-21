package com.kemendikbud.paud.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User extends MessageResponse implements Parcelable{

	private UserData data;
	private List<Picture> pictures = null;
	private List<Category> categories;

	public User() {
	}

	protected User(Parcel in) {
		if (in.readByte() == 0x01) {
			pictures = new ArrayList<>();
			in.readList(pictures, Picture.class.getClassLoader());
		} else {
			pictures = null;
		}
	}

	public static final Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

	public UserData getData() {
		return data;
	}

	public void setData(UserData data) {
		this.data = data;
	}

	public List<Picture> getPictures() {
		return pictures;
	}

	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeTypedList(pictures);
	}
}
