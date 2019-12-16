package com.manchesterbeach.transport.repo;

import com.manchesterbeach.transport.domain.Journey;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JourneyRepo {

    private List<Journey> journeyList = new ArrayList();

    public void addNewJourney(Journey journey){
        getJourneyList().add(journey);
    }

    public int getJourneyListSize(){
        return getJourneyList().size();
    }

    public List<Journey> getJourneyList() {
        return journeyList;
    }
}
