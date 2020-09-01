package ca.cplyon.cointime.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "coin_table")
data class Coin(
    @ColumnInfo(name = "Country") val country: String,
    @ColumnInfo(name = "Denomination") val denomination: String,
    @ColumnInfo(name = "Year") val year: Int,
    @ColumnInfo(name = "MintMark") val mintMark: String,
    @ColumnInfo(name = "Notes") val notes: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var coinId: Int = 0
}