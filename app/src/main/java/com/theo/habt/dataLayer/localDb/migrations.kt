package com.theo.habt.dataLayer.localDb

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

//This object will hold the instructions to go from version 1 to 2.
    val MIGRATION_1_2 = object : Migration(startVersion = 1, endVersion = 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // This is the SQL command to add the new column to the existing 'habits' table.
            // INTEGER is the SQLite type for Long.
            // NOT NULL means it can't be empty.
            // DEFAULT 1L is crucial: it gives a default value to all the existing rows.
            db.execSQL(
                """
            DELETE FROM habits
            WHERE id NOT IN (
                SELECT MIN(id)
                FROM habits
                GROUP BY name
            )
        """
            )
            db.execSQL("CREATE UNIQUE INDEX index_habits_name ON habits (name)")
        }
    }


val MIGRATION_2_3 = object : Migration(startVersion = 2, endVersion = 3){

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
                CREATE TABLE habits_new (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT NOT NULL,
                    color_argb TEXT NOT NULL,
                    icon_name INTEGER NOT NULL,
                    creation_date TEXT NOT NULL,
                    notification_time TEXT
                )
            """.trimIndent()
        )
    }
}
