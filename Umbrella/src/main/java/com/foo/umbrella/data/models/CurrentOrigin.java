package com.foo.umbrella.data.models;

/**
 * Created by adao1 on 9/13/2017.
 */

public class CurrentOrigin {
    private CurrentObservation current_observation;

    public CurrentObservation getCurrent_observation() {
        return current_observation;
    }

    public class CurrentObservation{
        private DisplayLocation display_location;
        private String weather;
        private double temp_f;
        private double temp_c;
        private String icon;

        public DisplayLocation getDisplay_location() {
            return display_location;
        }

        public String getWeather() {
            return weather;
        }

        public double getTemp_f() {
            return temp_f;
        }

        public double getTemp_c() {
            return temp_c;
        }

        public String getIcon() {
            return icon;
        }

        public class DisplayLocation{
            private String full;

            public String getFull() {
                return full;
            }
        }

    }
}
