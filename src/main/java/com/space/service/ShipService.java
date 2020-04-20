package com.space.service;

import com.space.model.Ship;

import java.util.List;

public interface ShipService {
    List<Ship> getAllShips(ShipDTO shipDTO);
    Ship getShipById(Long id);
    Ship updateShip(Long id, Ship ship);
    Ship addShip(Ship ship);
    void removeShip(Long id);
}
