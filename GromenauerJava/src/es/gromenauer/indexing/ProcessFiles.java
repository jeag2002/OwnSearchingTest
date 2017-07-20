package es.gromenauer.indexing;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import es.gromenauer.model.Coordinates;
import es.gromenauer.model.Model;
import es.gromenauer.utils.Constants;
import es.gromenauer.utils.FileUtils;
import es.gromenauer.utils.Stemmer;
import es.gromenauer.utils.StringUtils;

public class ProcessFiles {
	
	private long numWordsOfFile;
	private long realPosition;
	private int numFiles;
	
	public ProcessFiles(){
		
		numWordsOfFile = 0L;
		realPosition = 0L;
		numFiles = 1;
	}
	
	/**
	 * Process all files of the directory
	 * @param dir inputDirectory
	 * @return Model Class (Info parsing)
	 * @throws IOException
	 * @throws Exception
	 */
	
	public Model runProcessFiles(File dir, File files[]) throws IOException, Exception{
		
		
		Model mod = new Model();	
		mod.Initialize(dir.getAbsolutePath());
	
		
		if (files != null){
			if (files.length > 0){			
				
				
				mod.setMaxFiles(files.length);
				
				for(File fil: files){
					processFile(fil,numFiles,mod);
					mod.insertTotProcessedWords(numFiles, numWordsOfFile);
					numWordsOfFile = 0L;
					realPosition = 0L;
					numFiles += 1;
				}
				
				mod.backDataToFiles();
			}else{
				throw new IOException("Empty directory ");
			}
		}else{
			throw new IOException("Error when trying to obtain files from directory");
		}
		
		return mod;
	}
	
	 
	/**
	 * Process File of the list of files. 
	 * @param fil
	 * @return
	 * @throws Exception
	 */
	
	public int processFile(File fil, int numFiles, Model mod) throws Exception{
		int returnCode = 0;
		
		
		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
		    inputStream = new FileInputStream(fil);
		    sc = new Scanner(inputStream, "UTF-8");
		    while (sc.hasNextLine()) {
		        String line = sc.nextLine();
		        processLine(line, numFiles, mod);
		    }
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} finally {
		    if (inputStream != null) {
		        inputStream.close();
		    }
		    if (sc != null) {
		        sc.close();
		    }
		}	
		return returnCode;
	}
	
	/**
	 * Process field of any file of the input directory
	 * @param line
	 * @param file
	 * @param mod
	 * @throws Exception
	 */
	

	public void processLine(String line, int file, Model mod) throws Exception{
		
			
			String data[] = StringUtils.sentenceToArrayOfString(line);
			
			boolean contains = false;
			String dataStemmer = "";
			int inputLine = 0;
			
			Pattern pattern = Pattern.compile("[0-9](#|@)");
			
			for(String word:data){		
				
				Matcher matcher = pattern.matcher(word);
				
				if (matcher.find()){
					String word_1 = word.replaceAll("#", ",");
					word_1 = word_1.replaceAll("@", ".");
					inputLine = line.toLowerCase().indexOf(word_1, inputLine);
				}else{
					inputLine = line.toLowerCase().indexOf(word, inputLine);
				}
				
				mod.insertDatahistogramDoc(file, word);
				mod.insertHistogramDocumentFields(file, word);
				mod.insertDataHistogramDocumentFields(file, numWordsOfFile, new Coordinates(inputLine, inputLine + word.length()), word);		
				numWordsOfFile += 1;
				inputLine += word.length();
		
			}
			realPosition += line.length();
		}

}
