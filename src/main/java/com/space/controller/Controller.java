package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipDTO;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class Controller {

    @Autowired
    private ShipService shipService;

    @GetMapping("/rest/ships")
    public List<Ship> getShips(@RequestParam(name = "name")Optional<String> name,
                               @RequestParam(name = "planet") Optional<String> planet,
                               @RequestParam(name = "shipType") Optional<ShipType> shipType,
                               @RequestParam(name = "after") Optional<Long> after,
                               @RequestParam(name = "before") Optional<Long> before,
                               @RequestParam(name = "isUsed") Optional<Boolean> isUsed,
                               @RequestParam(name = "minSpeed") Optional<Double> minSpeed,
                               @RequestParam(name = "maxSpeed") Optional<Double> maxSpeed,
                               @RequestParam(name = "minCrewSize") Optional<Integer> minCrewSize,
                               @RequestParam(name = "maxCrewSize") Optional<Integer> maxCrewSize,
                               @RequestParam(name = "minRating") Optional<Double> minRating,
                               @RequestParam(name = "maxRating") Optional<Double> maxRating,
                               @RequestParam(name = "order", defaultValue = "ID") Optional<String> order,
                               @RequestParam(name = "pageNumber", defaultValue = "0") Optional<Integer> pageNumber,
                               @RequestParam(name = "pageSize", defaultValue = "3") Optional<Integer> pageSize){

        ShipDTO shipDTO = new ShipDTO(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating, order, pageNumber, pageSize);

        return shipService.getAllShips(shipDTO);
    }

    @GetMapping("/rest/ships/count")
    public Long getShipsCount(@RequestParam(value = "name") Optional<String> name,
                              @RequestParam(value = "planet") Optional<String> planet,
                              @RequestParam(value = "shipType") Optional<ShipType> shipType,
                              @RequestParam(value = "after") Optional<Long> after,
                              @RequestParam(value = "before") Optional<Long> before,
                              @RequestParam(value = "isUsed") Optional<Boolean> isUsed,
                              @RequestParam(value = "minSpeed") Optional<Double> minSpeed,
                              @RequestParam(value = "maxSpeed") Optional<Double> maxSpeed,
                              @RequestParam(value = "minCrewSize") Optional<Integer> minCrewSize,
                              @RequestParam(value = "maxCrewSize") Optional<Integer> maxCrewSize,
                              @RequestParam(value = "minRating") Optional<Double> minRating,
                              @RequestParam(value = "maxRating") Optional<Double> maxRating){

        Optional<String> order = Optional.of("");
        Optional<Integer> pageNumber = Optional.of(Integer.MAX_VALUE);
        Optional<Integer> pageSize = Optional.of(Integer.MAX_VALUE);

        ShipDTO shipDTO = new ShipDTO(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating, order, pageNumber, pageSize);

        return (long) shipService.getAllShips(shipDTO).size();
    }

    @PostMapping("/rest/ships")
    public Ship addShip(@RequestBody Ship ship){
        return shipService.addShip(ship);
    }

    @PostMapping("/rest/ships/{id}")
    public Ship updateShip(@PathVariable Long id, @RequestBody Ship ship){
        return shipService.updateShip(id, ship);
    }

    @DeleteMapping("/rest/ships/{id}")
    public void removeShip(@PathVariable Long id){
        shipService.removeShip(id);
    }

    @GetMapping("/rest/ships/{id}")
    public Ship getShipById(@PathVariable Long id) {
        return shipService.getShipById(id);
    }
}
