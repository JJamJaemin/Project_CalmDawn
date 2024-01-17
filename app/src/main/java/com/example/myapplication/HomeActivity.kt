package com.example.myapplication

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.db.DBLoader
import com.example.myapplication.model.Memo
import java.util.Calendar


//DBLoader 클래스의 인스턴스를 사용하여 데이터베이스 작업을 수행하며, 저장, 수정, 삭제
//코드는 주어진 레이아웃 파일과 메뉴 파일을 기반으로 메모 애플리케이션의 메모 작성, 편집, 저장, 삭제 기능을 구현
class HomeActivity : AppCompatActivity() {

    // layout/activity_memo.xml
    private lateinit var  edit_title: EditText
    private lateinit var  edit_memo: EditText
    private var item: Memo? = null
    private var date: String?= null

    // onCreate(savedInstanceState: Bundle?): 액티비티가 생성될 때 호출되는 메서드
    // 레이아웃을 설정하고 뒤로 가기 버튼을 활성화하며, 제목을 설정
    // edit_title과 edit_memo는 레이아웃의 EditText 위젯과 연결
    // 인텐트에서 전달된 데이터를 확인하여 기존 메모를 편집하는 경우 해당 메모의 내용을 EditText(edit_title,edit_memo)에 표시
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_memo)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //뒤로가기 버튼 구현
        supportActionBar!!.setTitle("memo") // 이름 수정 코드

        edit_memo = findViewById(R.id.edit_memo)
        edit_title = findViewById(R.id.edit_title)

        date = intent!!.getStringExtra("date")
        item = intent.getSerializableExtra("item") as Memo?
        if(item != null){

            edit_memo.setText(item?.memo)
            edit_title.setText(item?.title)
        }
    }

    //onCreateOptionsMenu(menu: Menu?): Boolean: 액션 바에 메뉴를 표시 하는 메서드
    // menu_memo.xml 파일을 인플레이션하여 메뉴를 생성하고, (if문 ->메모가 존재 하지않는 경우 삭제메뉴를 숨김
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_memo,menu)
        val deleteItem = menu!!.findItem(R.id.action_delete)
        if(this.item == null){
            deleteItem.isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    //onOptionsItemSelected(item: MenuItem): Boolean: 메뉴 아이템 선택 시 호출되는 메서드입니다. 선택된 메뉴 아이템에 따라 다양한 동작을 수행
    //android.R.id.home: 뒤로 가기 버튼을 선택한 경우 액티비티를 종료
    //R.id.action_save: 저장 버튼을 선택한 경우, 입력된 제목과 내용을 가져와서 비어 있지 않은 경우에만 DBLoader 클래스를 사용하여 메모를 저장 또는 수정
    // 이미 저장된 메모인 경우 수정하고, 새로운 메모인 경우 저장
    //R.id.action_delete: 삭제 버튼을 선택한 경우, 해당 메모를 DBLoader 클래스를 사용하여 삭제하고 액티비티를 종료
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            android.R.id.home->{
                finish()
            }//back

            R.id.action_save ->{
                val title = edit_title.text.toString()
                val memo = edit_memo.text.toString()
                if(!memo.equals("")) {
                    var calendar : Calendar? = null
                    if(date != null){
                        calendar = Calendar.getInstance()
                        val date = this.date!!.split("/")
                        calendar.set(date[0].toInt(), date[1].toInt(), date[2].toInt())
                    }
                    if(this.item != null){ //이미 저장되있는게 있다면
                        DBLoader(applicationContext).update(this.item!!.id, title, memo)
                        finish()
                    }else {
                        DBLoader(applicationContext).save(title, memo, calendar)
                        finish()
                    }
                }
            }//save

            R.id.action_delete ->{
                if(this.item != null){
                    DBLoader(applicationContext).delete(this.item!!.id)
                    finish() //메모 삭제 기능
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }




}
