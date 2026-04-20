package edu.tcu.cs.projectpulsebackend.dto;

import java.time.LocalDate;
import java.util.List;

public class ActiveWeekSetupRequest {

    private List<LocalDate> inactiveWeekStartDates;

    public ActiveWeekSetupRequest() {
    }

    public List<LocalDate> getInactiveWeekStartDates() {
        return inactiveWeekStartDates;
    }

    public void setInactiveWeekStartDates(List<LocalDate> inactiveWeekStartDates) {
        this.inactiveWeekStartDates = inactiveWeekStartDates;
    }
}
