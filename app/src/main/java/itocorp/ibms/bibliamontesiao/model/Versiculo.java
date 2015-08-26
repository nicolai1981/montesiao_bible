package itocorp.ibms.bibliamontesiao.model;

import android.content.Context;
import android.content.res.Resources;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Table(name = "VERSICULOS")
public class Versiculo extends Model {
    public static final int TYPE_TITLE = 1;
    public static final int TYPE_TEXT = 2;

    @Column(name = "TYPE")
    public int mType = TYPE_TEXT;

    @Column(name = "NUMBER")
    public int mNumber = 0;

    @Column(name = "TEXT")
    public String mText = "";

    @Column(name = "CHAPTER")
    public int mChapter = 0;

    @Column(name = "LIVRO")
    public String mLivro;

    @Column(name = "VERSION")
    public Version mVersion;

    public static final List<Versiculo> getChapter(int chapter, String livro, Version version, Context context) {
        Version item = version;
        if (item == null) {
            item = Version.getDefault();
        }
        List<Versiculo> list = new Select()
                .from(Versiculo.class)
                .where("VERSION=? AND LIVRO=? AND CHAPTER=?", item.getId(), livro, chapter)
                .orderBy("NUMBER ASC, TYPE ASC")
                .execute();

        if (list.size() == 0) {
            list = getDefaultChapter(livro, chapter, context);
        }

        return list;
    }

    public static final List<Versiculo> getTitleFromChapter(int chapter, String livro, Version version) {
        Version item = version;
        if (item == null) {
            item = Version.getDefault();
        }
        List<Versiculo> list = new Select()
                .from(Versiculo.class)
                .where("VERSION=? AND LIVRO=? AND CHAPTER=? AND TYPE=?", item.getId(), livro, chapter, TYPE_TITLE)
                .orderBy("NUMBER ASC")
                .execute();
        return list;
    }

    private static final List<Versiculo> getDefaultChapter(String livro, int chapter, Context context) {
        Version version = Version.getDefault();
        Resources resources = context.getResources();
        String fileName = livro + "_" + chapter;

        InputStream is = resources.openRawResource(resources.getIdentifier("raw/v_" + fileName, "raw", context.getPackageName()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            int counter = 1;
            while ((line = reader.readLine()) != null) {
                Versiculo versiculo = new Versiculo();
                versiculo.mVersion = version;
                versiculo.mLivro = livro;
                versiculo.mChapter = chapter;
                versiculo.mType = Versiculo.TYPE_TEXT;
                versiculo.mNumber = counter++;
                versiculo.mText = line;
                versiculo.save();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            is = resources.openRawResource(resources.getIdentifier("raw/t_" + fileName, "raw", context.getPackageName()));
            reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("@");
                Versiculo versiculo = new Versiculo();
                versiculo.mVersion = version;
                versiculo.mLivro = livro;
                versiculo.mChapter = chapter;
                versiculo.mType = Versiculo.TYPE_TITLE;
                versiculo.mNumber = Integer.valueOf(values[0]);
                versiculo.mText = values[1];
                versiculo.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return new Select()
                .from(Versiculo.class)
                .where("VERSION=? AND LIVRO=? AND CHAPTER=?", version.getId(), livro, chapter)
                .orderBy("NUMBER ASC, TYPE ASC")
                .execute();
    }
}
