package club.blog.redis.util;

import java.util.List;
import java.util.Set;

public class StringArrayUtil {

    public static String[]  toArray(List<String> list){
        String[] strings = new String[list.size()];
        strings =  list.toArray(strings);
        return  strings;
    }

    public static String[] toArray(Set<String> set){
        String[] strings = new String[set.size()];
        strings =  set.toArray(strings);
        return  strings;
    }

}
