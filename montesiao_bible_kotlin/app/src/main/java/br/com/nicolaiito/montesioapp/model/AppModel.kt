package br.com.nicolaiito.montesioapp.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.preference.Preference
import android.preference.PreferenceManager

class AppModel {
    val PREF_BOOK = "CUR_BOOK"
    val PREF_CHAPTER = "CUR_CHAPTER"
    var mReadState : MutableLiveData<ReadState> = MutableLiveData()

    class ReadState(var book : Int = -1,
                    var chapter : Int = -1)

    fun getReadState(context : Context) : MutableLiveData<ReadState> {
        if (mReadState.value == null) {
            var state = ReadState()
            var pref = PreferenceManager.getDefaultSharedPreferences(context)
            state.book = pref.getInt(PREF_BOOK, -1)
            state.chapter = pref.getInt(PREF_CHAPTER, -1)
            mReadState.value = state
        }

        return mReadState
    }

    fun setReadState(context : Context, newState : ReadState) {
        var pref = PreferenceManager.getDefaultSharedPreferences(context)
        var edit = pref.edit()
        edit.putInt(PREF_BOOK, newState.book)
        edit.putInt(PREF_CHAPTER, newState.chapter)
        edit.commit()

        mReadState.value = newState
    }
}