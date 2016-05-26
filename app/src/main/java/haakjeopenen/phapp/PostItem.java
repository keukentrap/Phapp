package haakjeopenen.phapp;

import java.util.Date;

/**
 * Created by wietze on 5/26/16.
 */
public class PostItem {
    private String title;
    private String content;
    private Date date;
    private String author;

    public PostItem(String title, String content, Date date, String author) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.author = author;
    }
}
