package com.company;

public class House {
    public int houseNumber = 0;
    private int roomNumber = 0;
    private int occupantNumber = 0;
    private String name = "";
    @Override
    public String toString(){
        return "house №" + houseNumber + " with name " + name + " has " + roomNumber + " rooms, " + occupantNumber + " occupants";
    }
    public String getName() {
        return name;
    }
    private void incrementOccupants() {
        occupantNumber++;
    }
    private void setOccupantsRooms(int occupants, int rooms) {
        occupantNumber = occupants;
        roomNumber = rooms;
    }

    public House() { }
    public House(int houseNumber, int roomNumber, int occupantNumber, String name) {
        this.houseNumber = houseNumber;
        this.roomNumber = roomNumber;
        this.occupantNumber = occupantNumber;
        this.name = name;
    }
}
