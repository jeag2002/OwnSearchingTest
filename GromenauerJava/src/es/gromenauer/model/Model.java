package es.gromenauer.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import es.gromenauer.utils.Constants;
import es.gromenauer.utils.FileUtils;

/**
 * CLASS MODEL. KEEP ALL THE INFORMATION OF THE PROCESSING/SEARCHING/SCORING ENGINE.
 * @author Usuario
 *
 */

public class Model {
	
	//Num data for files (needed for scoring step)
	private List<Long> numDataOfFiles; //file_1
	
	//Histogram term by document
	private List<TermsInDocument> histogramDoc; //file_2
	private Long histogramDocSize;

	//Elements term by document & field 
	private List<TermsInDocumentFields> histogramDocumentFields; //file_3
	private Long histogramDocumentFieldsSize;
	
	//Elements term by document & field & count
	private List<TermsInDocumentFieldsCounts> histogramDocumentFieldsCounts; //file_4
	private Long histogramDocumentFieldsCountsSize;
	
	//Scoring Data
	private List<ScoringData> scores;
	
	//innerPath
	private String path;
	
	//boolean keep data in disk
	private boolean setDataToDisk;
	
	//number max of files
	private Integer maxFiles;


	/**
	 * Create Model
	 */
	public Model (){
		numDataOfFiles = new ArrayList<Long>();
		histogramDoc = new ArrayList<TermsInDocument>();
		histogramDocumentFields = new ArrayList<TermsInDocumentFields>();
		histogramDocumentFieldsCounts = new ArrayList<TermsInDocumentFieldsCounts>();
		
		scores = new ArrayList<ScoringData>();
		
		
		histogramDocSize = 0L;
		histogramDocumentFieldsSize = 0L;
		histogramDocumentFieldsCountsSize = 0L; 
		
		path = "";
		setDataToDisk = true;
		
		maxFiles = 0;
	}
	
	/**
	 * Delete all data of the Model
	 */
	
	public void clear(){
		numDataOfFiles.clear();
		histogramDoc.clear();
		histogramDocumentFields.clear();
		histogramDocumentFieldsCounts.clear();
	}
	
	//INITIALIZE STEP
	///////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Create directory of indexation
	 * @param _path
	 */
	
	public void Initialize(String _path){
		path = _path;
		try{
			FileUtils.deleteFolder(path, Constants.IndexFolder);
			FileUtils.createFolder(path, Constants.IndexFolder);
			setDataToDisk = true;
		}catch(Exception e){
			System.out.println("WARN - Can create files of indexation (" + e.getMessage() + ")");
			setDataToDisk = false;
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//INDEXING STEP
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Collection number of findings of terms by files
	 * @param file -> file (container of the term)
	 * @param word -> term
	 */
	
	public void insertHistogramDocumentFields(int file, String word) throws Exception{
		
		final TermsInDocumentFieldsCounts tIDFC = histogramDocumentFieldsCounts.stream().filter(x -> word.equalsIgnoreCase(x.getTerm())).findAny().orElse(null);
		if (tIDFC == null){
			
			TermsInDocumentFieldsCounts tIDNew = new TermsInDocumentFieldsCounts(word,file);
			histogramDocumentFieldsCounts.add(tIDNew);
			histogramDocumentFieldsCounts = (ArrayList<TermsInDocumentFieldsCounts>)histogramDocumentFieldsCounts.stream().sorted().collect(Collectors.toList());
			
		}else{
			histogramDocumentFieldsCounts
			.stream()
			.filter(x -> word.equals(x.getTerm()))
			.findFirst()
			.ifPresent(x -> x.addCounts(new Long(file)));
		}
		
		if ((histogramDocumentFieldsCounts.size() >= Constants.MAXREGISTERTOMEMORY) && (setDataToDisk)){
			
			mergeHistogramDocumentFieldsCounts();
			histogramDocumentFieldsCountsSize += histogramDocumentFieldsCounts.size();
			histogramDocumentFieldsCounts.clear();
			
		}
		
	}
	
	/**
	 * Collection number of files where we find a term
	 * @param file -> file (container of the term)
	 * @param word -> term
	 */
	
	
	public void insertDatahistogramDoc(int file, String word) throws Exception{
		final TermsInDocument tID = histogramDoc.stream().filter(x -> word.equalsIgnoreCase(x.getTerm())).findAny().orElse(null);
		if (tID == null){
			
			TermsInDocument tIDNew = new TermsInDocument(word,file);
			histogramDoc.add(tIDNew);
			histogramDoc = (ArrayList<TermsInDocument>)histogramDoc.stream().sorted().collect(Collectors.toList());
			
		}else{
			tID.addDocument(file);
			histogramDoc
			.stream()
			.filter(x -> word.equals(x.getTerm()))
			.findFirst()
			.ifPresent(x -> x.setDocuments(tID.getDocuments()));
		}
		
		if ((histogramDoc.size() >= Constants.MAXREGISTERTOMEMORY)  && (setDataToDisk)){
			mergehistogramDoc();
			histogramDocSize += histogramDoc.size(); 
			histogramDoc.clear();
		}
		
	}
	
	/**
	 * Collection of term's definitions of all the files
	 * @param file 		-> file (container of the term)
	 * @param numWord	-> index of the term into the file
	 * @param xy		-> physic situation of the term in the file
	 * @param word		-> term
	 */
	
	
	public void insertDataHistogramDocumentFields(int file, long numWord, Coordinates xy, String word){
		TermsInDocumentFields tIDF = new TermsInDocumentFields(word,file,numWord,xy);
		histogramDocumentFields.add(tIDF);
		
		if ((histogramDocumentFields.size() >= Constants.MAXREGISTERTOMEMORY)  && (setDataToDisk)){
			histogramDocumentFieldsSize += histogramDocumentFields.size();
			unloadHistogramDocFieldToFile();
			histogramDocumentFields.clear();
		}
	}
	
	/**
	 * Collection of number of terms by files
	 * @param file		-> file (container of the term)
	 * @param numWord	-> number of terms of the file
	 */
	
	public void insertTotProcessedWords(int file, long numWord){
		numDataOfFiles.add(numWord);
	}
	
	/**
	 * Flag control load/unload data to file (for debugging purposes)
	 * @return
	 */
	
	///////////////////////////////////////////////////////////////////
	public boolean isSetDataToDisk() {
		return setDataToDisk;
	}


	public void setSetDataToDisk(boolean setDataToDisk) {
		this.setDataToDisk = setDataToDisk;
	}
	
	
	public Integer getMaxFiles() {
		return maxFiles;
	}

	public void setMaxFiles(Integer maxFiles) {
		this.maxFiles = maxFiles;
	}
	///////////////////////////////////////////////////////////////////
	
	/**
	 * Download data to files (back to files)
	 * @throws Exception
	 */
	
	public void backDataToFiles() throws Exception{
		
		histogramDocumentFieldsSize += histogramDocumentFields.size();
		
		if (setDataToDisk){
			unloadTotalCount();
			unloadHistogramDocFieldToFile();
	
		
			if (histogramDocSize == 0){
				histogramDocSize += histogramDoc.size();
				unloadHistogramDocToFile(histogramDoc);
			}else{
				mergehistogramDoc();
				histogramDocSize += histogramDoc.size();
			}
		
			if (histogramDocumentFieldsCountsSize == 0){
				histogramDocumentFieldsCountsSize += histogramDocumentFieldsCounts.size();
				unloadHistogramDocumentFieldsCountsToFile(histogramDocumentFieldsCounts);
			}else{
				
				mergeHistogramDocumentFieldsCounts();
				histogramDocumentFieldsCountsSize += histogramDocumentFieldsCounts.size();
			}
		}
	}
	
	/**
	 * Merge data memory + file (back memory)
	 * @throws Exception
	 */
	
	public void mergehistogramDoc() throws Exception{
		
		for(long i=0L; i<histogramDocSize; i=i+Constants.MAXREGISTERTOMEMORY){
			
			List<TermsInDocument> dataFile = loadHistogramDoc(i);
			
			
			for(TermsInDocument dataMem: histogramDoc){
				
				final TermsInDocument tIDFC = dataFile.stream().filter(x -> dataMem.getTerm().equalsIgnoreCase(x.getTerm())).findAny().orElse(null);
				
				if (tIDFC != null){
					dataFile
					.stream()
					.filter(x -> dataMem.getTerm().equalsIgnoreCase(x.getTerm()))
					.findFirst()
					.ifPresent(x -> x.mergeDocuments(dataMem.getDocuments()));
					
					histogramDoc.remove(dataMem);
				}
			}
			
			unloadhistogramDocToFile(dataFile, i, i+dataFile.size());
			dataFile.clear();
			
		}
		this.unloadHistogramDocToFile(histogramDoc);
	}
	
	
	
	
	/**
	 * Merge data memory + file (back memory) termFilesFieldsCounts.idx
	 * @throws Exception
	 */
	
	public void mergeHistogramDocumentFieldsCounts() throws Exception{
		
		for(long i=0L; i<histogramDocumentFieldsCountsSize; i=i+Constants.MAXREGISTERTOMEMORY){
			
			List<TermsInDocumentFieldsCounts> dataFile = loadHistogramDocumentFieldsCounts(i);
			
			
			for(TermsInDocumentFieldsCounts dataMem: histogramDocumentFieldsCounts){
				
				final TermsInDocumentFieldsCounts tIDFC = dataFile.stream().filter(x -> dataMem.getTerm().equalsIgnoreCase(x.getTerm())).findAny().orElse(null);
				
				if (tIDFC != null){
					dataFile
					.stream()
					.filter(x -> dataMem.getTerm().equalsIgnoreCase(x.getTerm()))
					.findFirst()
					.ifPresent(x -> x.mergeCountsByTerm(dataMem.getCountsByTerm()));
					
					histogramDocumentFieldsCounts.remove(dataMem);
				}
			}
			
			unloadHistogramDocumentFieldsCountsToFile(dataFile, i, i+dataFile.size());
			dataFile.clear();
			
		}
		
		this.unloadHistogramDocumentFieldsCountsToFile(histogramDocumentFieldsCounts);
	}
	
	
	/**
	 * Unload number of findings of terms in every input file to termFiles.idx
	 * @param _histogramDocumentFieldsCounts
	 * @param iniPos
	 * @param finPos
	 * @throws Exception
	 */
	
	
	public void unloadhistogramDocToFile(List<TermsInDocument> _histogramDoc, long iniPos, long finPos) throws Exception{
		String pathFile_4 = path + File.separatorChar + Constants.IndexFolder + File.separatorChar + Constants.TERM_IN_FILES;
		try{
			if (_histogramDoc.size() > 0){
				
				String row = _histogramDoc.get(0).toStringFormatted(maxFiles);
				byte[] rowToByte = row.getBytes();
				//->Get size of the row (register)
				long sizeOfByte = rowToByte.length;
				
				long iniPosInFile = iniPos * sizeOfByte;
				
				RandomAccessFile fileStore = new RandomAccessFile(pathFile_4, "rw");
				
				for(int i=0; i<histogramDocumentFieldsCounts.size(); i++){
					fileStore.seek(iniPosInFile + (sizeOfByte*i));
					row = _histogramDoc.get(i).toStringFormatted(maxFiles);
					fileStore.write(row.getBytes());
				}
				
				fileStore.close();
			}

		}catch(Exception e){
			System.out.println("WARN - Cannot save data TermsInDocument to document (" + Constants.TERM_IN_FILES + ") : " + e.getMessage());
		}
	}

	/**
	 * Unload number of findings of terms in every input file to termFilesFieldsCounts.idx
	 * @param _histogramDocumentFieldsCounts
	 * @param iniPos
	 * @param finPos
	 * @throws Exception
	 */
	
	
	public void unloadHistogramDocumentFieldsCountsToFile(List<TermsInDocumentFieldsCounts> _histogramDocumentFieldsCounts, long iniPos, long finPos) throws Exception{
		String pathFile_4 = path + File.separatorChar + Constants.IndexFolder + File.separatorChar + Constants.TERM_IN_FILES_AND_FIELDS_COUNTS;
		try{
			if (_histogramDocumentFieldsCounts.size() > 0){
				
				String row = _histogramDocumentFieldsCounts.get(0).toStringFormatted(maxFiles);
				byte[] rowToByte = row.getBytes();
				//->Get size of the row (register)
				long sizeOfByte = rowToByte.length;
				
				long iniPosInFile = iniPos * sizeOfByte;
				
				RandomAccessFile fileStore = new RandomAccessFile(pathFile_4, "rw");
				
				for(int i=0; i<histogramDocumentFieldsCounts.size(); i++){
					fileStore.seek(iniPosInFile + (sizeOfByte*i));
					row = _histogramDocumentFieldsCounts.get(i).toStringFormatted(maxFiles);
					fileStore.write(row.getBytes());
				}
				
				fileStore.close();
			}

		}catch(Exception e){
			System.out.println("WARN - Cannot save data TermsInDocument to document (" + Constants.TERM_IN_FILES_AND_FIELDS_COUNTS + ") : " + e.getMessage());
		}
	}

	
	/**
	 * Unload number of findings of terms in every input file to termFilesFieldsCounts.idx
	 * @param _histogramDocumentFieldsCounts
	 * @throws Exception
	 */
	
	public void unloadHistogramDocumentFieldsCountsToFile(List<TermsInDocumentFieldsCounts> _histogramDocumentFieldsCounts) throws Exception{
		String pathFile_4 = path + File.separatorChar + Constants.IndexFolder + File.separatorChar + Constants.TERM_IN_FILES_AND_FIELDS_COUNTS;
		try{
			
			FileWriter fout = new FileWriter(pathFile_4, true);
			for(TermsInDocumentFieldsCounts tIDFC: _histogramDocumentFieldsCounts){
				fout.write(tIDFC.toStringFormatted(maxFiles));
			}
			fout.close();

			
		}catch(Exception e){
			System.out.println("WARN - Cannot save data TermsInDocument to document (" + Constants.TERM_IN_FILES_AND_FIELDS_COUNTS + ") : " + e.getMessage());
		}
	}
	
	/**
	 * Load interval of files of termFilesFieldsCounts.idx MAX Constants.MAXREGISTERTOMEMORY
	 * @param init
	 * @return
	 * @throws Exception
	 */
	
	public List<TermsInDocumentFieldsCounts> loadHistogramDocumentFieldsCounts(long init) throws Exception{
		List<TermsInDocumentFieldsCounts> data = new ArrayList<TermsInDocumentFieldsCounts>();
		
		String pathFile_4 = path + File.separatorChar + Constants.IndexFolder + File.separatorChar + Constants.TERM_IN_FILES_AND_FIELDS_COUNTS;
		
		try (Stream<String> lines = Files.lines(Paths.get(pathFile_4))) {
			
			Stream<String> lineInit = lines.skip(init).limit(Constants.MAXREGISTERTOMEMORY);
			lineInit.forEach(info->data.add((new TermsInDocumentFieldsCounts(info))));
		
		}catch(Exception e){
			System.out.println("WARN Error when trying to load data from file (" + Constants.TERM_IN_FILES_AND_FIELDS_COUNTS + ") to memory " + e.getMessage());
		}
		
		return data;
	}
	
	/**
	 * Load interval of files of termFiles.idx MAX Constants.MAXREGISTERTOMEMORY
	 * @param init
	 * @return
	 * @throws Exception
	 */
	
	public List<TermsInDocument> loadHistogramDoc(long init) throws Exception{
		List<TermsInDocument> data = new ArrayList<TermsInDocument>();
		
		String pathFile_1 = path + File.separatorChar + Constants.IndexFolder + File.separatorChar + Constants.TERM_IN_FILES;
		
		try (Stream<String> lines = Files.lines(Paths.get(pathFile_1))) {
			
			Stream<String> lineInit = lines.skip(init).limit(Constants.MAXREGISTERTOMEMORY);
			lineInit.forEach(info->data.add((new TermsInDocument(info))));
		
		}catch(Exception e){
			System.out.println("WARN Error when trying to load data from file (" + Constants.TERM_IN_FILES_AND_FIELDS_COUNTS + ") to memory " + e.getMessage());
		}
		
		return data;
	}
	
	/**
	 * Get Number of terms of files
	 */
	public List<Long> getNumDataOfFiles(){
		return numDataOfFiles;
	}
	
	
	/**
	 * Unload list of files where exist a term to file termFiles.idx
	 * @throws Exception
	 */
	
	public void unloadHistogramDocToFile( List<TermsInDocument> _histogramDoc) throws Exception{
		
		String pathFile_2 = path + File.separatorChar + Constants.IndexFolder + File.separatorChar + Constants.TERM_IN_FILES;
		
		try{
			FileWriter fout = new FileWriter(pathFile_2, true);
			for(TermsInDocument tIDNew: _histogramDoc){
				fout.write(tIDNew.toStringFormatted(maxFiles));
			}
			fout.close();
		
		}catch(Exception e){
			System.out.println("WARN - Cannot save data TermsInDocument to document (" + Constants.TERM_IN_FILES + ") : " + e.getMessage());
		}
	}
	
	/**
	 * Load interval of files of termFiles.idx MAX Constants.MAXREGISTERTOMEMORY
	 * @param init
	 * @return
	 * @throws Exception
	 */
	
	public List<TermsInDocument> loadHistogramDocToFile(long init) throws Exception{
		List<TermsInDocument> data = new ArrayList<TermsInDocument>();
		
		String pathFile_2 = path + File.separatorChar + Constants.IndexFolder + File.separatorChar + Constants.TERM_IN_FILES;
		
		try (Stream<String> lines = Files.lines(Paths.get(pathFile_2))) {
			
			Stream<String> lineInit = lines.skip(init).limit(Constants.MAXREGISTERTOMEMORY);
			lineInit.forEach(info->data.add((new TermsInDocument(info))));
			
		}catch(Exception e){
			System.out.println("WARN Error when trying to load data from file (" + Constants.TERM_IN_FILES + ") to memory " + e.getMessage());
		}
		
		return data;
	}
	
	
	/**
	 * Unload data of every term in any file to file termFilesField.idx
	 */
	public void unloadHistogramDocFieldToFile(){
		
		String pathFile_3 = path + File.separatorChar + Constants.IndexFolder + File.separatorChar + Constants.TERM_IN_FILES_AND_FIELDS;
		try{
			
			FileWriter fout = new FileWriter(pathFile_3, true);
			for(TermsInDocumentFields tIDF: histogramDocumentFields){
				fout.write(tIDF.toString()+"\r\n");
			}
			fout.close();
			
		}catch(Exception e){
			System.out.println("WARN - Cannot save data TermsInDocumentFields to document (" + Constants.TERM_IN_FILES_AND_FIELDS + ") : " + e.getMessage());
		}
	}
		
	
	/**
	 * Load interval of files of termFilesFields.idx MAX Constants.MAXREGISTERTOMEMORY
	 * @param init
	 * @return
	 * @throws Exception
	 */
	public List<TermsInDocumentFields> loadHistogramDocField(long init) throws Exception{
		List<TermsInDocumentFields> data = new ArrayList<TermsInDocumentFields>();
		
		String pathFile_3 = path + File.separatorChar + Constants.IndexFolder + File.separatorChar + Constants.TERM_IN_FILES_AND_FIELDS;
		
		try (Stream<String> lines = Files.lines(Paths.get(pathFile_3))) {
			Stream<String> lineInit = lines.skip(init).limit(Constants.MAXREGISTERTOMEMORY);
			lineInit.forEach(info->data.add((new TermsInDocumentFields(info))));
			
			
		}catch (Exception e) {
			System.out.println("WARN Error when trying to load data from file (" + Constants.TERM_IN_FILES_AND_FIELDS + ") to memory " + e.getMessage());
		}
		
		
		return data;
	}
	
	/**
	 * Unload data of total count of words by file to file totalCountData.idx
	 */
	
	public void unloadTotalCount(){
		
		String pathFile_1 = path + File.separatorChar + Constants.IndexFolder+File.separatorChar +Constants.FILE_TOTAL_COUNT;
		
		try{
			FileWriter fout = new FileWriter(pathFile_1, true);
			for(Long count: numDataOfFiles){
				fout.write(count.toString()+"\r\n");
			}
			fout.close();
			
		}catch(Exception e){
			System.out.println("WARN - Cannot save data TotalCount to document (" + Constants.FILE_TOTAL_COUNT + ") : " + e.getMessage());
		}	
	}
	
	
	/**
	 * Size of histogramDoc (Number of Documents contains term)
	 * @return
	 */
	public Long getHistogramDocSize() {
		return histogramDocSize;
	}
	
	/**
	 * Size of HistogramDocumentFields (finding of terms in any file)
	 * @return
	 */
	public Long getHistogramDocumentFieldsSize() {
		return histogramDocumentFieldsSize;
	}
	
	
	/**
	 * Size of Histogram 
	 * @return
	 */
	public Long getHistogramDocumentFieldsCountsSize() {
		return histogramDocumentFieldsCountsSize;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//SEARCHING
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Get list of files where exist a term.
	 * @param word
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Integer> getHistogramDoc(String word) throws Exception{
		
		ArrayList<Integer> documentsFound = new ArrayList<Integer>();
		TermsInDocument tID = histogramDoc.stream().filter(x -> word.equalsIgnoreCase(x.getTerm())).findAny().orElse(null);
		
		if (tID != null){
			documentsFound = tID.getDocuments();
		}else{
			
			if (this.setDataToDisk){
				if (histogramDocSize > Constants.MAXREGISTERTOMEMORY){
					
					boolean found = false;
					
					for(long i=0; (i<histogramDocSize) && (!found); i=i+Constants.MAXREGISTERTOMEMORY){
						
						List<TermsInDocument> data = loadHistogramDoc(i);
						tID = data.stream().filter(x -> word.equalsIgnoreCase(x.getTerm())).findAny().orElse(null);
						if (tID != null){
							found = true;
							documentsFound = tID.getDocuments();
						}
						data.clear();
						data = null;
					}
				}
			}
		}
		return documentsFound;
	}
	
	/**
	 * Get the List of number of finding data in files for a word
	 * @param word
	 * @return
	 * @throws Exception
	 */
	
	public ArrayList<Coordinates> getHistogramDocFieldsCounts(String word) throws Exception{
		
		ArrayList<Coordinates> documentsCountsFound = new ArrayList<Coordinates>();
		TermsInDocumentFieldsCounts tIDFC = histogramDocumentFieldsCounts.stream().filter(x -> word.equalsIgnoreCase(x.getTerm())).findAny().orElse(null);
		
		if (tIDFC != null){
			documentsCountsFound = tIDFC.getCountsByTerm();
		}else{
			
			if (this.setDataToDisk){
				if (histogramDocumentFieldsCountsSize > Constants.MAXREGISTERTOMEMORY){
					
					boolean found = false;
					
					for(long i=0; (i<histogramDocumentFieldsCountsSize) && (!found); i=i+Constants.MAXREGISTERTOMEMORY){
						
						List<TermsInDocumentFieldsCounts> data = loadHistogramDocumentFieldsCounts(i);
						tIDFC = data.stream().filter(x -> word.equalsIgnoreCase(x.getTerm())).findAny().orElse(null);
						if (tIDFC != null){
							found = true;
							documentsCountsFound = tIDFC.getCountsByTerm();
						}
						data.clear();
						data = null;
					}
				}
			}
		}
		return documentsCountsFound;
		
		
	}
	
	/**
	 * Get TermsInDocument (list of files where exist a term) List Data
	 * @return List
	 * @throws Exception
	 */
	
	public List<TermsInDocument> getHistogramDoc() throws Exception{
		if (!this.setDataToDisk){
			return histogramDoc;
		}else{
			if (histogramDocSize <= Constants.MAXREGISTERTOMEMORY){
				return histogramDoc;
			}else{
				return loadHistogramDoc(0);
			}
		}
	}
	
	/**
	 * Get TermsInDocumentFields (all terms information) List data
	 * @return List
	 * @throws Exception
	 */
	
	public List<TermsInDocumentFields> getHistogramDocFields() throws Exception{
		
		if (!this.setDataToDisk){
			return histogramDocumentFields;
		}else{
			if (histogramDocumentFieldsSize <= Constants.MAXREGISTERTOMEMORY){
				return histogramDocumentFields;
			}else{
				return loadHistogramDocField(0);
			}
		}
	}
	
	/**
	 * Get TermsInDocumentsFieldsCount (number of findings of a term in a file) List Data
	 * @return List
	 * @throws Exception
	 */
	public List<TermsInDocumentFieldsCounts> getHistogramDocFieldsCounts() throws Exception{
		if (!this.setDataToDisk){
			return histogramDocumentFieldsCounts;
		}else{
			if (histogramDocumentFieldsSize <= Constants.MAXREGISTERTOMEMORY){
				return histogramDocumentFieldsCounts;
			}else{
				return loadHistogramDocumentFieldsCounts(0);
			}
		}
	}
	
	

	/**
	 * Get the list of terms by word and files
	 * @param _histogramDocumentFields
	 * @param word
	 * @param file
	 * @return
	 * @throws Exception
	 */
	
	
	public List<TermsInDocumentFields> getHistogramDocFieldToFile(List<TermsInDocumentFields> _histogramDocumentFields, String word, int file) throws Exception{
		
		List<TermsInDocumentFields> dataFound = new ArrayList<TermsInDocumentFields>();
		
		dataFound = _histogramDocumentFields
					.stream()
					.filter(x->(word.equalsIgnoreCase(x.getTerm()) && (file == x.getFileId())))
					.collect(Collectors.toList());
			
		return dataFound;
	}
	
	/**
	 * Get the list of terms by word, files and position.
	 * @param _histogramDocumentFields
	 * @param word
	 * @param file
	 * @param pos
	 * @return
	 * @throws Exception
	 */
	
	
	public List<TermsInDocumentFields> getHistogramDocFieldToFile(List<TermsInDocumentFields> _histogramDocumentFields, String word, int file, long pos) throws Exception{
		
		List<TermsInDocumentFields> dataFound = new ArrayList<TermsInDocumentFields>();
		
		dataFound = _histogramDocumentFields
					.stream()
					.filter(x->(word.equalsIgnoreCase(x.getTerm()) && (file == x.getFileId()) && (pos == x.getIndexByFile())))
					.collect(Collectors.toList());
			
		return dataFound;
	}
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//SCORING
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Add Scores to Collection.
	 * @param sD
	 * @throws Exception
	 */
	public void addScoringToModel(ScoringData sD) throws Exception{
		this.scores.add(sD);
	}
	
	/**
	 * Set PercentData to Scores.
	 * @param fileNameId
	 * @param percent
	 * @throws Exception
	 */
	
	
	public void setScoringDataToModel(int fileNameId, BigDecimal percent) throws Exception{
		
		final ScoringData sC = scores.stream().filter(x -> (x.getIdFileName() == fileNameId)).findAny().orElse(null);
		if (sC == null){
			
			ScoringData sD = new ScoringData("",fileNameId,percent);
			addScoringToModel(sD);
			
		}else{
			scores
			.stream()
			.filter(x -> (x.getIdFileName() == fileNameId))
			.findFirst()
			.ifPresent(x -> x.setPercentaje(percent));
		}
	}
	
	/**
	 * Return ScoringData 
	 * @return
	 */
	public List<ScoringData> getScores(){
		scores = (List<ScoringData>)scores.stream().sorted(Collections.reverseOrder()).collect(Collectors.toList());
		return scores;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////	
}

