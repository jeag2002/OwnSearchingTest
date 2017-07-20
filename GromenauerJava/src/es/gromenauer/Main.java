package es.gromenauer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.security.InvalidParameterException;
import java.util.Scanner;

import es.gromenauer.indexing.ProcessFiles;
import es.gromenauer.model.Model;
import es.gromenauer.scoring.Score;
import es.gromenauer.searching.Search;
import es.gromenauer.utils.FileUtils;


public class Main {
	
	ProcessFiles pFile;
	Model pModel;
	Search search;
	Score score;
	
	
	/**
	 * Constructor
	 * @throws Exception
	 */
	public Main(){
		pFile = new ProcessFiles();
		search = new Search();
		score = new Score();
	}
	
	/**
	 * Run method. get Directory of data for processing information and indexing
	 * @param args Input parameter from console
	 * @exception InvalidParameterException --> Less input parameters than expected.
	 * @exception FileNotFoundException --> File not found.
	 * @exception NotDirectoryException --> Is not a Directory.
	 * @exception Exception --> Other internals Exceptions
	 * @return
	 */
	
	public int processRun(String[] args) throws 
	InvalidParameterException, 
	FileNotFoundException, 
	NotDirectoryException,
	Exception{
		int result = 0;
		
		if (args.length == 0){
			System.out.println("WARN - Process Expects at least one parameter!");
			System.out.println("[1] -- directory of data for indexing");
			
			throw new InvalidParameterException("Not enough parameters");
			
		}else if (args.length > 0){
			String path = args[0];
			File fil = new File(path);
			if (!fil.exists()){
				System.out.println("WARN - [" + path + "] doesn't exists!");
				throw new FileNotFoundException("Path [" + path + "] doesn't exist");
			}
			
			else if (!fil.isDirectory()){
				System.out.println("WARN - ["+path + "] is not a directory");
				throw new NotDirectoryException("Path [" + path + "] is not a Directory");
			}
			
			else{
				result = createConsole(path);
			}
		}
		
		return result;
	}
	
	
	/**
	 * console Method. process information, create Console, define indexing, define searching  
	 * @param pathToDirectory directory of input data for processing 
	 * @return
	 */
	
	
	private int createConsole(String pathToDirectory) throws Exception{
		int result = 0;
		
		final File dir = new File(pathToDirectory);
		int DONE=0;
		
		System.out.println("***********************************************");
		System.out.println("Begin to process directory " + dir.getAbsolutePath());
		
		File files[] = FileUtils.getListOfFile(dir);
		System.out.println("Processing (" + files.length + ") files");
		
		long firstStepMillis = System.currentTimeMillis();
		pModel = pFile.runProcessFiles(dir,files);		
		long endStepMillis = System.currentTimeMillis();
		
		endStepMillis = endStepMillis - firstStepMillis;
		System.out.println("Time analysis/processing files found in directory (" + dir.getAbsolutePath() + ") : (" + endStepMillis + ") ms");
		
		
		score.Initialize(pModel,dir);
		search.Initialize(pModel,score);
		
		System.out.println("Commands:");
		System.out.println("search> (criteria) found data in file processed");
		System.out.println("search> :quit exit of program");
		System.out.println("***********************************************");
		
		
		Scanner keyboard = new Scanner(System.in);
		while (DONE!=es.gromenauer.utils.Constants.END){
			
			System.out.print(es.gromenauer.utils.Constants.Console_Search);
			String line = keyboard.nextLine();
			if (line.equals(es.gromenauer.utils.Constants.Console_Quit)){
				DONE = es.gromenauer.utils.Constants.END;
			}else{
				firstStepMillis = System.currentTimeMillis();
				search.searchSentencesInFiles(line);
				endStepMillis = System.currentTimeMillis();
				endStepMillis = endStepMillis - firstStepMillis;
				
				System.out.println("Time analysis/processing query (" + line + ") : (" + endStepMillis + ") ms");
				score.printResults();
				score.clear();
			}
		}
		
		System.out.println(es.gromenauer.utils.Constants.Console_Quit_MSG);
		
		return result;
		
	}

	
	/**
	 * MAIN M
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args){
		int halt = 0;
		
		try{
			Main main = new Main();
			halt = main.processRun(args);
		}catch(Exception e){
			System.out.println("GENERAL ERROR " + e.getMessage());
			halt = -1;
		}
		System.exit(halt);
	}

}
