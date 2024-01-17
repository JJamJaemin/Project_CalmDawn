package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HomeActivity
import com.example.myapplication.R
import com.example.myapplication.model.Memo
import java.text.SimpleDateFormat
import java.util.Date

//MemoAdapter는 RecyclerView와 함께 사용되어 Memo 객체의 데이터를 목록 형태로 표시하고,
// 각 아이템을 클릭하면 해당 메모를 편집할 수 있는 HomeActivity를 시작
class MemoAdapter(context: Context) :RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val context = context
    private val arrayList = ArrayList<Memo>()

    //onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder 객체를 생성하는 메서드
    // item_memo.xml 레이아웃 파일을 인플레이션하여 ViewHolder를 생성하고 반환
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_memo, parent, false)
        return HolderView(view)
    }

    //getItemCount(): Int: RecyclerView에 표시할 아이템의 개수를 반환합니다. ArrayList의 크기를 반환
    override fun getItemCount(): Int {
        return  arrayList.size
    }

    //onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int): ViewHolder와 데이터를 바인딩하는 메서드
    // 해당 위치의 Memo 객체를 가져와서 ViewHolder의 뷰에 데이터를 설정
    // 날짜 및 시간은 SimpleDateFormat을 사용하여 형식을 변경한 후 TextView에 표시
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = arrayList.get(position)
        val view = holder as HolderView
        view.text_title.setText(item.title)
        view.text_memo.setText(item.memo)
        val date = Date()
        date.time = item.datetime.toLong()
        view.text_datetime.setText(SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date))

        //onClickListener: RecyclerView의 각 아이템을 클릭할 때 발생하는 클릭 이벤트
        //클릭된 아이템의 Memo 객체를 인텐트에 추가하여 HomeActivity로 전달하고, HomeActivity를 시작
        view.itemView.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("item", item)
            context.startActivity(intent)
        }
    }

    //setList(array: ArrayList<Memo>): MemoAdapter에 표시할 데이터 목록을 설정하는 메서드입니다.
    // 기존의 arrayList를 지우고, 새로운 데이터로 대체한 후 RecyclerView를 갱신합니다.
    fun setList(array : ArrayList<Memo>){
        arrayList.clear()
        arrayList.addAll(array)
        notifyDataSetChanged()
    }
    //HolderView(view: View): ViewHolder 클래스입니다.
    // item_memo.xml 레이아웃 파일에서 TextView들을 찾아서 참조 변수에 연결합니다
    private class HolderView(view : View) : RecyclerView.ViewHolder(view) {
        // layout/item_memo.xml
        val text_title = view.findViewById<TextView>(R.id.text_title)
        val text_memo = view.findViewById<TextView>(R.id.text_memo)
        val text_datetime = view.findViewById<TextView>(R.id.text_datetime)

    }
}