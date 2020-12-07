package de.thb.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Observable;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UIModelData extends Observable implements Serializable {
    private UUID Id;
    private Event event;
    private int numberOfTicketToBuy;
    private int actualAvailability;

}
