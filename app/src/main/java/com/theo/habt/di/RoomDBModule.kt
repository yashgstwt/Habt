package com.theo.habt.di

import android.content.Context
import androidx.room.Room
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.theo.habt.HabtApplication
import com.theo.habt.dataLayer.localDb.HabtDb
import com.theo.habt.dataLayer.localDb.MIGRATION_1_2
import com.theo.habt.dataLayer.repositorys.RoomDbRepo
import com.theo.habt.dataLayer.typeConverters.ColorConverter
import com.theo.habt.dataLayer.typeConverters.TimeConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {

    @Singleton
    @Provides
    @TypeConverters(ColorConverter::class , TimeConverter::class)
    fun provideRoomDb( @ApplicationContext application: Context):HabtDb {
        return Room.databaseBuilder(
            context = application,
            klass = HabtDb::class.java,
            name = "HabtDb"
        )
//            .fallbackToDestructiveMigration() // don't use this line in production app , this will erase all the data from existing database and creates new one
//            .addMigrations(MIGRATION_1_2) //use this for production app
            .build()

    }



    @Singleton
    @Provides
    fun provideRoomDbToRepo(room: HabtDb): RoomDbRepo {
        return RoomDbRepo(room)
    }


}