package com.hwatong.btphone.bean;

import java.util.Comparator;

import com.hwatong.btphone.util.L;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable, Comparable<Contact> {
	public String name = "";
	public String number = "";
	/**
	 * full pinyin of name
	 */
	public String comFlg = "";
	public String firstLetters = "";

	public Contact() {}

	public Contact(String name, String number, String comFlg, String firstLetters) {
		this.name = name;
		this.number = number;
		this.comFlg = comFlg;
		this.firstLetters = firstLetters;
		L.dRoll("[Contact]", "name: " + name + " number: " + number + " comFlg: " + comFlg + " firstLetters: " + firstLetters);
	}

	public static class ContactComparator implements Comparator<Contact> {
		@Override
		public int compare(Contact lhs, Contact rhs) {
			return lhs.comFlg.compareToIgnoreCase(rhs.comFlg);
		}
	}


	@Override
	public int compareTo(Contact another) {
		int nameCompare = 1;
		if (this.comFlg.startsWith("#") || another.comFlg.startsWith("#")) {
			nameCompare = another.comFlg.compareToIgnoreCase(this.comFlg);
			if(nameCompare == 0) {
				return another.number.compareToIgnoreCase(this.number);
			} else {
				return nameCompare;
			}
		}
		nameCompare = this.comFlg.compareToIgnoreCase(another.comFlg);
		if(nameCompare == 0 ) {
			return this.number.compareToIgnoreCase(another.number);
		} else {
			return nameCompare;
		}
		
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Contact){
			Contact contact = (Contact) o;
			return name.equals(contact.name) && number.equals(contact.number);
		}
		return super.equals(o);
	}
	

	
	public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
		public Contact createFromParcel(Parcel in) {
			return new Contact(in);
		}

		public Contact[] newArray(int size) {
			return new Contact[size];
		}
	};

	private Contact(Parcel in) {
		readFromParcel(in);
	}

	public void readFromParcel(Parcel in) {
		name = in.readString();
		number = in.readString();
		comFlg = in.readString();
		firstLetters = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(number);
		dest.writeString(comFlg);
		dest.writeString(firstLetters);
	}
	
	
	
}
