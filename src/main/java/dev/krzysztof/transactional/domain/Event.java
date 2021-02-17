package dev.krzysztof.transactional.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Event extends BaseEntity{
    @Column(name = "Event_Name", length = 10, nullable = false)
    private EventName eventName;

    @Column(name = "Status", length = 10, nullable = false)
    private EventStatus eventStatus;

    @Column(name = "Person_ID")
    private Long personId;

    @Override
    public int hashCode () {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

}

enum EventName {
    CREATED, MODIFIED
}

enum EventStatus {
    SUCCESS,FALSE
}
