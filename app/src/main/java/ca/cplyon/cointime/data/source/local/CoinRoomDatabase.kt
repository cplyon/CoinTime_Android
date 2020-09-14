package ca.cplyon.cointime.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ca.cplyon.cointime.data.Coin

// Annotates class to be a Room Database with a table (entity) of the Coin class
// code lifted from
// https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/index.html

// TODO: set a directory for Room to use to export the schema so we can check the current schema into git
@Database(entities = [Coin::class], version = 1, exportSchema = false)
abstract class CoinRoomDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: CoinRoomDatabase? = null

        fun getDatabase(context: Context): CoinRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CoinRoomDatabase::class.java,
                    "Coins.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}