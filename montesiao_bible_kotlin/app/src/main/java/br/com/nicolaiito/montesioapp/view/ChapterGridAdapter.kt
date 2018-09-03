package br.com.nicolaiito.montesioapp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import br.com.nicolaiito.montesioapp.R
import kotlinx.android.synthetic.main.chapter_grid_item.view.*

class ChapterGridAdapter(val context: Context, private var totalChapter : Int) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.chapter_grid_item, parent, false)
            view.tag = MyViewHolder(view)
        } else {
            view = convertView
        }

        var holder: MyViewHolder = view.tag as MyViewHolder
        holder.chapterView.text = (position + 1).toString()
        return view
    }

    override fun getItem(position: Int): Any {
        return position + 1
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return totalChapter
    }

    fun setDataSize(newSize : Int) {
        totalChapter = newSize
    }

    class MyViewHolder(val view: View) {
        var chapterView : TextView

        init {
            chapterView = view.item_chapter
        }
    }
}