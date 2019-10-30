package com.deer.retrofitapisample.obj;

import com.google.gson.annotations.Expose;

public class LogResponse extends BaseResponse {
    
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {
        @Expose
        private String uuid;  
        @Expose
        private double lat;
        @Expose
        private double lng;
        @Expose
        private long timeStamp;

        public String getUuid() {
            return uuid;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public long getTimeStamp() {
            return timeStamp;
        }
    }
}
