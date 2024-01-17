package com.example.myapplication.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.widget.Toast
import com.example.myapplication.model.Memo
import java.util.Calendar

class DBLoader(context: Context) {

    private val context = context
    private var db :DBHelper

    init {
        db = DBHelper(context);
    }

    // save(title:String, memo: String, getCalendar: Calendar?): 메모를 저장하는 함수
    // 제목(title), 내용(memo), 선택적으로 전달되는 날짜 및 시간(getCalendar)을 받아와서 SQLite 데이터베이스에 저장
    // 만약 getCalendar가 null인 경우에는 현재 시간을 사용
    fun save(title:String,memo: String, getCalendar: Calendar?){
        val calendar : Calendar
        if(getCalendar == null){
            calendar = Calendar.getInstance() //현재시간 넣기
        }else{
            calendar = getCalendar //선택한 날짜 시간 넣기
        }
        val contentValues = ContentValues()
        contentValues.put("title", title)
        contentValues.put("memo", memo)
        contentValues.put("daytime", calendar.timeInMillis)

        db.writableDatabase.insert("note", null, contentValues)
        db.close()
        Toast.makeText(context, "저장됨", Toast.LENGTH_SHORT).show()
    }

    // delete(id: Int): 지정된 ID의 메모를 삭제하는 함수
    // id를 받아와서 해당 ID를 가진 메모를 SQLite 데이터베이스에서 삭제
    fun delete(id: Int){
        db.writableDatabase.delete("note","id=?", arrayOf(id.toString()))
        db.close()
        Toast.makeText(context, "삭제됨", Toast.LENGTH_SHORT).show()
    }

    //update(id: Int, title:String, memo: String): 지정된 ID의 메모를 수정하는 함수
    // id, 새로운 제목(title), 새로운 내용(memo)을 받아와서 해당 ID를 가진 메모를 SQLite 데이터베이스에서 수정
    fun update(id: Int, title:String, memo: String){
        val contentValues = ContentValues()
        contentValues.put("title", title)
        contentValues.put("memo", memo)
        db.writableDatabase.update("note", contentValues, "id=?", arrayOf(id.toString()))
        db.close()
    }

    // memoList(datetime: Long?): 메모 목록을 조회하는 함수
    // 선택적으로 날짜 및 시간(datetime)을 받아와서 해당 날짜 및 시간에 대한 메모 목록을
    // SQLite 데이터베이스에서 조회, 조회된 결과를 Memo 모델 객체의 리스트로 반환
    @SuppressLint("Range")
    fun memoList(datetime: Long?): ArrayList<Memo> {
        val array = ArrayList<Memo>() // model/Memo.kt
        var sql = ""
        if(datetime == null){
            sql = "select * from note order by daytime desc"
        }else{
            sql = "select * from note where daytime like '%" + datetime + "%' order by daytime desc"
        }
        val cursor = db.readableDatabase.rawQuery(sql, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val title = cursor.getString(cursor.getColumnIndex("title"))
            val memo = cursor.getString(cursor.getColumnIndex("memo"))
            val getDatetime = cursor.getLong(cursor.getColumnIndex("daytime"))

            val memoItem = Memo(id, title, memo, getDatetime)
            array.add(memoItem)
        }
        return array
    }
}