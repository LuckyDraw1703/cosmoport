package com.space.service;

import com.space.model.ShipType;

import java.util.Optional;

public class ShipDTO {
    private Optional<String> name;
    private Optional<String> planet;
    private Optional<ShipType> shipType;
    private Optional<Long> after;
    private Optional<Long> before;
    private Optional<Boolean> isUsed;
    private Optional<Double> minSpeed;
    private Optional<Double> maxSpeed;
    private Optional<Integer> minCrewSize;
    private Optional<Integer> maxCrewSize;
    private Optional<Double> minRating;
    private Optional<Double> maxRating;
    private Optional<String> order;
    private Optional<Integer> pageNumber;
    private Optional<Integer> pageSize;

    public ShipDTO(Optional<String> name, Optional<String> planet, Optional<ShipType> shipType, Optional<Long> after,
                   Optional<Long> before, Optional<Boolean> isUsed, Optional<Double> minSpeed, Optional<Double> maxSpeed,
                   Optional<Integer> minCrewSize, Optional<Integer> maxCrewSize, Optional<Double> minRating,
                   Optional<Double> maxRating, Optional<String> order, Optional<Integer> pageNumber, Optional<Integer> pageSize) {
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.after = after;
        this.before = before;
        this.isUsed = isUsed;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.minCrewSize = minCrewSize;
        this.maxCrewSize = maxCrewSize;
        this.minRating = minRating;
        this.maxRating = maxRating;
        this.order = order;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getPlanet() {
        return planet;
    }

    public Optional<ShipType> getShipType() {
        return shipType;
    }

    public Optional<Long> getAfter() {
        return after;
    }

    public Optional<Long> getBefore() {
        return before;
    }

    public Optional<Boolean> isUsed() {
        return isUsed;
    }

    public Optional<Double> getMinSpeed() {
        return minSpeed;
    }

    public Optional<Double> getMaxSpeed() {
        return maxSpeed;
    }

    public Optional<Integer> getMinCrewSize() {
        return minCrewSize;
    }

    public Optional<Integer> getMaxCrewSize() {
        return maxCrewSize;
    }

    public Optional<Double> getMinRating() {
        return minRating;
    }

    public Optional<Double> getMaxRating() {
        return maxRating;
    }

    public Optional<String> getOrder() {
        return order;
    }

    public Optional<Integer> getPageNumber() {
        return pageNumber;
    }

    public Optional<Integer> getPageSize() {
        return pageSize;
    }

}
