package ca.cplyon.cointime.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "coin_table")
data class Coin(
    @ColumnInfo(name = "Country") var country: String,
    @ColumnInfo(name = "Denomination") var denomination: String,
    @ColumnInfo(name = "Year") var year: Int,
    @ColumnInfo(name = "MintMark") var mintMark: String,
    @ColumnInfo(name = "Notes") var notes: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var coinId: Long = 0

    @ColumnInfo(name = "Obverse")
    var obverse: String? = null

    @ColumnInfo(name = "Reverse")
    var reverse: String? = null

    override fun toString(): String {
        return buildString {
            append(country)
            append(" ")
            append(denomination)
            append(" ")
            append(year)
            append(" ")
            append(mintMark)
            append(" ")
            append(notes)
        }
    }
}