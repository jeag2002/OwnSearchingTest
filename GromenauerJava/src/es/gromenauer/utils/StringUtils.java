package es.gromenauer.utils;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Utilities working with term/words
 * @author Usuario
 */

public final class StringUtils {
	
	//String to Integer
	public static Integer val(String inputData){
		Integer iData = 0;
		
		try{
			iData = Integer.parseInt(inputData);
;		}catch(Exception e){
			iData = -1;
		}finally{
			return iData;
		}
	}
	
	//String to Long
	public static Long valLong(String inputData){
		Long iData = 0L;
		try{
			iData = Long.parseLong(inputData);
		}catch(Exception e){
			iData = 0L;
		}finally{
			return iData;
		}
	}
	
	//Sentence to String[]
	public static String[] sentenceToArrayOfString(String sentence) throws Exception{
		
		String line_1 = sentence.replaceAll("[\\|\\(\\)\\{\\}\\[\\]=/\"]", ";");
		
		try{
			line_1 = normalizeNumerics(line_1);
		}catch(Exception e){
			line_1 = sentence.replaceAll("[\\|\\(\\)\\{\\}\\[\\]=/\"]", ";");
		}
		
		String data_1[] = line_1.split("([?!:,;\\.\\t*\\s*])+");
		
		final Stemmer stemmer = new Stemmer();
		
		data_1 = (String[])Arrays.stream(data_1)
		.map(n -> n.toLowerCase())
		//.map(n -> stemmer.stringToStemmerString(n))
		.filter(x->!x.trim().equalsIgnoreCase(""))
		.filter(e -> !Stream.of(Constants.stopWords).anyMatch(x -> x.equals(e)))
		.toArray(String[]::new);
			
		return data_1;
		
	}
	
	//normalize numeric strings for reverse indexing.
	public static String normalizeNumerics(String sentence) throws Exception{
		
		char data[] = sentence.toCharArray();
		
		for(int i=0;i<sentence.length(); i++){
			if ((data[i]==',')||(data[i]=='.')){
				if (i > 0){
					if (
					   (data[i-1]=='0')||
					   (data[i-1]=='1') ||
					   (data[i-1]=='2') ||
					   (data[i-1]=='3') ||
					   (data[i-1]=='4') ||
					   (data[i-1]=='5') ||
					   (data[i-1]=='6') ||
					   (data[i-1]=='7') ||
					   (data[i-1]=='8') ||
					   (data[i-1]=='9')){
						
						if (data[i]==','){data[i]='#';}
						else if (data[i]=='.'){data[i]='@';}
						
					}
				}
			}
		}
		
		sentence = new String(data);
		return sentence;
	}

}
