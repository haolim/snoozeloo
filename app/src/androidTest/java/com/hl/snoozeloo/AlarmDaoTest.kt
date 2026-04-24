package com.hl.snoozeloo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hl.snoozeloo.data.local.AlarmDao
import com.hl.snoozeloo.data.local.AlarmDatabase
import com.hl.snoozeloo.data.local.AlarmEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AlarmDaoTest {

    private lateinit var alarmDao: AlarmDao
    private lateinit var alarmDatabase: AlarmDatabase

    @Before
    fun createDB() {
        val context: Context = ApplicationProvider.getApplicationContext()
        alarmDatabase = Room.inMemoryDatabaseBuilder(
            context,
            AlarmDatabase::class.java
        ).allowMainThreadQueries().build()
        alarmDao = alarmDatabase.alarmDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        alarmDatabase.close()
    }

    private var alarm1 = AlarmEntity(
        id = 1,
        hour = 13,
        minute = 30,
        alarmTitle = "Work",
        isEnabled = true
    )
    private var alarm2 = AlarmEntity(
        id = 2,
        hour = 10,
        minute = 0,
        alarmTitle = "Wake Up",
        isEnabled = true
    )

    private suspend fun addOneAlarmToDb() {
        alarmDao.addAlarm(alarm1)
    }
    private suspend fun addTwoAlarmToDb() {
        alarmDao.addAlarm(alarm1)
        alarmDao.addAlarm(alarm2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsAlarmIntoDB() = runBlocking {
        addOneAlarmToDb()
        val allAlarms = alarmDao.getAllAlarms().first()
        Assert.assertEquals(allAlarms[0], alarm1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDB() = runBlocking {
        addTwoAlarmToDb()
        val allAlarms = alarmDao.getAllAlarms().first()
        Assert.assertEquals(allAlarms[0], alarm1)
        Assert.assertEquals(allAlarms[1], alarm2)
    }
}