package es.gromenauer.model;

import java.util.ArrayList;

import es.gromenauer.utils.StringUtils;

/**
 * Class Model: Number of findings of a term by file 
 * @author Usuario
 *
 */

public class TermsInDocumentFieldsCounts implements Comparable<TermsInDocumentFieldsCounts>{
	
	private String Term;
	private ArrayList<Coordinates> countsByTerm;
	
	public TermsInDocumentFieldsCounts(){
		Term = "";
		countsByTerm = new ArrayList<Coordinates>();
	}
	
	public TermsInDocumentFieldsCounts(String _data){
		Term = "";
		countsByTerm = new ArrayList<Coordinates>();
		this.stringTo(_data);
	}
	
	public TermsInDocumentFieldsCounts(String _term, long file){
		Term = _term;
		countsByTerm = new ArrayList<Coordinates>();
		countsByTerm.add(new Coordinates(file,1L));
	}
	
	public String getTerm() {
		return Term;
	}


	public void setTerm(String term) {
		Term = term;
	}

	public ArrayList<Coordinates> getCountsByTerm() {
		return countsByTerm;
	}

	public void setCountsByTerm(ArrayList<Coordinates> documents) {
		this.countsByTerm = documents;
	}
	
	public void mergeCountsByTerm(ArrayList<Coordinates> documents){
		for(Coordinates coord:documents){
			addCounts(coord.getInfLimit());
		}
	}
	
	
	public void addCounts(Long file){
		Coordinates data = countsByTerm.stream().filter(x->x.getInfLimit()==file).findAny().orElse(null);
		if(data!=null){
			countsByTerm
			.stream()
			.filter(x -> x.getInfLimit() == data.getInfLimit())
			.findFirst()
			.ifPresent(x -> x.setSupLimit(data.getSupLimit()+1));
		}else{
			countsByTerm.add(new Coordinates(file,1L));
		}
	}
	
	public String toStringFormatted(int maxSize){
		String out = "";
		out = String.format("%-50s",Term.trim());
		for(Coordinates coord: countsByTerm){
			out += "," + String.format("%05d", coord.getInfLimit()) + "," + String.format("%05d",coord.getSupLimit());
		}
		
		if (countsByTerm.size() <  maxSize){
			int padd = maxSize - countsByTerm.size();
			for(int j=0; j<padd; j++){
				out += ",00000,00000";
			}
		}
		out += "\r\n";
		
		return out;
	}
	
	public void stringTo(String data){
		String attrib[] = data.split(",");
		this.Term = attrib[0].trim();
		countsByTerm.clear();
		
		for(int i=2; i<attrib.length; i=i+2){
			Long y = StringUtils.valLong(attrib[i]);
			Long x = StringUtils.valLong(attrib[i-1]);
			
			if ((y != 0) && (x != 0)){
				Coordinates coord = new Coordinates(x,y);
				countsByTerm.add(coord);
			}
		}
	}
	
	@Override
	public int compareTo(TermsInDocumentFieldsCounts arg0) {
		return Term.compareTo(arg0.getTerm());
	}
	
	
    @Override
    public boolean equals(final Object obj) {
      if (obj == null) return false;
      else if (!(obj instanceof TermsInDocumentFieldsCounts)) return false;
      else{
    	  TermsInDocumentFieldsCounts tIDFC = (TermsInDocumentFieldsCounts)obj;
    	  return Term.equalsIgnoreCase(tIDFC.getTerm());
      }
    }
	
	
	
	

}
