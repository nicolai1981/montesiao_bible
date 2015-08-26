package itocorp.ibms.bibliamontesiao.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itocorp.ibms.bibliamontesiao.R;
import itocorp.ibms.bibliamontesiao.model.Versiculo;
import itocorp.ibms.bibliamontesiao.model.Version;
import itocorp.ibms.bibliamontesiao.view.adapter.VersiculoAdapter;

public class FragmentBible extends Fragment implements AbsListView.OnScrollListener {
    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.subtitle)
    TextView mSubTitle;

    @Bind(R.id.spinner_book)
    Spinner mSpinnerBook;
    private ArrayAdapter<String> mBookAdapter;

    @Bind(R.id.spinner_chapter)
    Spinner mSpinnerChapter;
    private ArrayAdapter<String> mChapterAdapter;

    @Bind(R.id.text_list)
    ListView mVersiculoListView;
    private VersiculoAdapter mVersiculoAdapter;

    private List<Versiculo> mTitleList;
    private int mBookIndex = 0;
    private boolean mIsBack = false;

    public static FragmentBible newInstance() {
        return new FragmentBible();
    }

    public FragmentBible() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bible, container, false);
        ButterKnife.bind(this, view);

        mVersiculoAdapter = new VersiculoAdapter(getActivity().getApplicationContext());
        mVersiculoListView.setAdapter(mVersiculoAdapter);
        mVersiculoListView.setOnScrollListener(this);

        mBookAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.item_livro,
                getResources().getStringArray(R.array.livros_list));
        mSpinnerBook.setAdapter(mBookAdapter);
        mSpinnerBook.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBookIndex = position;
                int[] bookChapterList = getResources().getIntArray(R.array.chapter_list);
                String[] chapterList = new String[bookChapterList[position]];
                for (int i=0; i<bookChapterList[position]; i++) {
                    chapterList[i] = "Cap. " + (i+1);
                }
                mChapterAdapter.clear();
                mChapterAdapter.addAll(chapterList);
                mChapterAdapter.notifyDataSetChanged();
                if (mIsBack) {
                    mSpinnerChapter.setSelection(mChapterAdapter.getCount() - 1);
                } else {
                    mSpinnerChapter.setSelection(0);
                }
                mIsBack = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mChapterAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.item_livro, new ArrayList<String>());
        mSpinnerChapter.setAdapter(mChapterAdapter);
        mSpinnerChapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] bookList = getResources().getStringArray(R.array.books_list);
                mVersiculoAdapter.clear();
                mVersiculoAdapter.addAll(Versiculo.getChapter(position+1, bookList[mBookIndex], Version.getDefault(), getActivity().getApplicationContext()));
                mVersiculoAdapter.notifyDataSetChanged();
                mVersiculoListView.smoothScrollToPosition(0);
                mTitleList = Versiculo.getTitleFromChapter(position+1, bookList[mBookIndex], Version.getDefault());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mSpinnerChapter.setSelection(0);

        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mVersiculoAdapter.getCount() > firstVisibleItem) {
            Versiculo item = mVersiculoAdapter.getItem(firstVisibleItem);
            Versiculo title = null;
            int lastVersiculo = mVersiculoAdapter.getCount() - mTitleList.size();
            for (int i=0; i < mTitleList.size(); i++) {
                Versiculo versiculo = mTitleList.get(i);
                if (versiculo.mNumber > item.mNumber) {
                    if (title == null) {
                        title = versiculo;
                    }
                    if (mTitleList.size() > i) {
                        lastVersiculo = mTitleList.get(i).mNumber - 1;
                    }
                    break;
                }
                title = versiculo;
            }
            if ((title == null) || (title.mNumber > lastVersiculo)) {
                mTitle.setText("");
                mSubTitle.setText("");
            } else {
                mTitle.setText(title.mText);
                mSubTitle.setText("Vs." + title.mNumber + "-" + lastVersiculo);
            }
        }
    }

    @OnClick(R.id.button_next)
    public void moveNextChapter(View view) {
        int nextChapter = mSpinnerChapter.getSelectedItemPosition() + 1;
        if (nextChapter < mChapterAdapter.getCount()) {
            mSpinnerChapter.setSelection(nextChapter);
        } else {
            int nextBook = mSpinnerBook.getSelectedItemPosition() + 1;
            if (nextBook < mBookAdapter.getCount()) {
                mSpinnerBook.setSelection(nextBook);
            }
        }
    }

    @OnClick(R.id.button_back)
    public void movePreviousChapter(View view) {
        int prevChapter = mSpinnerChapter.getSelectedItemPosition() - 1;
        if (prevChapter >= 0) {
            mSpinnerChapter.setSelection(prevChapter);
        } else {
            int prevBook = mSpinnerBook.getSelectedItemPosition() - 1;
            if (prevBook >= 0) {
                mSpinnerBook.setSelection(prevBook);
                mIsBack = true;
            }
        }
    }
}
