
package com.saptarshi.coronaupdate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Regional {

    @SerializedName("loc")
    @Expose
    private String loc;
    @SerializedName("confirmedCasesIndian")
    @Expose
    private Integer confirmedCasesIndian;
    @SerializedName("discharged")
    @Expose
    private Integer discharged;
    @SerializedName("deaths")
    @Expose
    private Integer deaths;
    @SerializedName("confirmedCasesForeign")
    @Expose
    private Integer confirmedCasesForeign;

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public Integer getConfirmedCasesIndian() {
        return confirmedCasesIndian;
    }

    public void setConfirmedCasesIndian(Integer confirmedCasesIndian) {
        this.confirmedCasesIndian = confirmedCasesIndian;
    }

    public Integer getDischarged() {
        return discharged;
    }

    public void setDischarged(Integer discharged) {
        this.discharged = discharged;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getConfirmedCasesForeign() {
        return confirmedCasesForeign;
    }

    public void setConfirmedCasesForeign(Integer confirmedCasesForeign) {
        this.confirmedCasesForeign = confirmedCasesForeign;
    }

}
