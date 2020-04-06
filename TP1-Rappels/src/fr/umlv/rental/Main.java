package fr.umlv.rental;

public class Main {
	public static void main(String[] args) {
		Car mustang = new Car("ford mustang", 2014);
		Car ds4 = new Car("citroen ds4", 2016);

		System.out.println(mustang);
		
		CarRental cars = new CarRental();
		
		cars.add(mustang);
		cars.add(ds4);
		System.out.println(cars.toString());

		
		var rental = new CarRental();
		rental.add(new Car("ford mustang", 2014));
		rental.add(new Camel(2010));
		
		System.out.println(rental.findAllByYear(2002));
		
		
		
		
	}
}
