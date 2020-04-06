package fr.umlv.rental;


abstract class AbstractVehicle implements Vehicle {
	private final int year;

	/**
	 * test if year is negative
	 * @param year
	 * @return
	 */
	public int requirePositiveYear(int year) {
		if(year < 0) {
			throw new IllegalArgumentException("year must be positive");
		}
		return year;
	}
	
	/**
	 * @param year
	 */
	public AbstractVehicle(int year) {
		super();
		this.year = this.requirePositiveYear(year);
	}
	
	@Override
	public int getYear() {
		return year;
	}

	@Override 
	public boolean equals(Object o) {
		if(!(o instanceof AbstractVehicle)) {
			return false;
		}
		AbstractVehicle abs = (AbstractVehicle)o;
		return  year == abs.year ;
	}
	
	@Override
	public int hashCode() {
		return  year;
	}
	
}
