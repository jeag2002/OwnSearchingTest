package es.gromenauer.model;

import es.gromenauer.utils.StringUtils;

/**
 * Class Model: Complete information of a term
 * @author Usuario
 *
 */

public class TermsInDocumentFields implements Comparable<TermsInDocumentFields> {
	

	private String Term;
	private Integer FileId;
	private Long IndexByFile;
	private Coordinates RealPosition;
	
	
	public TermsInDocumentFields(){
		Term = "";
		FileId = 0;
		IndexByFile = 0L;
		RealPosition = new Coordinates();
	}
	
	public TermsInDocumentFields(String _data){
		Term = "";
		FileId = 0;
		IndexByFile = 0L;
		RealPosition = new Coordinates();
		this.stringTo(_data);
	}
	
	
	public TermsInDocumentFields(String _term, Integer _fileId, Long _indexByFile, Coordinates _realPosition){
		Term = _term;
		FileId = _fileId;
		IndexByFile = _indexByFile;
		RealPosition = new Coordinates(_realPosition);
	}
	
	public String getTerm() {
		return Term;
	}

	public void setTerm(String term) {
		Term = term;
	}
	
	public Integer getFileId() {
		return FileId;
	}

	public void setFileId(Integer fileId) {
		FileId = fileId;
	}
	
	public Long getIndexByFile() {
		return IndexByFile;
	}
	
	public void setIndexByFile(Long indexByFile) {
		IndexByFile = indexByFile;
	}

	public Coordinates getRealPosition() {
		return RealPosition;
	}

	public void setRealPosition(Coordinates realPosition) {
		RealPosition = realPosition;
	}

	@Override
	public int compareTo(TermsInDocumentFields arg0) {
		return Term.compareTo(arg0.getTerm());
	}
	
	
    @Override
    public boolean equals(final Object obj) {
      if (obj == null) return false;
      else if (!(obj instanceof TermsInDocumentFields)) return false;
      else{
    	  TermsInDocumentFields tIDF = (TermsInDocumentFields)obj;
    	  return Term.equalsIgnoreCase(tIDF.getTerm());
      }
    }
	
	public void clear(){
		Term = "";
		FileId = 0;
		IndexByFile = 0L;
		RealPosition = new Coordinates();
	}
	
	public String toString(){
		String out = "";
		out = this.Term+","+this.FileId.toString()+","+IndexByFile.toString()+","+RealPosition.getInfLimit()+","+RealPosition.getSupLimit();
		return out;
	}
	
	public void stringTo(String data){
		
		String termInfo[] = data.split(",");
		this.Term = termInfo[0];
		this.IndexByFile = StringUtils.valLong(termInfo[1]);
		Long x = StringUtils.valLong(termInfo[2]);
		Long y = StringUtils.valLong(termInfo[3]);
		RealPosition = new Coordinates(x,y);
	}




}
