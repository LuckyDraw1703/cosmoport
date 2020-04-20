package com.space.service;

import com.space.controller.ShipOrder;
import com.space.exceptions.ShipBadRequestException;
import com.space.exceptions.ShipNotFoundException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
@Transactional
public class ShipServiceImpl implements ShipService{

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ShipRepository repository;


    @Override
    public List<Ship> getAllShips(ShipDTO shipDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ship> shipCriteria = cb.createQuery(Ship.class);
        Root<Ship> rootShip = shipCriteria.from(Ship.class);
        shipCriteria.select(rootShip);
        Predicate criteria = cb.conjunction();

        //check by name
        if(shipDTO.getName().isPresent() && isValidString(shipDTO.getName().get())){
            Predicate p = cb.like(rootShip.get("name"), "%" + shipDTO.getName().get() + "%");
            criteria = cb.and(criteria, p);
        }


        //check by planet
        if(shipDTO.getPlanet().isPresent() && isValidString(shipDTO.getPlanet().get())){
            Predicate p = cb.like(rootShip.get("planet"), "%" + shipDTO.getPlanet().get() + "%");
            criteria = cb.and(criteria, p);
        }


        //check by date
        if(shipDTO.getAfter().isPresent() && isValidProductionDate(shipDTO.getAfter().get())
                && shipDTO.getBefore().isPresent() && isValidProductionDate(shipDTO.getBefore().get())){
            Predicate p = cb.between(rootShip.get("prodDate"), new Date(shipDTO.getAfter().get()), new Date(shipDTO.getBefore().get()));
            criteria = cb.and(criteria, p);
        }else if(shipDTO.getAfter().isPresent() && isValidProductionDate(shipDTO.getAfter().get())){
            Predicate p = cb.greaterThanOrEqualTo(rootShip.get("prodDate"), new Date(shipDTO.getAfter().get()));
            criteria = cb.and(criteria, p);
        }else if(shipDTO.getBefore().isPresent() && isValidProductionDate(shipDTO.getBefore().get())){
            Predicate p = cb.lessThanOrEqualTo(rootShip.get("prodDate"), new Date(shipDTO.getBefore().get()));
            criteria = cb.and(criteria, p);
        }

        //check by condition
        if(shipDTO.isUsed().isPresent()){
            Predicate p = cb.equal(rootShip.get("isUsed"), shipDTO.isUsed().get() ? 1 : 0);
            criteria = cb.and(criteria, p);
        }

        if(shipDTO.getShipType().isPresent()){
            Predicate p = cb.equal(rootShip.get("shipType"), shipDTO.getShipType().get());
            criteria = cb.and(criteria, p);
        }

        //check by crewSize
        if(shipDTO.getMinCrewSize().isPresent() && isValidCrewSie(shipDTO.getMinCrewSize().get())
                && shipDTO.getMaxCrewSize().isPresent() && isValidCrewSie(shipDTO.getMaxCrewSize().get())){
            Predicate p = cb.between(rootShip.get("crewSize"), shipDTO.getMinCrewSize().get(), shipDTO.getMaxCrewSize().get());
            criteria = cb.and(criteria, p);
        }else if(shipDTO.getMinCrewSize().isPresent() && isValidCrewSie(shipDTO.getMinCrewSize().get())){
            Predicate p = cb.greaterThanOrEqualTo(rootShip.get("crewSize"), shipDTO.getMinCrewSize().get());
            criteria = cb.and(criteria, p);
        }else if(shipDTO.getMaxCrewSize().isPresent() && isValidCrewSie(shipDTO.getMaxCrewSize().get())){
            Predicate p = cb.lessThanOrEqualTo(rootShip.get("crewSize"), shipDTO.getMaxCrewSize().get());
            criteria = cb.and(criteria, p);
        }

        //check by speed
        if(shipDTO.getMinSpeed().isPresent() && isValidSpeed(shipDTO.getMinSpeed().get())
                && shipDTO.getMaxSpeed().isPresent() && isValidSpeed(shipDTO.getMaxSpeed().get())){
            Predicate p = cb.between(rootShip.get("speed"), shipDTO.getMinSpeed().get(), shipDTO.getMaxSpeed().get());
            criteria = cb.and(criteria, p);
        }else if(shipDTO.getMinSpeed().isPresent() && isValidSpeed(shipDTO.getMinSpeed().get())){
            Predicate p = cb.greaterThanOrEqualTo(rootShip.get("speed"), shipDTO.getMinSpeed().get());
            criteria = cb.and(criteria, p);
        }else if(shipDTO.getMaxSpeed().isPresent() && isValidSpeed(shipDTO.getMaxSpeed().get())){
            Predicate p = cb.lessThanOrEqualTo(rootShip.get("speed"), shipDTO.getMaxSpeed().get());
            criteria = cb.and(criteria, p);
        }

        //check by rating
        if(shipDTO.getMinRating().isPresent() && isValidRating(shipDTO.getMinRating().get())
                && shipDTO.getMaxRating().isPresent() && isValidRating(shipDTO.getMaxRating().get())){
            Predicate p = cb.between(rootShip.get("rating"), shipDTO.getMinRating().get(), shipDTO.getMaxRating().get());
            criteria = cb.and(criteria, p);
        }else if(shipDTO.getMinRating().isPresent() && isValidRating(shipDTO.getMinRating().get())){
            Predicate p = cb.greaterThanOrEqualTo(rootShip.get("rating"), shipDTO.getMinRating().get());
            criteria = cb.and(criteria, p);
        }else if(shipDTO.getMaxRating().isPresent() && isValidRating(shipDTO.getMaxRating().get())){
            Predicate p = cb.lessThanOrEqualTo(rootShip.get("rating"), shipDTO.getMaxRating().get());
            criteria = cb.and(criteria, p);
        }

        shipCriteria.where(criteria);


        //order
        if(shipDTO.getOrder().isPresent() && isValidOrder(shipDTO.getOrder().get())){
            shipCriteria.orderBy(cb.asc(rootShip.get(ShipOrder.valueOf(shipDTO.getOrder().get()).getFieldName())));
        }

        //pagination
        if(shipDTO.getPageSize().isPresent() && isValidPageSizeOrPageNumber(shipDTO.getPageSize().get())
            && shipDTO.getPageNumber().isPresent() && isValidPageSizeOrPageNumber(shipDTO.getPageNumber().get()) &&
                shipDTO.getPageNumber().get() == 0){
            return entityManager.createQuery(shipCriteria).setFirstResult(shipDTO.getPageNumber().get()).
                    setMaxResults(shipDTO.getPageSize().get()).getResultList();
        }else if(shipDTO.getPageSize().isPresent() && isValidPageSizeOrPageNumber(shipDTO.getPageSize().get())
                    && shipDTO.getPageNumber().isPresent() && isValidPageSizeOrPageNumber(shipDTO.getPageNumber().get())){
            return entityManager.createQuery(shipCriteria).setFirstResult(shipDTO.getPageNumber().get() *
                    shipDTO.getPageSize().get()).setMaxResults(shipDTO.getPageSize().get()).getResultList();
        }else return entityManager.createQuery(shipCriteria).getResultList();
    }

    @Override
    public Ship getShipById(Long id) {
        if(isValidId(id)) return repository.findById(id).orElseThrow(ShipNotFoundException::new);
        throw  new ShipBadRequestException();
    }

    @Override
    public Ship updateShip(Long id, Ship reqShip) {

        if (isValidId(id)) {
            Ship ship = repository.findById(id).orElseThrow(ShipNotFoundException::new);

            if(reqShip.getName() != null){
                if(isValidString(reqShip.getName())) ship.setName(reqShip.getName());
                else throw new ShipBadRequestException();
            }

            if(reqShip.getPlanet() != null){
                if(isValidString(reqShip.getPlanet())) ship.setPlanet(reqShip.getPlanet());
                else throw new ShipBadRequestException();
            }

            if(reqShip.getSpeed() != null){
                if(isValidSpeed(reqShip.getSpeed())) ship.setSpeed( reqShip.getSpeed());
                else throw new ShipBadRequestException();
            }

            if(reqShip.getCrewSize() != null){
                if(isValidCrewSie(reqShip.getCrewSize())) ship.setCrewSize(reqShip.getCrewSize());
                else throw new ShipBadRequestException();
            }

            if(reqShip.isUsed() != null){
                ship.setUsed(reqShip.isUsed());
            }

            if(reqShip.getShipType() != null){
                if(isValidShipType(reqShip.getShipType())) ship.setShipType(reqShip.getShipType());
                else throw new ShipBadRequestException();
            }

            if(reqShip.getProdDate() != null){
                if(isValidProductionDate(reqShip.getProdDate().getTime())) ship.setProdDate(reqShip.getProdDate());
                else throw new ShipBadRequestException();
            }

            ship.setRating(calculateShipRating(ship.isUsed(), ship.getSpeed(), ship.getProdDate()));
            repository.saveAndFlush(ship);
            return ship;
        }

        return null;
    }

    @Override
    public Ship addShip(Ship ship) {

        if(isValidString(ship.getName()) && isValidString(ship.getPlanet())
                && isValidShipType(ship.getShipType()) && isValidProductionDate(ship.getProdDate().getTime())
                && isValidSpeed(ship.getSpeed()) && isValidCrewSie(ship.getCrewSize())){

            if (ship.isUsed() == null) ship.setUsed(false);

            ship.setRating(calculateShipRating(ship.isUsed(), ship.getSpeed(), ship.getProdDate()));
            ship.setSpeed(new BigDecimal(ship.getSpeed()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            repository.saveAndFlush(ship);

            return ship;
        } else throw new ShipBadRequestException();
    }

    @Override
    public void removeShip(Long id) {
        if(id > 0){
            Ship ship = repository.findById(id).orElseThrow(ShipNotFoundException::new);
            repository.deleteById(id);
        }else throw new ShipBadRequestException();
    }

    public double calculateShipRating(Boolean isUsed, double speed, Date prodDate){
        double usedRating = isUsed ? 0.5 : 1;
        double rating = (80 * speed * usedRating) / (new GregorianCalendar(3019, 1, 1).getTime().getYear() - prodDate.getYear() + 1);
        return new BigDecimal(rating).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public boolean isValidString(String string){
        return string != null && !string.equals("") && string.length() <= 50;
    }
    public boolean isValidProductionDate(Long productionDate){
        return productionDate != null && productionDate >= new GregorianCalendar(2800, 1, 1).getTimeInMillis()
                && productionDate < new GregorianCalendar(3019, 1 ,1).getTimeInMillis();
    }

    public boolean isValidCrewSie(Integer crewSize){
        return crewSize != null && crewSize > 1 && crewSize < 9999;
    }

    public boolean isValidSpeed(Double speed){
        return speed != null && speed > 0.01d && speed < 0.99d;
    }

    public boolean isValidRating(Double rating){
        return rating != null && rating < Double.MAX_VALUE;
    }

    public boolean isValidOrder(String order){
        return !order.equals("");
    }

    public boolean isValidPageSizeOrPageNumber(Integer number) {
        return number != Integer.MAX_VALUE;
    }


    public boolean isValidId(Long id){
        if(id > 0){
            return true;
        }else throw new ShipBadRequestException();
    }

    public boolean isValidShipType(ShipType shipType){
        boolean b = false;
        for(ShipType type : ShipType.values()) {
            if (type == shipType) {
                b = true;
                break;
            }
        }
        return b;
    }
}
