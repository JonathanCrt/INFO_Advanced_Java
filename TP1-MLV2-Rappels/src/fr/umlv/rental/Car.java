package fr.umlv.rental;

import java.util.Objects;


public class Car extends AbstractVehicle {
	private final String model;

	/**
	 * @param model
	 * @param yearBuild
	 */
	public Car(String model, int year) {
		super(year);
		this.model = Objects.requireNonNull(model);
	}

	@Override
	public String toString() {
		return model + " " + this.getYear();
	}
	
	/**
	 * return model
	 * @return
	 */
	public String getModel() {
		return model;
	}
	
	/**
	 * calculate price of insurance for cars
	 * @param year
	 * @return
	 */
	@Override
	public int calculatePrice(int year) {
		
		if(year < this.getYear()) {
			throw new IllegalArgumentException("year is invalid");
		}
		if(year - this.getYear() < 10) {
			return 200;
		}
		else {
			return 500;
		}
	}
	

	@Override
	public boolean equals(Object o) {
		// Verifie si l'objet o est du même type que Car
		if(!(o instanceof Car)){
			return false;
		}
		Car cr = (Car)o;
		// on délégue
		return super.equals(cr) && (this.model.equals(cr.model));
	}
	
	@Override 
	public int hashCode() {
		return  super.hashCode() + this.model.hashCode();
	}
	
	
	
}
