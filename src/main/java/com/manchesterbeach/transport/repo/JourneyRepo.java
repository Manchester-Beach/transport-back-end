package com.manchesterbeach.transport.repo;

import com.manchesterbeach.transport.domain.Journey;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class JourneyRepo implements CrudRepo<Journey> {

    private List<Journey> journeyList = new ArrayList<>();

    @Override
    public void save(Journey journey){
        findAll().add(journey);
    }

    @Override
    public List<Journey> findAll() {
        return journeyList;
    }

    @Override
    public Journey findById(Long journeyIndex) {

        if(journeyIndex >= findAll().size() || journeyIndex < 0){
            return null;
        }

        return findAll().get(journeyIndex.intValue());
    }

    @Override
    public ResponseEntity delete(Journey journeyToDelete) {
        journeyList.remove(journeyToDelete);
        return ResponseEntity.ok().build();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public int count() {
        return findAll().size();
    }
}
