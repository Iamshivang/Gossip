package com.example.gossip.domain.models

import android.os.Parcel
import android.os.Parcelable

data class User(
    var name: String? = null,
    var email: String? = null,
    var uid: String? = null,
    var url: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(uid)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}

//class User {
//
//    var name: String?= null
//    var email: String?= null
//    var uid: String? = null
//    var bio: String?= null
//    var url: String?= null
//
//    constructor(){}
//
//    constructor(name: String?, email: String?, uid: String?, bio:String?, url: String?){
//        this.name= name
//        this.email= email
//        this.uid= uid
//        this.bio= bio
//        this.url= url
//    }
//}