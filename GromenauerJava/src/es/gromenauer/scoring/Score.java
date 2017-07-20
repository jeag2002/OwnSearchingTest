package es.gromenauer.scoring;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import es.gromenauer.model.Model;
import es.gromenauer.model.ScoringData;
import es.gromenauer.utils.Constants;
import es.gromenauer.utils.FileUtils;

/**
 * Third Step scoring results.
 * @author Usuario
 */

public class Score {
	
	private Model pMod;
	
	public Score(){
		pMod = new Model();
	}
	
	/**
	 * Initializing Scoring parameters
	 * @param _pMod
	 * @param _dir
	 * @throws Exception
	 */
	
	public void Initialize(Model _pMod, File _dir) throws Exception{
		pMod = _pMod;
		File[] files = FileUtils.getListOfFile(_dir);
		
		for(int i=0; i<files.length; i++){
			File fil = files[i];
			ScoringData sD = new ScoringData(fil.getName(),i+1,BigDecimal.ZERO);
			pMod.addScoringToModel(sD);
		}
	}
	
	/**
	 * Print Results 
	 * @throws Exception
	 */
	public void printResults() throws Exception{
		
		List<ScoringData> lSD = pMod.getScores();
		
		for(int i=0; (i<lSD.size() && i<Constants.MAXRESULTS); i++){
			System.out.println(lSD.get(i).toString());
		}
	}
	
	/**
	 * Clear results.
	 * @throws Exception
	 */
	
	public void clear() throws Exception{
		
		List<ScoringData> lSD = pMod.getScores();
		
		for(int i=0; i<lSD.size(); i++){
			ScoringData sC = lSD.get(i);
			pMod.setScoringDataToModel(sC.getIdFileName(), BigDecimal.ZERO);
		}
		
	}
	
	

}
