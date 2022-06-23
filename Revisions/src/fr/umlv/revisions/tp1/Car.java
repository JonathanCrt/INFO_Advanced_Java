package fr.umlv.revisions.tp1;

import java.util.Objects;

public class Car extends AbstractVehicle {
    private final String model;

    public Car(String model, int yearDistribution) {
        super(yearDistribution);
        this.model = Objects.requireNonNull(model);
    }

    public String getModel() {
        return model;
    }

    @Override
    public int calculatePrice(int year) {
        if(year < this.getYear()) {
            throw new IllegalArgumentException("year is invalid");
        }
        if(year - this.getYear() < 10) {
            return 200;
        } else {
            return 500;
        }
    }


    @Override
    public String toString() {
        return this.model + " " + this.getYear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Car car = (Car) o;
        return Objects.equals(model, car.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), model);
    }
}
