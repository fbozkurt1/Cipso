package com.example.cipso;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String Name;
    private String Surname;
    private String PhoneNum;
    private String Email;

    public User(String name, String surname, String phoneNum, String email) {
        Name = name;
        Surname = surname;
        PhoneNum = phoneNum;
        Email = email;
    }

    protected User(Parcel in) {
        Name = in.readString();
        Surname = in.readString();
        PhoneNum = in.readString();
        Email = in.readString();
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

    public String getName() {
        return Name;
    }

    public String getSurname() {
        return Surname;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public String getEmail() {
        return Email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(Name);
        dest.writeString(Surname);
        dest.writeString(PhoneNum);
        dest.writeString(Email);
    }
}

