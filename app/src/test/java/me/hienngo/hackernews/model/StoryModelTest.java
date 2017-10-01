package me.hienngo.hackernews.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author hienngo
 * @since 10/1/17
 */
@RunWith(RobolectricTestRunner.class)
public class StoryModelTest {
    Item item;
    @Before
    public void setup() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 6);
        calendar.set(Calendar.DAY_OF_MONTH, 22);

        item = new Item();
        item.text = "Text";
        item.by = "Hien Ngo";
        item.id = 1;
        item.score = 10;
        item.title = "Title";
        item.kids = new ArrayList<>();

        item.time = calendar.getTimeInMillis()/1000;
    }

    @Test
    public void testStoryModel() {
        StoryModel storyModel = new StoryModel(item);
        assertEquals(storyModel.getTitle(), "Title");
        assertEquals(storyModel.getInfo(), "10 points by Hien Ngo Jul 22, 2017 | 0 comment");
        assertEquals(storyModel.getItemId(), 1);

        List<Long> kids = new ArrayList<>();
        kids.add(0L);
        kids.add(1L);
        item.kids = kids;
        storyModel = new StoryModel(item);
        assertEquals(storyModel.getInfo(), "10 points by Hien Ngo Jul 22, 2017 | 2 comment");
        assertEquals(storyModel.getKids(), kids);
    }
}