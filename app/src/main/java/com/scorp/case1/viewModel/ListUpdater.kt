package com.scorp.case1.viewModel

import com.scorp.case1.model.FetchError
import com.scorp.case1.model.Person

interface ListUpdater {

    fun listUpdate(list: List<Person>, next: String, error : String)  //for list update

}