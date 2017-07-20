package es.gromenauer.model;

/**
 * Coordinate Class. 
 * position in file of the term searched. (posInf; posSup)
 * count of terms by files.
 * @author Usuario
 */

public class Coordinates {
	

	private long infLimit;
	private long supLimit;
	
	public Coordinates(){
		infLimit = 0;
		supLimit = 0;
	}
	
	public Coordinates(long inf, long sup){
		infLimit = inf;
		supLimit = sup;
	}
	
	
	public Coordinates(Coordinates copy){
		infLimit = copy.getInfLimit();
		supLimit = copy.getSupLimit();
	}
	
	public long getInfLimit() {
		return infLimit;
	}

	public void setInfLimit(long infLimit) {
		this.infLimit = infLimit;
	}

	public long getSupLimit() {
		return supLimit;
	}

	public void setSupLimit(long supLimit) {
		this.supLimit = supLimit;
	}

	
	public void clear(){
		infLimit = 0;
		supLimit = 0;
	}
	
	public boolean equals(Object obj){
		if (obj == null) return false;
		else if (!(obj instanceof Coordinates)) return false;
		else{
			Coordinates coord_aux = (Coordinates)obj;
			return ((this.infLimit == coord_aux.getInfLimit()) && (this.supLimit == coord_aux.getSupLimit()));
		}
	}

}
