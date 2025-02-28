package com.example.composeapp

import android.app.Application
import kotlinx.coroutines.delay

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.core.context.startKoin
data class User(
    val id: String,
    val name: String,
    val email: String
)

class UserRepository(private val dataSource: UserDataSource) {
    suspend fun getUsers(): List<User> {
        return dataSource.fetchUsers()
    }
}

class UserDataSource {
    suspend fun fetchUsers(): List<User> {
        delay(1000)
        return listOf(
            User("1", "Nguyễn Văn A", "nguyenvana@example.com"),
            User("2", "Trần Thị B", "tranthib@example.com"),
            User("3", "Lê Văn C", "levanc@example.com")
        )
    }

    suspend fun fetchUserById(id: String): User? {
        delay(500)
        return when (id) {
            "1" -> User("1", "Nguyễn Văn A", "nguyenvana@example.com")
            "2" -> User("2", "Trần Thị B", "tranthib@example.com")
            "3" -> User("3", "Lê Văn C", "levanc@example.com")
            else -> null
        }
    }
}

val appModule = module {
    // Repository
    single { UserDataSource() }
    single { UserRepository(get()) }
    viewModel { MainViewModel(get()) }
}

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}