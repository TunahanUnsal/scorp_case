package com.scorp.case1.view.activity

import PaginationScrollListener
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scorp.case1.model.Person
import com.scorp.case1.viewModel.Controller
import com.scorp.case1.viewModel.ListUpdater
import com.scorp.case1.view.adapter.PersonAdapter
import com.scorp.case1.databinding.ActivityMainBinding
import com.scorp.case1.viewModel.OnSwipeTouchListener



class MainActivity : AppCompatActivity(), ListUpdater {

    private lateinit var binding: ActivityMainBinding  //view binding
    private var TAG: String = MainActivity::class.simpleName.toString()
    private lateinit var tempNext: String
    private lateinit var tempOld: String
    private var people: MutableList<Person> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Controller.executeFetch(null)

        listeners()

        tempNext = null.toString()
        tempOld = null.toString()


    }

    @SuppressLint("ClickableViewAccessibility")
    private fun listeners() {

        Controller.registerListUpdater(this)


        binding.refreshButton.setOnClickListener {

            refresh()

        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.personList.layoutManager=layoutManager

        var isLastPage: Boolean = false
        var isLoading: Boolean = false

        binding.personList.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                Log.d(TAG, "isLastPage")
                return isLastPage
            }

            override fun isLoading(): Boolean {
                Log.d(TAG, "isLoading")
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                Log.d(TAG, "loadMoreItems")
            }
        })


    }

    private fun recyclerViewAdapter(list: List<Person>) {  //list create or update


        var size = people.size
        people.addAll(list)

        binding.personList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.personList.adapter = PersonAdapter(people)

        binding.progressBar.visibility = View.INVISIBLE

        var sizeNew = people.size



    }

    override fun listUpdate(list: List<Person>, next: String,error: String) { //list update interface

        Log.d(TAG, "listUpdate next -> $next")
        Log.d(TAG, "listUpdate error -> $error")

        val errorCode : Int = Controller.errorCodeWizard(error,tempOld)

        Log.d(TAG, "listUpdate error code -> $errorCode")

        if (errorCode == 0){

            if(next == "null"&& list.isNotEmpty()){

                binding.refreshButton.visibility = View.VISIBLE
                recyclerViewAdapter(list)
            }
            else if (next == "null" && list.isEmpty()){

                binding.emptyText.visibility = View.VISIBLE
                binding.refreshButton.visibility = View.VISIBLE

                recyclerViewAdapter(list)
            }

            else {

                Log.d(TAG, "listUpdate next -> !null ")
                binding.emptyText.visibility = View.INVISIBLE
                binding.refreshButton.visibility = View.INVISIBLE
                tempNext = next
                recyclerViewAdapter(list)

            }


        }

    }
    fun scrollDown(){

        binding.progressBar.visibility = View.VISIBLE
        Controller.executeFetch(tempNext)
        tempOld = tempNext
        binding.progressBar.visibility = View.VISIBLE

    }
    fun scrollUp(){

        binding.progressBar.visibility = View.VISIBLE
        Controller.executeFetch(null)
        tempNext = null.toString()
        tempOld = null.toString()

    }
    fun refresh(){

        Controller.executeFetch(null)
        binding.progressBar.visibility = View.VISIBLE
        binding.refreshButton.visibility = View.INVISIBLE
    }




}
