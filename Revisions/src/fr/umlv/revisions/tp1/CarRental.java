package fr.umlv.revisions.tp1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CarRental {
    private final ArrayList<Vehicle> vehicleArrayList;

    public CarRental() {
        this.vehicleArrayList = new ArrayList<>();
    }

    public void add(Vehicle vehicle) {
        this.vehicleArrayList.add(Objects.requireNonNull(vehicle));
    }

    public void remove(Vehicle vehicle) {
        Objects.requireNonNull(vehicle);
        if (vehicleArrayList.isEmpty()) {
            throw new IllegalStateException("rental list is empty !");
        }
        if (vehicleArrayList.contains(vehicle)) {
            vehicleArrayList.remove(Objects.requireNonNull(vehicle));
        }
    }

    public List<Vehicle> findAllByYear(int year) {
        return this.vehicleArrayList
                .stream()
                .filter(vehicle -> vehicle.getYear() == year)
                .collect(Collectors.toList());
    }

    public Optional<Car> findACarByModel(String model) {
        Objects.requireNonNull(model);
        var carListByModel = vehicleArrayList
                .stream()
                .filter(x -> x instanceof Car && ((Car) x).getModel().equals(model))
                .toList();
        if (!carListByModel.isEmpty()) {
            return Optional.of((Car) carListByModel.get(0));
        } else {
            return Optional.empty();
        }
    }

    public int insuranceCostAt(int year) {
        return vehicleArrayList
                .stream()
                .mapToInt(vehicle -> vehicle.calculatePrice(year))
                .sum();
    }

    @Override
    public String toString() {
        return vehicleArrayList
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }
}
