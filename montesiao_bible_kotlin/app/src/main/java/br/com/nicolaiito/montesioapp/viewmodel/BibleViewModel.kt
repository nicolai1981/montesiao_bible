package br.com.nicolaiito.montesioapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.res.Resources
import br.com.nicolaiito.montesioapp.model.BibleModel.Book
import br.com.nicolaiito.montesioapp.model.BibleModel.Verse
import br.com.nicolaiito.montesioapp.R
import br.com.nicolaiito.montesioapp.model.BibleModel

class BibleViewModel() : ViewModel() {
    private val mCurrentChapter : MutableLiveData<List<Verse>> = MutableLiveData()
    private val mBookList: MutableList<Book> = mutableListOf()
    private val mBibleModel = BibleModel()

    private val mGetChapterCB = object : BibleModel.GetChapterCallBack {
        override fun onFinished(list : List<Verse>) {
            mCurrentChapter.value = list
        }
    }

    init{
        mCurrentChapter.value = listOf()
    }

    fun getCurrentChapter(): MutableLiveData<List<Verse>> {
        return mCurrentChapter
    }

    fun updateCurrentChapter(book: Int, chapter: Int, context: Context) {
        mBibleModel.getChapter(book, chapter, context, mGetChapterCB)
    }

    fun getBook(res: Resources): MutableList<Book> {
        if (mBookList.size == 0) {
            var names: Array<String> = res.getStringArray(R.array.books_name)
            var acros: Array<String> = res.getStringArray(R.array.books_acro)
            var sizes: IntArray = res.getIntArray(R.array.books_size)
            for (i in 0.. names.size-1) {
                var item = Book(i, names[i], acros[i], sizes[i])
                mBookList.add(item)
            }
        }
        return mBookList
    }
}