package me.hienngo.hackernews.modal;

import java.util.List;

/**
 * @author hienngo
 * @since 9/30/17
 */

public class Item {
    public long id;
    public List<Long> kids;
    public long parent;
    public String text;
    public long time;
    public String title;
    public String type;
    public String url;
    public int score;
}
