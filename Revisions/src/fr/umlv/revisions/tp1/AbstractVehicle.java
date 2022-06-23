package fr.umlv.revisions.tp1;

import java.util.Objects;

abstract class AbstractVehicle implements Vehicle {
    private final int year;

    public int requirePositiveYear(int year) {
        if (year < 0) {
            throw new IllegalArgumentException("Year must be positive");
        }
        return year;
    }

    public AbstractVehicle(int year) {
        this.year = year;
    }

    @Override
    public int getYear() {
        return year;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractVehicle that = (AbstractVehicle) o;
        return year == that.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year);
    }
}
