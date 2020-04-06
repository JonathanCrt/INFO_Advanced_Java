package fr.umlv.rental;

public class Camel extends AbstractVehicle {
	
	public Camel(int year) {
		super(year);
	}

	@Override
	public String toString() {
		return "camel " + getYear() ;
	}
	

	/**
	 * calculate price of insurance for camels
	 * @param year
	 * @return
	 */
	@Override
	public int calculatePrice(int year) {
		if(year < this.getYear()) {
			throw new IllegalArgumentException("year is invalid");
		}
		return (year - this.getYear()) * 100;
	}
	

}
