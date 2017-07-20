package es.gromenauer.test;

import org.junit.Test;

import es.gromenauer.indexing.ProcessFiles;
import es.gromenauer.model.Coordinates;
import es.gromenauer.model.Model;
import es.gromenauer.utils.StringUtils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;

public class ProcessFilesTest {
	
	@Test
	public void testProcessingFileInputTest(){
		try{
			
			File fil = new File("./input_2/prueba0.txt");
			Model mod = new Model();
			mod.setSetDataToDisk(false);
			ProcessFiles pF = new ProcessFiles();
			pF.processFile(fil, 1, mod);
			
			ArrayList<Coordinates> data = mod.getHistogramDocFieldsCounts("prueba");
			assertEquals("size of Coordinates after processing prueba0.txt",1,data.size());
			Coordinates dataPrueba = data.get(0);
			assertEquals("num findings term \"prueba\"", 2, dataPrueba.getSupLimit());
			
			
			data = mod.getHistogramDocFieldsCounts("prueba0");
			assertEquals("size of Coordinates after processing prueba0.txt",1,data.size());
			dataPrueba = data.get(0);
			assertEquals("num findings term \"prueba0\"", 1, dataPrueba.getSupLimit());
			
			
			String inputDataNum = "-1,200.200";
			String inputDataNumNorm = StringUtils.normalizeNumerics(inputDataNum);
			
			data = mod.getHistogramDocFieldsCounts(inputDataNumNorm);
			assertEquals("size of Coordinates after processing prueba0.txt",1,data.size());
			dataPrueba = data.get(0);
			assertEquals("num findings term \""+inputDataNum+"\"", 1, dataPrueba.getSupLimit());
			
			data = mod.getHistogramDocFieldsCounts("pruebaX");
			assertEquals("size of Coordinates after processing prueba0.txt word that doesnt exist pruebaX",0,data.size());
			
			
		}catch(Exception e){
			
		}
	}
	
	@Test
	public void testProcessingLineInputTest_1(){
		try{
			Model mod = new Model();
			mod.setSetDataToDisk(false);
			ProcessFiles pF = new ProcessFiles();
			
			String inputData="esto es una prueba y lo voy a demostrar esta prueba. si quieres 100% en este fichero busca quiero prueba0 1.200,200 -1,200.200 1.200.200,200";
			pF.processLine(inputData, 1, mod);
			
			ArrayList<Coordinates> data = mod.getHistogramDocFieldsCounts("prueba");
			assertEquals("size of Coordinates after processing sentence (" + inputData + ")",1,data.size());
			Coordinates dataPrueba = data.get(0);
			assertEquals("num findings term \"prueba\"", 2, dataPrueba.getSupLimit());
			
			
			data = mod.getHistogramDocFieldsCounts("prueba0");
			assertEquals("size of Coordinates after processing sentence (" + inputData + ")",1,data.size());
			dataPrueba = data.get(0);
			assertEquals("num findings term \"prueba0\"", 1, dataPrueba.getSupLimit());
			
			
			String inputDataNum = "-1,200.200";
			String inputDataNumNorm = StringUtils.normalizeNumerics(inputDataNum);
			
			data = mod.getHistogramDocFieldsCounts(inputDataNumNorm);
			assertEquals("size of Coordinates after processing sentence (" + inputData + ")",1,data.size());
			dataPrueba = data.get(0);
			assertEquals("num findings term \""+inputDataNum+"\"", 1, dataPrueba.getSupLimit());

			data = mod.getHistogramDocFieldsCounts("pruebaX");
			assertEquals("size of Coordinates after processing prueba0.txt word that doesnt exist pruebaX",0,data.size());
			
		}catch(Exception e){
			
		}
	}
	
	
	

}
