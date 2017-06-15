package com.caiex.account.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author wsy
 *
 */
public class NumberUtil {

    /**
     * 被除数是否能被除数整除
     * 
     * @param dividend
     *            被除数
     * @param divisor
     *            除数
     * @return
     */
    public static boolean isExactDivision(double dividend, double divisor) {

        double result = dividend / divisor;
        String afterDot = ((result + "").split("\\."))[1];
        return afterDot.charAt(0) == '0' && afterDot.length() == 1;
    }

    /**
     * 整数去掉小数点后的零
     * 
     * @param number
     * @return
     */
    public static String removeZeroAfterDot(double number) {

        if (isExactDivision(number, 1.0)) {
            return ((int) number) + "";
        }
        return number + "";
    }

    
   //向上取整 
    public static double getNumberAccordingToPercision(double oldNumber, int percision){
    	//ROUND_HALF_UP  向上取整    percision：保留几位小数
		BigDecimal setScale = BigDecimal.valueOf(oldNumber).setScale(percision, BigDecimal.ROUND_HALF_UP);
		
		return setScale.doubleValue();
	}
    
    

    private static final DecimalFormat decimalFormat = new DecimalFormat();
    
    static {
    	decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);
    }
    
    public static String doubleToScientificNotation(Double number, int scale) {
    	if (number < 10000) {
    		return number.toString();
    	}
    	StringBuilder pattern = new StringBuilder("0.");
    	
    	for (int i = 1; i < scale; i++) {
			pattern.append("0");
		}
    	pattern.append("E00");
    	
    	decimalFormat.applyPattern(pattern.toString());
    	return decimalFormat.format(number);
    }
    
    public static Double scientificNotationToDouble(String scientificNotationNumber, int scale) {
    	return new BigDecimal(scientificNotationNumber).setScale(scale, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }
    
}
