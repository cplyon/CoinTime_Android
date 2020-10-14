package ca.cplyon.cointime.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import ca.cplyon.cointime.data.Coin
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CoinDaoTest {

    private lateinit var database: CoinRoomDatabase

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            CoinRoomDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertCoin() = runBlockingTest {
        // GIVEN - Insert a coin.
        val coin = Coin("Canada", "25 cents", 2001, "P", "")
        coin.coinId = database.coinDao().addCoin(coin)

        // WHEN - Get coins from the database.
        val loaded = database.coinDao().getAllCoins()

        // THEN - The loaded data contains the expected values.
        assertThat(loaded, notNullValue())
        assertThat(loaded.size, `is`(1))
        assertThat(loaded[0].coinId, `is`(coin.coinId))
        assertThat(loaded[0].country, `is`(coin.country))
        assertThat(loaded[0].denomination, `is`(coin.denomination))
        assertThat(loaded[0].year, `is`(coin.year))
        assertThat(loaded[0].mintMark, `is`(coin.mintMark))
        assertThat(loaded[0].notes, `is`(coin.notes))
        assertThat(loaded[0].obverse, `is`(coin.obverse))
        assertThat(loaded[0].reverse, `is`(coin.reverse))
    }

    @Test(expected = android.database.sqlite.SQLiteConstraintException::class)
    fun insertCoin_abort() = runBlockingTest {
        // GIVEN - Insert a coin.
        val coin = Coin("Canada", "25 cents", 2001, "P", "")
        coin.coinId = database.coinDao().addCoin(coin)

        // WHEN - add same coin again
        database.coinDao().addCoin(coin)

        // THEN - The expected exception gets thrown.
    }

    @Test
    fun deleteCoin() = runBlockingTest {
        // GIVEN - Insert and delete a coin.
        val coin = Coin("Canada", "25 cents", 2001, "P", "")
        coin.coinId = database.coinDao().addCoin(coin)
        database.coinDao().deleteCoin(coin)

        // WHEN - Get coins from the database.
        val loaded = database.coinDao().getAllCoins()

        // THEN - The loaded data contains the expected values.
        assertThat(loaded, notNullValue())
        assertThat(loaded.size, `is`(0))
    }

    @Test
    fun updateCoin() = runBlockingTest {
        // GIVEN - Insert and update a coin.
        val coin = Coin("Canada", "25 cents", 2001, "P", "")
        coin.coinId = database.coinDao().addCoin(coin)
        coin.country = "USA"
        coin.denomination = "10 cents"
        coin.year = 2010
        coin.mintMark = ""
        coin.notes = "notes"
        coin.obverse = "obverse"
        coin.reverse = "reverse"
        database.coinDao().updateCoin(coin)

        // WHEN - Get coins from the database.
        val loaded = database.coinDao().getAllCoins()

        // THEN - The loaded data contains the expected values.
        assertThat(loaded, notNullValue())
        assertThat(loaded.size, `is`(1))
        assertThat(loaded[0].coinId, `is`(coin.coinId))
        assertThat(loaded[0].country, `is`(coin.country))
        assertThat(loaded[0].denomination, `is`(coin.denomination))
        assertThat(loaded[0].year, `is`(coin.year))
        assertThat(loaded[0].mintMark, `is`(coin.mintMark))
        assertThat(loaded[0].notes, `is`(coin.notes))
        assertThat(loaded[0].obverse, `is`(coin.obverse))
        assertThat(loaded[0].reverse, `is`(coin.reverse))
    }
}
