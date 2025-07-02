package com.example.ws50.api.model

import android.os.Parcel
import android.os.Parcelable

data class Transfer(
    val id: String?,
    val toNumber: String?,
    val toItem: String?,
    val material: String?,
    val storageLocation: String?,
    val warehouse: String?,
    val storageBin: String?,
    val qtyPick: Double?,
    val qtyConfirm: Double?,
    val pick: Boolean?,
    val confirm: Boolean?,
    val createdDate: String?,
    val lastModifiedDate: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(toNumber)
        parcel.writeString(toItem)
        parcel.writeString(material)
        parcel.writeString(storageLocation)
        parcel.writeString(warehouse)
        parcel.writeString(storageBin)
        parcel.writeValue(qtyPick)
        parcel.writeValue(qtyConfirm)
        parcel.writeValue(pick)
        parcel.writeValue(confirm)
        parcel.writeString(createdDate)
        parcel.writeString(lastModifiedDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transfer> {
        override fun createFromParcel(parcel: Parcel): Transfer {
            return Transfer(parcel)
        }

        override fun newArray(size: Int): Array<Transfer?> {
            return arrayOfNulls(size)
        }
    }
}
