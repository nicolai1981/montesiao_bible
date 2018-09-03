package br.com.nicolaiito.montesioapp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import br.com.nicolaiito.montesioapp.R
import br.com.nicolaiito.montesioapp.model.BibleModel
import kotlinx.android.synthetic.main.book_item.view.*

class BookListAdapter(val context : Context,
                      val dataList : List<BibleModel.Book>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view : View;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false)
            view.tag = MyViewHolder(view)

        } else {
            view = convertView
        }

        var item : BibleModel.Book = dataList[position]
        var holder = view.tag as MyViewHolder
        holder.acroView.text = item.acro
        holder.nameView.text = item.name
        holder.sizeView.text = item.size.toString()
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

    class MyViewHolder(val view : View) {
        var acroView : TextView
        var nameView : TextView
        var sizeView : TextView

        init {
            acroView = view.acro_book_item_text
            nameView = view.name_book_item_text
            sizeView = view.size_book_item_text
        }
    }
}