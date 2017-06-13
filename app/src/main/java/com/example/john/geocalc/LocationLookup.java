package com.example.john.geocalc;

import org.parceler.Parcel;

/**
 * Created by John on 6/8/2017.
 */

@Parcel
public class LocationLookup {
    String _key;
    double origLat;
    double origLng;
    double destLat;
    double destLng;
    String timestamp;

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

    public Double getOrigLat() {
        return origLat;
    }

    public void setOrigLat(Double origLat) {
        this.origLat = origLat;
    }

    public Double getOrigLng() {
        return origLng;
    }

    public void setOrigLng(Double origLng) {
        this.origLng = origLng;
    }

    public Double getDestLng() {
        return destLng;
    }

    public void setDestLng(Double destLng) {
        this.destLng = destLng;
    }

    public Double getDestLat() {
        return destLat;
    }

    public void setDestLat(Double destLat) {
        this.destLat = destLat;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
