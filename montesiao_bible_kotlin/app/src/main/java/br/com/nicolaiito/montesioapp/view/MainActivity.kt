package br.com.nicolaiito.montesioapp.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import br.com.nicolaiito.montesioapp.R
import br.com.nicolaiito.montesioapp.model.AppModel
import br.com.nicolaiito.montesioapp.viewmodel.AppViewModel
import br.com.nicolaiito.montesioapp.viewmodel.BibleViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mAdView : AdView

    private var mChapterGridAdapter : ChapterGridAdapter? = null
    private var mVerseAdapter : VerseListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val appVM = ViewModelProviders.of(this).get(AppViewModel::class.java)
        val bibleVM = ViewModelProviders.of(this).get(BibleViewModel::class.java)

        bookListView.adapter = BookListAdapter(applicationContext, bibleVM.getBook(resources))
        bookListView.onItemClickListener = AdapterView.OnItemClickListener {
            adapterView, view, position, id ->
                appVM.setReadState(applicationContext, AppModel.ReadState(position))
        }

        mChapterGridAdapter = ChapterGridAdapter(applicationContext, 0)
        chapterGridView.adapter = mChapterGridAdapter
        chapterGridView.onItemClickListener = AdapterView.OnItemClickListener() {
            adapterView, view, position, id ->
                var state = appVM.getReadState(applicationContext).value
                appVM.setReadState(applicationContext,
                        AppModel.ReadState(state?.book ?: 0, position))
        }

        mVerseAdapter = VerseListAdapter(applicationContext, mutableListOf())
        verseListView.adapter = mVerseAdapter

        appVM.getReadState(applicationContext)
             .observe(this, Observer<AppModel.ReadState> {
                 var bookList = bibleVM.getBook(resources)

                 if (it?.book == -1) {
                     this@MainActivity.actbar_book.visibility = View.GONE
                     this@MainActivity.actbar_chapter.visibility = View.GONE
                     this@MainActivity.actbar_msg.text = getString(R.string.actbar_select_book)
                     this@MainActivity.actbar_msg.visibility = View.VISIBLE

                     this@MainActivity.bookListView.visibility = View.VISIBLE
                     this@MainActivity.chapterGridView.visibility = View.GONE
                     this@MainActivity.verseListView.visibility = View.GONE

                     this@MainActivity.nextButton.visibility = View.GONE
                     this@MainActivity.prevButton.visibility = View.GONE

                 } else if (it?.chapter == -1) {
                     this@MainActivity.actbar_book.text = bookList[it?.book ?: 0].acro
                     this@MainActivity.actbar_book.visibility = View.VISIBLE
                     this@MainActivity.actbar_chapter.visibility = View.GONE
                     this@MainActivity.actbar_msg.text = getString(R.string.actbar_select_chapter)
                     this@MainActivity.actbar_msg.visibility = View.VISIBLE

                     this@MainActivity.bookListView.visibility = View.GONE
                     mChapterGridAdapter?.setDataSize(bookList[it?.book ?: 0].size)
                     this@MainActivity.chapterGridView.visibility = View.VISIBLE
                     this@MainActivity.verseListView.visibility = View.GONE

                     this@MainActivity.nextButton.visibility = View.GONE
                     this@MainActivity.prevButton.visibility = View.GONE

                 } else {
                     this@MainActivity.actbar_book.text = bookList[it?.book ?: 0].acro
                     this@MainActivity.actbar_book.visibility = View.VISIBLE
                     this@MainActivity.actbar_chapter.text = ((it?.chapter ?: 0) + 1).toString()
                     this@MainActivity.actbar_chapter.visibility = View.VISIBLE
                     this@MainActivity.actbar_msg.visibility = View.GONE

                     this@MainActivity.bookListView.visibility = View.GONE
                     this@MainActivity.chapterGridView.visibility = View.GONE
                     this@MainActivity.verseListView.visibility = View.VISIBLE

                     bibleVM.updateCurrentChapter(it?.book ?: 0, it?.chapter ?: 0, applicationContext)

                     if (it?.book == 0 && it?.chapter == 0) {
                         this@MainActivity.prevButton.visibility = View.GONE
                     } else {
                         this@MainActivity.prevButton.visibility = View.VISIBLE
                     }

                     var bookList = bibleVM.getBook(resources)
                     if (it?.book == bookList.size - 1 && it?.chapter == bookList[bookList.size - 1].size - 1) {
                         this@MainActivity.nextButton.visibility = View.GONE
                     } else {
                         this@MainActivity.nextButton.visibility = View.VISIBLE
                     }
                 }
             })

        bibleVM.getCurrentChapter().observe(this, Observer {
            mVerseAdapter?.setData(bibleVM.getCurrentChapter().value ?: mutableListOf())
            verseListView.setSelection(0)
        })

        actbar_book.setOnClickListener{
            appVM.setReadState(applicationContext, AppModel.ReadState())
        }

        actbar_chapter.setOnClickListener{
            var status = appVM.getReadState(applicationContext).value
            appVM.setReadState(applicationContext, AppModel.ReadState(status?.book ?: 0))
        }

        nextButton.setOnClickListener{
            var nextState = AppModel.ReadState()
            var status = appVM.getReadState(applicationContext).value

            if (status == null) {
                nextState.book = 0
                nextState.chapter = 0

            } else  if (status.chapter + 1 == bibleVM.getBook(resources)[status.book].size) {
                nextState.book = status.book + 1
                nextState.chapter = 0
            } else {
                nextState.book = status.book
                nextState.chapter = status.chapter + 1
            }

            appVM.setReadState(applicationContext, nextState)
        }

        prevButton.setOnClickListener{
            var nextState = AppModel.ReadState()
            var status = appVM.getReadState(applicationContext).value

            if (status == null) {
                nextState.book = 0
                nextState.chapter = 0

            } else  if (status.chapter == 0) {
                nextState.book = status.book - 1
                nextState.chapter = bibleVM.getBook(resources)[nextState.book].size - 1
            } else {
                nextState.book = status.book
                nextState.chapter = status.chapter - 1
            }

            appVM.setReadState(applicationContext, nextState)
        }
    }
}
