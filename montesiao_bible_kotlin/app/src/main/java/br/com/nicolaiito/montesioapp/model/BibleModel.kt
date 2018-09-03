package br.com.nicolaiito.montesioapp.model

import android.content.Context
import android.util.Log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.BufferedReader
import java.io.InputStreamReader

class BibleModel {
    class Book(var id: Int, var name: String, var acro: String, var size: Int)
    class Verse(var number: Int, var text: String, var start: Int = 0, var end: Int = 0)

    fun getChapter(book: Int, chapter: Int, context: Context, cb : GetChapterCallBack) {
        doAsync {
            var verseList : MutableList<Verse> = mutableListOf()
            var verseNumber = 1

            val resId = context.getResources()
                               .getIdentifier("v" + book + "_" + chapter, "raw",
                                              context.getPackageName())
            val raw = context.resources.openRawResource(resId)
            if (raw != null) {
                val inputStream = BufferedReader(InputStreamReader(raw, "UTF8"))
                var verseText: String? = inputStream.readLine()
                while (verseText != null) {
                    var terms = verseText.split("@")
                    if (terms.size == 1) {
                        verseList.add(Verse(verseNumber++, verseText))
                    } else {
                        verseList.add(Verse(-1, terms[1], terms[0].toInt()))
                    }
                    verseText = inputStream.readLine()
                }
                verseList.add(Verse(-2, ""))
            }
            uiThread {
                cb.onFinished(verseList)
            }
        }
    }

    interface GetChapterCallBack {
        fun onFinished(list : List<Verse>)
    }
}