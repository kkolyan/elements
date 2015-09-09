package net.kkolyan.elements.engine.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author nplekhanov
 */
public class CollectionUtilTest {
    @Test
    public void testLazyConcatenate() throws Exception {
        Collection<List<String>> lists = new ArrayList<List<String>>();
        lists.add(Arrays.asList("a","b","c"));
        lists.add(Arrays.asList("d","e","f"));
        lists.add(Arrays.asList("g","h"));
        lists.add(null);
        lists.add(Arrays.asList("i","j","k"));
        lists.add(Arrays.<String>asList());
        lists.add(Arrays.asList("l","m","n","o", "p"));
        List<String> resultList = CollectionUtil.lazyConcatenate(lists);

        String[] expected = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p"};

        Assert.assertEquals(16, resultList.size());
        for (int i = 0; i < expected.length; i ++) {
            Assert.assertEquals(expected[i], resultList.get(i));
        }


        String[] result = resultList.toArray(new String[resultList.size()]);

        Assert.assertArrayEquals(expected, result);
    }
}
