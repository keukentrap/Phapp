package haakjeopenen.phapp.nonactivityclasses;

import java.util.Date;

/**
 * Created by wietze on 5/26/16.
 */
public class PostItem implements Comparable<PostItem> {
    public String title;
    public String content;
    public Date date;
    public String author;
    public int id;

    public PostItem(int id, String title, String content, Date date, String author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.author = author;

    }

    @Override
    public String toString() {
        return content;
    }

    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     * a positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @Override
    public int compareTo(PostItem another) {
        return another.date.compareTo(this.date);
    }
}




