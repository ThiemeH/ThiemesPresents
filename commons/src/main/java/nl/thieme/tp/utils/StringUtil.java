package nl.thieme.tp.utils;

public class StringUtil {

    public static String fillSpace(String s, int len) {
        int amount = len - s.length();
        return s + repeatSpace(amount);
    }

    public static String repeatSpace(int amount) {
        String result = "";
        for(int i = 0; i < amount; i++) {
            result += " ";
        }
        return result;
    }
}
