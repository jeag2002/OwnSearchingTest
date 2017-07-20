package es.gromenauer.model;

import java.util.ArrayList;
import java.util.Optional;

import es.gromenauer.utils.StringUtils;

/**
 * Class TermsInDocument. In which Files we can find the word "Term"
 * @author Usuario
 */

public class TermsInDocument implements Comparable<TermsInDocument> {
	
	//Term
	private String Term;
	
	//Files where is finded the word "term"
	private ArrayList<Integer> documents;
	
	
	public TermsInDocument(){
		Term = "";
		documents = new ArrayList<Integer>();
	}
	
	public TermsInDocument(String _data){
		Term = "";
		documents = new ArrayList<Integer>();
		this.stringTo(_data);
	}
	
	
	public TermsInDocument(String _term, int file){
		Term = _term;
		documents = new ArrayList<Integer>();
		documents.add(file);
	}


	public String getTerm() {
		return Term;
	}


	public void setTerm(String term) {
		Term = term;
	}

	public ArrayList<Integer> getDocuments() {
		return documents;
	}

	public void setDocuments(ArrayList<Integer> documents) {
		this.documents = documents;
	}
	
	
	public void addDocument(Integer docIndex){
		Optional<Integer> data = documents.stream().filter(x->x==docIndex).findAny();
		if(!data.isPresent()){
			documents.add(docIndex);
		}
	}
	
	public String toStringFormatted(int maxFiles){
		String out = "";
		out = String.format("%-50s", this.Term);
		for(Integer file:documents){out += "," + String.format("%05d", file);}
		
		if(documents.size() < maxFiles){
			int padding = maxFiles - documents.size();
			for(int j=0; j<padding; j++){
				out += ",00000";
			}
		}
		out += "\r\n";
		
		return out;
	}
	
	public void stringTo(String data){
		String attrib[] = data.split(",");
		this.Term = attrib[0].trim();
		documents.clear();
		for(int i=1; i<attrib.length; i++){
			int attribValue = StringUtils.val(attrib[i]);
			if (attribValue != 0){
				documents.add(StringUtils.val(attrib[i]));
			}
		}
	}
	
	@Override
	public int compareTo(TermsInDocument arg0) {
		return Term.compareTo(arg0.getTerm());
	}
	
	
    @Override
    public boolean equals(final Object obj) {
      if (obj == null) return false;
      else if (!(obj instanceof TermsInDocument)) return false;
      else{
    	  TermsInDocument tID = (TermsInDocument)obj;
    	  return Term.equalsIgnoreCase(tID.getTerm());
      }
    }
    
	public void mergeDocuments(ArrayList<Integer> documents){
		for(Integer document:documents){
			addDocument(document);
		}
	}
	
	
}
