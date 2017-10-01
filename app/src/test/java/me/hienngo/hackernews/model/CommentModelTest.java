package me.hienngo.hackernews.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * @author hienngo
 * @since 10/1/17
 */
@RunWith(RobolectricTestRunner.class)
public class CommentModelTest {
    Item item;
    @Before
    public void setup() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 6);
        calendar.set(Calendar.DAY_OF_MONTH, 22);

        item = new Item();
        item.text = "<b>html text</b>";
        item.by = "Hien Ngo";
        item.id = 1;
        item.score = 10;
        item.title = "Title";
        item.kids = new ArrayList<>();

        item.time = calendar.getTimeInMillis()/1000;
    }

    @Test
    public void testCommentModel() {
        CommentModel storyModel = new CommentModel(item, 0);
        assertEquals(storyModel.getLevel(), 0);
        assertEquals(storyModel.getItemId(), 1);
        assertEquals(storyModel.getComment().toString(), "html text");
        assertEquals(storyModel.getInfo(), "Hien Ngo Jul 22, 2017");

        item.text = null;
        storyModel = new CommentModel(item, 1);
        assertEquals(storyModel.getComment(), null);
        assertEquals(storyModel.getLevel(), 1);
    }
}