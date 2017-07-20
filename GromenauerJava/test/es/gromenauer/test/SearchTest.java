package es.gromenauer.test;

import org.junit.Test;

import es.gromenauer.indexing.ProcessFiles;
import es.gromenauer.model.Coordinates;
import es.gromenauer.model.Model;
import es.gromenauer.model.ScoringData;
import es.gromenauer.scoring.Score;
import es.gromenauer.searching.Search;
import es.gromenauer.utils.FileUtils;
import es.gromenauer.utils.StringUtils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SearchTest {
	
	@Test
	public void testProcessingFileInputTest_1(){
		try{
			
			File dir = new File("./input_2");
			Model mod = new Model();
			mod.setSetDataToDisk(false);
			
			ProcessFiles pF = new ProcessFiles();
			
			File files[] = FileUtils.getListOfFile(dir);
			mod = pF.runProcessFiles(dir,files);
			
			Score scor = new Score();
			scor.Initialize(mod, dir);
			
			Search sch = new Search();
			sch.Initialize(mod, scor);
			
			String query = "prueba";
			
			sch.searchSentencesInFiles(query);
			
			List<ScoringData> data = mod.getScores();
			
			assertEquals("num findings term \""+query+"\" in \"" + data.get(0).getFileName() + "\"",(new BigDecimal(100.00)).longValue(),data.get(0).getPercentaje().longValue());
			assertEquals("num findings term \""+query+"\" in \"" + data.get(1).getFileName() + "\"",(new BigDecimal(100.00)).longValue(),data.get(1).getPercentaje().longValue());
			assertEquals("num findings term \""+query+"\" in \"" + data.get(2).getFileName() + "\"",(new BigDecimal(100.00)).longValue(),data.get(2).getPercentaje().longValue());
			assertEquals("num findings term \""+query+"\" in \"" + data.get(3).getFileName() + "\"",(new BigDecimal(100.00)).longValue(),data.get(3).getPercentaje().longValue());
			
			
			scor.clear();
			
		}catch(Exception e){
			
		}
	}	
		
	@Test
	public void testProcessingFileInputTest_2(){
			try{
				
				File dir = new File("./input_2");
				Model mod = new Model();
				mod.setSetDataToDisk(false);
				
				ProcessFiles pF = new ProcessFiles();
				
				File files[] = FileUtils.getListOfFile(dir);
				mod = pF.runProcessFiles(dir,files);
				
				Score scor = new Score();
				scor.Initialize(mod, dir);
				
				Search sch = new Search();
				sch.Initialize(mod, scor);
				
				String query = "esto es una prueba";
				
				sch.searchSentencesInFiles(query);
				
				List<ScoringData> data = mod.getScores();
				
				assertEquals("num findings term \""+query+"\" in \"" + data.get(0).getFileName() + "\"",(new BigDecimal(100.00)).longValue(),data.get(0).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(1).getFileName() + "\"",(new BigDecimal(100.00)).longValue(),data.get(1).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(2).getFileName() + "\"",(new BigDecimal(100.00)).longValue(),data.get(2).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(3).getFileName() + "\"",(new BigDecimal(100.00)).longValue(),data.get(3).getPercentaje().longValue());
				
				
				scor.clear();
				
			}catch(Exception e){
				
			}
	}
	
		
	@Test
	public void testProcessingFileInputTest_3(){
			try{
				
				File dir = new File("./input_2");
				Model mod = new Model();
				mod.setSetDataToDisk(false);
				
				ProcessFiles pF = new ProcessFiles();
				
				File files[] = FileUtils.getListOfFile(dir);
				mod = pF.runProcessFiles(dir,files);
				
				Score scor = new Score();
				scor.Initialize(mod, dir);
				
				Search sch = new Search();
				sch.Initialize(mod, scor);
				
				String query = "-1,200.200";
				
				sch.searchSentencesInFiles(query);
				
				List<ScoringData> data = mod.getScores();
				
				assertEquals("num findings term \""+query+"\" in \"" + data.get(0).getFileName() + "\"",(new BigDecimal(100.00)).longValue(),data.get(0).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(1).getFileName() + "\"",(new BigDecimal(0)).longValue(),data.get(1).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(2).getFileName() + "\"",(new BigDecimal(0)).longValue(),data.get(2).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(3).getFileName() + "\"",(new BigDecimal(0)).longValue(),data.get(3).getPercentaje().longValue());
				
				
				scor.clear();
				
			}catch(Exception e){
				
			}
	}	
	
	@Test
	public void testProcessingFileInputTest_4(){
			try{
				
				File dir = new File("./input_2");
				Model mod = new Model();
				mod.setSetDataToDisk(false);
				
				ProcessFiles pF = new ProcessFiles();
				
				File files[] = FileUtils.getListOfFile(dir);
				mod = pF.runProcessFiles(dir,files);
				
				Score scor = new Score();
				scor.Initialize(mod, dir);
				
				Search sch = new Search();
				sch.Initialize(mod, scor);
				
				String query = "prueba0";
				
				sch.searchSentencesInFiles(query);
				
				List<ScoringData> data = mod.getScores();
				
				assertEquals("num findings term \""+query+"\" in \"" + data.get(0).getFileName() + "\"",(new BigDecimal(100.00)).longValue(),data.get(0).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(1).getFileName() + "\"",(new BigDecimal(0)).longValue(),data.get(1).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(2).getFileName() + "\"",(new BigDecimal(0)).longValue(),data.get(2).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(3).getFileName() + "\"",(new BigDecimal(0)).longValue(),data.get(3).getPercentaje().longValue());
				
				
				scor.clear();
				
			}catch(Exception e){
				
			}
	}	
	
	
	@Test
	public void testProcessingFileInputTest_5(){
			try{
				
				File dir = new File("./input_2");
				Model mod = new Model();
				mod.setSetDataToDisk(false);
				
				ProcessFiles pF = new ProcessFiles();
				
				File files[] = FileUtils.getListOfFile(dir);
				mod = pF.runProcessFiles(dir,files);
				
				Score scor = new Score();
				scor.Initialize(mod, dir);
				
				Search sch = new Search();
				sch.Initialize(mod, scor);
				
				String query = "esto es una prueba y lo voy a demostrar esta prueba";
				
				sch.searchSentencesInFiles(query);
				
				List<ScoringData> data = mod.getScores();
				
				assertEquals("num findings term \""+query+"\" in \"" + data.get(0).getFileName() + "\"",(new BigDecimal(100.00)).longValue(),data.get(0).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(1).getFileName() + "\"",(new BigDecimal(100.00)).longValue(),data.get(1).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(2).getFileName() + "\"",(new BigDecimal(100.00)).longValue(),data.get(2).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(3).getFileName() + "\"",(new BigDecimal(80.00)).longValue(),data.get(3).getPercentaje().longValue());
				
				
				scor.clear();
				
			}catch(Exception e){
				
			}
	}	

	@Test
	public void testProcessingFileInputTest_6(){
			try{
				
				File dir = new File("./input_2");
				Model mod = new Model();
				mod.setSetDataToDisk(false);
				
				ProcessFiles pF = new ProcessFiles();
				
				File files[] = FileUtils.getListOfFile(dir);
				mod = pF.runProcessFiles(dir,files);
				
				Score scor = new Score();
				scor.Initialize(mod, dir);
				
				Search sch = new Search();
				sch.Initialize(mod, scor);
				
				String query = "busca quiero prueba1";
				
				sch.searchSentencesInFiles(query);
				
				List<ScoringData> data = mod.getScores();
				
				assertEquals("num findings term \""+query+"\" in \"" + data.get(0).getFileName() + "\"",(new BigDecimal(100.00)).longValue(),data.get(0).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(1).getFileName() + "\"",(new BigDecimal(66.00)).longValue(),data.get(1).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(2).getFileName() + "\"",(new BigDecimal(66.00)).longValue(),data.get(2).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(3).getFileName() + "\"",(new BigDecimal(66.00)).longValue(),data.get(3).getPercentaje().longValue());
				
				
				scor.clear();
				
			}catch(Exception e){
				
			}
	}	
	
	@Test
	public void testProcessingFileInputTest_7(){
			try{
				
				File dir = new File("./input_2");
				Model mod = new Model();
				mod.setSetDataToDisk(false);
				
				ProcessFiles pF = new ProcessFiles();
				
				File files[] = FileUtils.getListOfFile(dir);
				mod = pF.runProcessFiles(dir,files);
				
				Score scor = new Score();
				scor.Initialize(mod, dir);
				
				Search sch = new Search();
				sch.Initialize(mod, scor);
				
				String query = "pruebaX";
				
				sch.searchSentencesInFiles(query);
				
				List<ScoringData> data = mod.getScores();
				
				assertEquals("num findings term \""+query+"\" in \"" + data.get(0).getFileName() + "\"",(new BigDecimal(0.00)).longValue(),data.get(0).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(1).getFileName() + "\"",(new BigDecimal(0.00)).longValue(),data.get(1).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(2).getFileName() + "\"",(new BigDecimal(0.00)).longValue(),data.get(2).getPercentaje().longValue());
				assertEquals("num findings term \""+query+"\" in \"" + data.get(3).getFileName() + "\"",(new BigDecimal(0.00)).longValue(),data.get(3).getPercentaje().longValue());
				
				
				scor.clear();
				
			}catch(Exception e){
				
			}
	}	
	
	

}
