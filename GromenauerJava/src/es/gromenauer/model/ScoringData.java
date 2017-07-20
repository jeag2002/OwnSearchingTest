package es.gromenauer.model;

import java.math.BigDecimal;

/**
 * Scoring Data
 * @author Usuario
 *
 */

public class ScoringData implements Comparable<ScoringData> {

	String fileName;
	Integer idFileName;
	BigDecimal percentaje;
	
	public ScoringData(){
		fileName = "";
		idFileName = 0;
		percentaje = BigDecimal.ZERO;
	}
	
	public ScoringData(String _fileName, Integer _idFileName, BigDecimal _percentaje){
		fileName = _fileName;
		idFileName = _idFileName;
		percentaje = _percentaje;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getIdFileName() {
		return idFileName;
	}

	public void setIdFileName(Integer idFileName) {
		this.idFileName = idFileName;
	}

	public BigDecimal getPercentaje() {
		return percentaje;
	}

	public void setPercentaje(BigDecimal percentaje) {
		this.percentaje = percentaje;
	}
	
	public String toString(){
		return fileName + " (" + idFileName + ") :" + percentaje.toString() + "%";
	}
	
	@Override
	public int compareTo(ScoringData arg0) {
		return percentaje.compareTo(arg0.getPercentaje());
	}
	
	

}
