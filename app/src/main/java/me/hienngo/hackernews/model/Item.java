package me.hienngo.hackernews.model;

import java.util.List;

/**
 * @author hienngo
 * @since 9/30/17
 */

public class Item {
    public long id;
    public String by;
    public List<Long> kids;
    public long parent;
    public String text;
    public long time;
    public String title;
    public String type;
    public String url;
    public int score;
    public boolean deleted;
    public boolean dead;
}
