package com.example.myapplication.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper(context: Context) : SQLiteOpenHelper(context, "MyDatabase.db", null, 1) {

    companion object {
        private const val TABLE_NOTE = "note"
        private const val TABLE_USER = "user"
        private const val TABLE_HOUR = "hour"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_MEMO = "memo"
        private const val COLUMN_DAYTIME = "daytime"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PHONE_NUMBER = "phone_number"
        private const val COLUMN_AGE = "age"
        private const val COLUMN_EMERGENCY_PHONE = "emergency_phone"
        private const val COLUMN_GENDER = "gender"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_HOUR = "user_hour"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // note 테이블 생성
        val createNoteTableQuery = """
            CREATE TABLE $TABLE_NOTE (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_MEMO TEXT,
                $COLUMN_DAYTIME TEXT
            )
        """.trimIndent()
        db.execSQL(createNoteTableQuery)

        // user 테이블 생성
        val createUserTableQuery = """
            CREATE TABLE $TABLE_USER (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_PHONE_NUMBER TEXT,
                $COLUMN_AGE INTEGER,
                $COLUMN_EMERGENCY_PHONE TEXT,
                $COLUMN_GENDER TEXT,
                $COLUMN_PASSWORD INTEGER
            )
        """.trimIndent()
        db.execSQL(createUserTableQuery)

        // user_hour 테이블 생성
        val createUserHourTableQuery = """
            CREATE TABLE $TABLE_HOUR (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PHONE_NUMBER TEXT,
                $COLUMN_HOUR INTEGER
            )
        """.trimIndent()
        db.execSQL(createUserHourTableQuery)

        // 초기 데이터 삽입 등의 작업 수행...
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 데이터베이스 업그레이드 시 필요한 작업 수행...
        val dropNoteTableQuery = "DROP TABLE IF EXISTS $TABLE_USER"
        db.execSQL(dropNoteTableQuery)

        val dropUserTableQuery = "DROP TABLE IF EXISTS $TABLE_USER"
        db.execSQL(dropUserTableQuery)

        onCreate(db)
    }

    fun insertNote(title: String, memo: String, daytime: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_MEMO, memo)
            put(COLUMN_DAYTIME, daytime)
        }
        val db = writableDatabase
        val id = db.insert(TABLE_NOTE, null, values)
        db.close()
        return id
    }

    fun insertUser(
        name: String,
        phoneNumber: String,
        age: Int,
        emergencyPhone: String,
        gender: String,
        password: Int
    ): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_PHONE_NUMBER, phoneNumber)
            put(COLUMN_AGE, age)
            put(COLUMN_EMERGENCY_PHONE, emergencyPhone)
            put(COLUMN_GENDER, gender)
            put(COLUMN_PASSWORD, password)
        }
        val db = writableDatabase
        val id = db.insert(TABLE_USER, null, values)
        db.close()
        return id
    }

    fun isPhoneNumberExists(phoneNumber: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USER WHERE $COLUMN_PHONE_NUMBER = ?"
        val cursor = db.rawQuery(query, arrayOf(phoneNumber))
        val phoneNumberExists = cursor.count > 0
        cursor.close()
        db.close()
        return phoneNumberExists
    }

    fun login(phoneNumber: String, password: Int): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USER WHERE $COLUMN_PHONE_NUMBER = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(phoneNumber, password.toString()))
        val loginSuccessful = cursor.count > 0
        cursor.close()
        db.close()
        return loginSuccessful
    }
    fun getUserDataByPhoneNumber(phoneNumber: String): UserData? {
        val db = readableDatabase

        val projection = arrayOf(COLUMN_NAME, COLUMN_PHONE_NUMBER, COLUMN_AGE, COLUMN_EMERGENCY_PHONE, COLUMN_GENDER, COLUMN_PASSWORD)

        val selection = "$COLUMN_PHONE_NUMBER = ?"
        val selectionArgs = arrayOf(phoneNumber)

        val cursor = db.query(
            TABLE_USER,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var userData: UserData? = null

        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER))
            val age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE))
            val emergencyPhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMERGENCY_PHONE))
            val gender = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER))
            val password = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))

            // 가져온 데이터를 UserData 객체로 생성
            userData = UserData(name, phoneNumber, age, emergencyPhoneNumber, gender, password)
        }

        cursor.close()
        db.close()

        return userData
    }
    fun getUserHourByPhoneNumber(phoneNumber: String): Int? {
        val db = readableDatabase

        val projection = arrayOf(COLUMN_HOUR)

        val selection = "$COLUMN_PHONE_NUMBER = ?"
        val selectionArgs = arrayOf(phoneNumber)

        val cursor = db.query(
            TABLE_HOUR,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var userHour: Int? = null

        if (cursor.moveToFirst()) {
            userHour = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HOUR))
        }

        cursor.close()
        db.close()

        return userHour
    }




    data class UserData(
        val name: String,
        val phoneNumber: String,
        val age: Int,
        val emergencyPhoneNumber: String,
        val gender: String,
        val password: Int
    )



}
