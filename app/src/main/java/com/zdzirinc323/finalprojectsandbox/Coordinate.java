package com.zdzirinc323.finalprojectsandbox;

public class Coordinate {

    private double lat, lng;

    public Coordinate(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String toString() {
        return "Latitude: " + Double.toString(lat)
                + "\nLongitude: " + Double.toString(lng);

    }

    public double euclideanDistance(Coordinate other) {

        if (other.getLat() == 0 && other.getLng() == 0) {
            return 2;
        }

        final double R = 6372.8; //In kilometers
        double lat1 = lat;
        double lng1 = lng;
        double lat2 = other.getLat();
        double lng2 = other.getLat();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return kmToMiles(R * c);

    }

    public double kmToMiles(double km) {
        return (8 * km) / 5;
    }


}

