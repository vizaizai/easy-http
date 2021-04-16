package demo.model;

/**
 * @author liaochongwei
 * @date 2020/8/3 10:28
 */
public class Book1 {

    private String id;

    private String lang;

    private String name;

    private String author;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book1{" +
                "id='" + id + '\'' +
                ", lang='" + lang + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
