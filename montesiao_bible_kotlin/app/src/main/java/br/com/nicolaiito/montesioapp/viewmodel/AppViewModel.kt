package br.com.nicolaiito.montesioapp.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import br.com.nicolaiito.montesioapp.model.AppModel

class AppViewModel() : ViewModel() {
    var appModel : AppModel = AppModel()

    fun getReadState(context : Context) : MutableLiveData<AppModel.ReadState> {
        return appModel.getReadState(context)
    }

    fun setReadState(context : Context, state : AppModel.ReadState) {
        appModel.setReadState(context, state)
    }
}