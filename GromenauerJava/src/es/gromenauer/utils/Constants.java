package es.gromenauer.utils;


public class Constants {
	//CONSOLE SEARCH
	public static final String Console_Search = "search> ";
	
	//CONSOLE QUIT COMMAND
	public static final String Console_Quit=":quit";
	
	//CONSOLE QUIT MESSAGE
	public static final String Console_Quit_MSG="BYE";
	
	//FOLDER INDEXING FILES
	public static final String IndexFolder="index";
	
	//FLAG DONE MAIN CONSOLE
	public static final int END = 1;
	
	//DEFAULT STOP WORDS
	public static final String stopWords[] = {"a","an","and","are","as","at","be","by","for","from","has","he","in","is","it","its","of","on","that","the","to","was","were","will","with"};
	
	//BACKTOFILE
	public static final Long MAXREGISTERTOMEMORY = 100000L;
	
	//PRINT RESULTS
	public static final Long MAXRESULTS = 10L;
	
	//FILES
	public static final String FILE_TOTAL_COUNT = "totalCountData.idx";
	public static final String TERM_IN_FILES = "termFiles.idx";
	public static final String TERM_IN_FILES_AND_FIELDS = "termFilesFields.idx";
	public static final String TERM_IN_FILES_AND_FIELDS_COUNTS = "termFilesFieldsCounts.idx";
	
}
