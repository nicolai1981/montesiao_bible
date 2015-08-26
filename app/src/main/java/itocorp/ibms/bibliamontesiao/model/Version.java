package itocorp.ibms.bibliamontesiao.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "VERSION")
public class Version extends Model {
    @Column(name = "LANGUAGE")
    public String mLanguage = "pt-BR";

    @Column(name = "NAME")
    public String mName = "Almeida Corrigida e Revisada";

    @Column(name = "AUTHOR")
    public String mAuthor = "Author";

    public static final Version getDefault() {
        Version item = new Select()
                .from(Version.class)
                .where("LANGUAGE=? AND NAME=?", "pt-BR", "Almeida Corrigida e Revisada")
                .executeSingle();
        if (item == null) {
            item = new Version();
            item.save();
        }
        return item;
    }
}
