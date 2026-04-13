package com.hl.snoozeloo.data

import android.content.Context
import com.hl.snoozeloo.data.local.AlarmDatabase
import com.hl.snoozeloo.data.local.AlarmRepository
import com.hl.snoozeloo.data.local.OfflineAlarmRepository


/*
    Manual DI: We pass the repository into the ViewModel constructor. Loose coupling.
    Service Locator: The ViewModel reaches out to our AppContainer to grab what it needs. Tight coupling.

    Primary purpose of AppContainer is to instantiate long-lived data-related classes (e.g.
    Repositories, Databases, Network Clients) to ensure they are singletons
    across the app and to provide a data source to our VieWModel.
    Whether it will act as a Service Locator (Take) or DI (Give) depends on how our
    ViewModel gets the data.
    E.g. in our VM if we have code like
        // Service Locator pattern - VM reaches out to grab a repository
        val repo = AppContainer.alarmsRepository
        // DI pattern - VM is 'blind'. It simply asks for a repository in its contructor
        class MyViewModel(val repo: alarmsRepository)
    Other purpose includes Navigators or Analytics.
    Act as the Data Layer Entry Point.
    Inversion of Control: Allows easy testing using FakeAlarmRepository() for example.

 */

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val alarmRepository: AlarmRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineAlarmsRepository]
 */

class AppDataContainer(private val context: Context) : AppContainer {
    override val alarmRepository: AlarmRepository by lazy {
        OfflineAlarmRepository(AlarmDatabase.getDatabase(context).alarmDao()
        )
    }
}