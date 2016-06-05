package haakjeopenen.phapp.models;

import java.util.Date;

/**
 * Created by wietze on 5/26/16.
 */
public class NewsItem implements Comparable<NewsItem> {
    public String title;
    public String content;
    public Date date;
    public String author;
    public int id;

    public NewsItem(int id, String title, String content, Date date, String author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.author = author;

    }

    @Override
    public int compareTo(NewsItem another) {
        return another.date.compareTo(this.date);
    }
}




