package com.example.mylistapplication

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_layout.*
import java.util.*

class ListActivity : Activity() {

    val TAG = "ListActivity"
    var db : AppDatabase? = null
    var contactsList = mutableListOf<Contacts>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        //초기화
        db = AppDatabase.getInstance(this)

        //이전에 저장한 내용 모두 불러와서 추가하기
        val savedContacts = db!!.contactsDao().getAll()
        if(savedContacts.isNotEmpty()){
            contactsList.addAll(savedContacts)
        }

        //어댑터, 아이템 클릭 : 아이템 삭제
        val adapter = ContactsListAdapter(contactsList)
        adapter.setItemClickListener(object : ContactsListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {

                val contacts = contactsList[position]

                db?.contactsDao()?.delete(contacts = contacts) //DB에서 삭제
                contactsList.removeAt(position) //리스트에서 삭제
                adapter.notifyDataSetChanged() //리스트뷰 갱신

                Log.d(TAG, "remove item($position). name:${contacts.name}")
            }
        })
        mRecyclerView.adapter = adapter


        //플러스 버튼 클릭 : 데이터 추가
        mPlusButton.setOnClickListener {

            //랜덤 번호 만들기
            val random = Random()
            val numA = random.nextInt(1000)
            val numB = random.nextInt(10000)
            val numC = random.nextInt(10000)
            val rndNumber = String.format("%03d-%04d-%04d",numA,numB,numC)

            val contact = Contacts(0, "New $numA", rndNumber) //Contacts 생성
            db?.contactsDao()?.insertAll(contact) //DB에 추가
            contactsList.add(contact) //리스트 추가

            adapter.notifyDataSetChanged() //리스트뷰 갱신
        }

    }
}