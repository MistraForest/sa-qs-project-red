package de.demothb.model;

import java.time.LocalDate;
import java.util.UUID;

public class Event {

    public enum AvailabilityThreshold {SOLD_OUT, EXTREMELY_LOW, LOW, NORMAL};

    private UUID id;

    private String name;

    private LocalDate date;

    private int totalTicket;

    private int ticketSold;

    public Event(String name, int totalTicket, LocalDate date) throws Exception {
        this.name = name;
        if (totalTicket <= 0){
            throw new Exception("Number of seat must be greater than zero");
            // Normalement la validation doit se faire avant
        }
        this.totalTicket = totalTicket;
        this.date = date;
        this.ticketSold = 0;
        this.id = UUID.randomUUID();
    }

    public String getId() {
        return id.toString();
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getTotalTicket() {
        return totalTicket;
    }

    public void purchaseTicket(int quantity) throws Exception {
        if (quantity <= 0){
            throw  new Exception("quantity must be greater than zero");
        }
        if (quantity > this.getAvailability()) {
            throw new Exception("Cannot buy more than : " + getAvailability());
        }
        this.ticketSold += quantity;
    }

    public int getAvailability(){
        return this.totalTicket - this.ticketSold;
    }

    public long getAvailabilityRatio(){
        return Math.round(100.0 * getAvailability() / getTotalTicket());
    }

    public AvailabilityThreshold getAvailabilityThreshold() {
        if (0 == getAvailability()) return AvailabilityThreshold.SOLD_OUT;
        if (getAvailabilityRatio() <= 5) return AvailabilityThreshold.EXTREMELY_LOW;
        else if (getAvailabilityRatio() <= 10) return AvailabilityThreshold.LOW;
        else return AvailabilityThreshold.NORMAL;
    }

}
