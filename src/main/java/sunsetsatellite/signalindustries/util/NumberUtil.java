package sunsetsatellite.signalindustries.util;

public class NumberUtil {
    public static String[] shortSuffixes = new String[]{"k","M","B","T"};
    public static String[] longSuffixes = new String[]{"thousand","million","billion","trillion"};

    public static String format(double value){
        if(Double.isInfinite(value)){
            return "inf";
        }
        for (int i = 0; i < shortSuffixes.length; i++) {
            if(value >= 1000){
                value /= 1000;
                if(value < 1000){
                    return String.format("%.2f%s",value,shortSuffixes[i]);
                }
            } else {
                return String.valueOf(value);
            }
        }
        return "???";
    }
}
