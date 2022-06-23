package fr.umlv.rental;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


public class CarRental {
	private ArrayList<Vehicle>rental; 


	public CarRental() {
		super();
		this.rental= new ArrayList<Vehicle>();
	}
	
	/**
	 * add a vehicle to rental arrayList
	 * @param vh
	 */
	public void add(Vehicle vh) {
		rental.add(Objects.requireNonNull(vh));
	}
	
	/**
	 * test if the object is into ArrayList, and remove.
	 * check is one vehicle object is not null
	 * @param car
	 * @return
	 */
	public void remove(Vehicle vh)  {
		
		boolean testContains = rental.contains(Objects.requireNonNull(vh));
		
		if(rental.isEmpty()) {
			throw new IllegalStateException("rental list is empty !");
		}
		
		if(testContains) {
			rental.remove(Objects.requireNonNull(vh));
		}
		
	}
	
	/**
	 * return a list of cars with same year of build
	 * @param year
	 * @return
	 */
	public List<Vehicle> findAllByYear(int year) {

		return rental.stream()
		.filter(x -> x.getYear() == year)
		.collect(Collectors.toList());
		
	}
	
	/**
	 * return a list of cars with same model
	 * @param model
	 * @return
	 */
	/*
	public List<Car> findACarByModel(String model) {
		Objects.requireNonNull(model);
		return cars.stream()
		.filter(x -> x.getModel().equals(model))
		.collect(Collectors.toList());
	}
	*/
	
	/**
	 * return a car with his model
	 * @param model
	 * @return
	 */
	public Optional<Car> findACarByModel(String model) {
		Objects.requireNonNull(model);
		List<Vehicle> car = rental
				.stream()
				.filter(x -> x instanceof Car && ((Car) x).getModel().equals(model))
				.toList();
		
		if(!car.isEmpty()) {
			return Optional.of((Car)car.get(0));
		}
		else {
			return Optional.empty();
		}
		
	}
	
	/**
	 * return price of insurance
	 * @param year
	 * @return
	 */
	public int insuranceCostAt(int year) {
		
		return rental.stream()
		.mapToInt((x) -> x.calculatePrice(year))
		.sum();
	}
	
	@Override
	public String toString() {
		//StringJoiner
		
		/*
		StringBuilder st  = new StringBuilder();
		
		for(Car car: this.cars) {
			st.append(car);
			st.append("\n");
		}
		if(!cars.isEmpty()) {
			
			st.substring(0, st.length()-1);
			return st.toString();
		}
		return "";
		*/
		
		// transform Car to String
		return rental.stream()
		.map(x -> x.toString())
		//.map(Car::toString())
		.collect(Collectors.joining("\n"));
	}
	
	
	
}
