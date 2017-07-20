package es.gromenauer.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * TF-IDF evaluation criteria for words and sentences
 * 
 * Formula (Number finding of (word/sentence)/Number of words of the file) * log (Number of files/Number of files where has been found the word/sentence)
 * @author Usuario
 */
public class TFIDF {
	
	public static BigDecimal rangeTFIDF(double TF, double IDF) throws Exception{
		double res = TF * Math.log(IDF);
		BigDecimal response = new BigDecimal(res);		
		response = response.multiply(new BigDecimal(100)).setScale(2, RoundingMode.CEILING);
		return response;
		
	}
	

}
