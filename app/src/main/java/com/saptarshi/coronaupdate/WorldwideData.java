
package com.saptarshi.coronaupdate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorldwideData {

    @SerializedName("total_cases")
    @Expose
    private String totalCases;
    @SerializedName("total_deaths")
    @Expose
    private String totalDeaths;
    @SerializedName("total_recovered")
    @Expose
    private String totalRecovered;
    @SerializedName("new_cases")
    @Expose
    private String newCases;
    @SerializedName("new_deaths")
    @Expose
    private String newDeaths;
    @SerializedName("statistic_taken_at")
    @Expose
    private String statisticTakenAt;

    public String getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(String totalCases) {
        this.totalCases = totalCases;
    }

    public String getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(String totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public String getTotalRecovered() {
        return totalRecovered;
    }

    public void setTotalRecovered(String totalRecovered) {
        this.totalRecovered = totalRecovered;
    }

    public String getNewCases() {
        return newCases;
    }

    public void setNewCases(String newCases) {
        this.newCases = newCases;
    }

    public String getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(String newDeaths) {
        this.newDeaths = newDeaths;
    }

    public String getStatisticTakenAt() {
        return statisticTakenAt;
    }

    public void setStatisticTakenAt(String statisticTakenAt) {
        this.statisticTakenAt = statisticTakenAt;
    }

}
