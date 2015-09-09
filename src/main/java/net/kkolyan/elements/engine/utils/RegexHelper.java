package net.kkolyan.elements.engine.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nplekhanov
 */
public class RegexHelper {

    public static List<List<String>> find(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        List<List<String>> groups = new ArrayList<List<String>>();
        while (matcher.find()) {
            List<String> group = new ArrayList<String>();
            for (int i = 0; i < matcher.groupCount(); i ++) {
                group.add(matcher.group(1 + i));
            }
            groups.add(group);
        }
        return groups;
    }
}
