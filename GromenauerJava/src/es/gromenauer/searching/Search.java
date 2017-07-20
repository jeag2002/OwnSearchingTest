package es.gromenauer.searching;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import es.gromenauer.model.Model;
import es.gromenauer.model.ScoringData;
import es.gromenauer.model.TermsInDocumentFields;
import es.gromenauer.scoring.Score;
import es.gromenauer.utils.Constants;
import es.gromenauer.utils.StringUtils;
import es.gromenauer.utils.TFIDF;

/**
 * Second Step: Searching engine for sentences/words in all input files.
 * @author Usuario
 */

public class Search {
	
	Model pMod;
	Score sCor;
	private ArrayList<ArrayList<Float>> histogramData_1;
	private ArrayList<Float> histogramData;
	private ArrayList<Long> findings;
	private List<Long>numberOfTerm;
	
	public Search(){
		pMod = new Model();
		sCor = new Score();
		
		histogramData_1 = new ArrayList<ArrayList<Float>>();
		histogramData = new ArrayList<Float>();
		
		numberOfTerm = new ArrayList<Long>();
		
	}
	
	/**
	 * Inicialize search parameters.
	 * @param _pMod
	 * @param _sCor
	 */

	public void Initialize(Model _pMod, Score _sCor){
		pMod = _pMod;
		sCor = _sCor;
		
		histogramData_1 = new ArrayList<ArrayList<Float>>();
		histogramData = new ArrayList<Float>();
		
		numberOfTerm = pMod.getNumDataOfFiles();
		
		for(int i=0; i<numberOfTerm.size(); i++){
			histogramData.add(0.0f);
		}
		
		for(int i=0; i<numberOfTerm.size(); i++){
			histogramData_1.add(new ArrayList<Float>());
		}
	}
	
	/**
	 * Analysis of the sentence.
	 * @param sentence
	 * @throws Exception
	 */
	public void searchSentencesInFiles(String sentence) throws Exception{
		String queryData[] = StringUtils.sentenceToArrayOfString(sentence);
		List<Integer> documentList = new ArrayList<Integer>();
		
		if (queryData.length > 0){
			
			
			/*
			documentList = pMod.getHistogramDoc(queryData[0]);
			for(int i=1; i<queryData.length; i++){
				ArrayList<Integer> docAux = pMod.getHistogramDoc(queryData[i]);
				documentList = documentList
							   .stream()
							   .filter(e->docAux.stream().anyMatch(x -> x.equals(e)))
							   .collect(Collectors.toList());
			}*/
			
			documentList = pMod.getScores().stream().map(ScoringData::getIdFileName).collect(Collectors.toList());
			for(Integer file: documentList){
				float res = getIndex(file,queryData);
				float index = (float)(res/(float)queryData.length);
				
				BigDecimal indexBD = new BigDecimal(index);
				indexBD = indexBD.multiply(new BigDecimal(100)).setScale(2, RoundingMode.CEILING);
				pMod.setScoringDataToModel(file, indexBD);
			}
			
			//setScoringValues();
		}
	}
	
	
	/**
	 * Set Scoring Values
	 * @throws Exception
	 */
	/*
	public void setScoringValues(String queryData[]) throws Exception{
		
		Integer numFiles = numberOfTerm.size();
		Integer sizeData = queryData.length;
		
		List<Long> data = histogramData.stream().filter(x -> x != 0L).collect(Collectors.toList());
		Integer numFilesWhereExistChar = data.size();
		
		double IDF = (double)(numFiles.doubleValue()/numFilesWhereExistChar.doubleValue());
		for(int i=0; i<numberOfTerm.size(); i++){
			double TF = (double)(histogramData.get(i+1).doubleValue()/(numberOfTerm.get(i).doubleValue() - sizeData.doubleValue()));
			pMod.setScoringDataToModel(i+1, TFIDF.rangeTFIDF(TF, IDF));
		}
	}
	*/

	
	/**
	 * Analysis of the query sentence
	 * @param file
	 * @param queryData
	 * @param index
	 * @return
	 */
	
	public float getIndex(Integer file, String queryData[]) throws Exception{
		float res = 0;
		
		List<TermsInDocumentFields> data = pMod.getHistogramDocFieldToFile(pMod.getHistogramDocFields(), queryData[0], file);
		
		for(TermsInDocumentFields tIDF: data){
			float res_2 = 1 + getIndexRec(file, tIDF.getIndexByFile()+1, queryData, 1, queryData.length);
			if (res_2 > res){res = res_2;}
			
			/*
			ArrayList<Float> list = histogramData_1.get(file-1);
			list.add(res_2);
			histogramData_1.set(file-1,list);
			*/ 
			
		}
		
		//looking for data from files; using cache engine
		
		if ((res <= 1) && (pMod.isSetDataToDisk())){
			float res_1 = 0;
			if (pMod.getHistogramDocumentFieldsSize() > Constants.MAXREGISTERTOMEMORY){
				for(long i=0; i<pMod.getHistogramDocumentFieldsSize(); i=i+Constants.MAXREGISTERTOMEMORY){				
					data = pMod.getHistogramDocFieldToFile(pMod.loadHistogramDocField(i), queryData[0], file);
					for(TermsInDocumentFields tIDF: data){
						float res_2 = 1 + getIndexRec(file, tIDF.getIndexByFile()+1, queryData, 1, queryData.length);
						if (res_2 > res_1){res_1 = res_2;}
						
						/*
						ArrayList<Float> list = histogramData_1.get(file-1);
						list.add(res_2);
						histogramData_1.set(file-1,list);
						*/
						
					}
				}
			}
			if (res_1 >= res){res = res_1;}
		}
		
		return res;
	}
	
	/**
	 * Analysis of every term that contains the search.
	 * @param file
	 * @param pos
	 * @param queryData
	 * @param index
	 * @param queryLong
	 * @return
	 * @throws Exception
	 */
	
	public float getIndexRec(Integer file, Long pos, String queryData[], int index, int queryLong) throws Exception{
		float res = 0;
		
		if (index == queryLong){
			return 0;
		}else{
			List<TermsInDocumentFields> data = pMod.getHistogramDocFieldToFile(pMod.getHistogramDocFields(), queryData[index], file, pos);
			for(TermsInDocumentFields tIDF: data){
				float res_2 = 1+getIndexRec(file, tIDF.getIndexByFile()+1, queryData, index+1, queryLong);
				if (res_2 > res){res = res_2;}
			}
			
			//looking for data from files; using cache engine
			if ((res <= 1) && (pMod.isSetDataToDisk())){
				float res_1 = 0;
				if (pMod.getHistogramDocumentFieldsSize() > Constants.MAXREGISTERTOMEMORY){
					for(long i=0; i<pMod.getHistogramDocumentFieldsSize(); i=i+Constants.MAXREGISTERTOMEMORY){				
						data = pMod.getHistogramDocFieldToFile(pMod.loadHistogramDocField(i), queryData[0], file);
						for(TermsInDocumentFields tIDF: data){
							float res_2 = 1 + getIndexRec(file, tIDF.getIndexByFile()+1, queryData, 1, queryData.length);
							if (res_2 > res_1){res_1 = res_2;}
						}
					}
				}
				if (res_1 >= res){res = res_1;}
			}
			
			return res;
		}
	}
	
}
