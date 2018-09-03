package br.com.nicolaiito.montesioapp.view

import android.content.Context
import android.text.Html
import android.text.Html.fromHtml
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import br.com.nicolaiito.montesioapp.R
import br.com.nicolaiito.montesioapp.model.BibleModel
import kotlinx.android.synthetic.main.verse_item.view.*

class VerseListAdapter(val context : Context,
                       val dataList : MutableList<BibleModel.Verse>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view : View;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.verse_item, parent, false)
            view.tag = MyViewHolder(view)

        } else {
            view = convertView
        }

        var item : BibleModel.Verse = dataList[position]
        var holder = view.tag as MyViewHolder
        if (item.number == -1) {
            holder.titleView.text = item.text
            holder.titleView.visibility = View.VISIBLE

            holder.verseView.text = ""
            holder.verseView.visibility = View.GONE

            holder.emptyView.visibility = View.GONE

        } else if (item.number == -2) {
            holder.titleView.visibility = View.GONE
            holder.verseView.visibility = View.GONE
            holder.emptyView.visibility = View.VISIBLE

        } else {
            holder.titleView.text = ""
            holder.titleView.visibility = View.GONE

            holder.verseView.text = Html.fromHtml("<b>${item.number}</b> ${item.text}")
            holder.verseView.visibility = View.VISIBLE

            holder.emptyView.visibility = View.GONE
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return dataList.size
    }

    fun setData(list : List<BibleModel.Verse>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    class MyViewHolder(val view : View) {
        var titleView : TextView
        var verseView : TextView
        var emptyView : TextView

        init {
            titleView = view.title_text
            verseView = view.verse_text
            emptyView = view.empty_text
        }
    }
}