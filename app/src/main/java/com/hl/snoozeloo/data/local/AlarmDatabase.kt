package com.hl.snoozeloo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
TODO: implement DI using either Koin or kotlin-inject
TODO: implement Migration
 */
@Database(
    entities = [AlarmEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AlarmDatabase: RoomDatabase() {

    // Declare an Abstract Function AlarmDao so that database knows about the DAO
    abstract fun alarmDao(): AlarmDao

    companion object {

        /*
         Instance variable keeps a reference to the bases, when one has been created.
         This helps maintain a single instance of the database opened at a given time.
         Annotate Instance with @Volatile - this ensures no caching and that all reads
         and writes are to and from the main memory. This helps ensure that Instance
         is always up to date and is the same for all execution threads, immediately
         visible to all other threads.
         */
        @Volatile
        private var Instance: AlarmDatabase? = null

        fun getDatabase(context: Context): AlarmDatabase {
            /*
            Wrap get database code inside a synchronized block to ensure only
            one database instance is requested to ensure it is only initialized once.
             */
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    klass = AlarmDatabase::class.java,
                    name = "alarm_database"
                ).fallbackToDestructiveMigration(true)
                    .build()
                    // keeps a reference to the recently created DB isntance.
                    .also { Instance = it }
            }
        }
    }

}