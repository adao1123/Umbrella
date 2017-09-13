package com.foo.umbrella.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by adao1 on 9/13/2017.
 */

public class WeatherOrigin {

    @SerializedName("hourly_forecast")
    private Forecast[] forecastArray;

    public Forecast[] getForecastArray() {
        return forecastArray;
    }

    public class Forecast{
        private Time FCTTIME;
        private Temperature temp;
        private Temperature dewpoint;
        private String condition;
        private String icon;
        private boolean isColdest;
        private boolean isHottest;

        public boolean isColdest() {
            return isColdest;
        }

        public boolean isHottest() {
            return isHottest;
        }

        public void setColdest(boolean coldest) {
            isColdest = coldest;
        }

        public void setHottest(boolean hottest) {
            isHottest = hottest;
        }

        public Time getFCTTIME() {
            return FCTTIME;
        }

        public Temperature getTemp() {
            return temp;
        }

        public Temperature getDewpoint() {
            return dewpoint;
        }

        public String getCondition() {
            return condition;
        }

        public String getIcon() {
            return icon;
        }

        public class Time{
            private String hour;
            private String hour_padded;
            private String civil;
            private String weekday_name;
            private String weekday_name_night;
            private String weekday_name_abbrev;
            private String ampm;

            public String getHour() {
                return hour;
            }

            public String getHour_padded() {
                return hour_padded;
            }

            public String getCivil() {
                return civil;
            }

            public String getWeekday_name() {
                return weekday_name;
            }

            public String getWeekday_name_night() {
                return weekday_name_night;
            }

            public String getWeekday_name_abbrev() {
                return weekday_name_abbrev;
            }

            public String getAmpm() {
                return ampm;
            }
        }

        public class Temperature{
            private String english;
            private String metric;

            public String getEnglish() {
                return english;
            }

            public String getMetric() {
                return metric;
            }
        }
    }
}
