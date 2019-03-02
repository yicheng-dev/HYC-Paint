package util;

import java.util.regex.Pattern;

public class StringUtil {

    public static boolean isInteger(String s){
        Pattern pattern = Pattern.compile("[+-]?[0-9]+");
        return pattern.matcher(s).matches();
    }

    public static boolean isDouble(String s){
        Pattern pattern = Pattern.compile("[+-]?([0-9]*\\.?[0-9]+|[0-9]+\\.)");
        return pattern.matcher(s).matches();
    }
}
