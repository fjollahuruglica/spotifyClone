package com.example.spotify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExplicitContent {

    @SerializedName("filter_enabled")
    @Expose
    private Boolean filterEnabled;
    @SerializedName("filter_locked")
    @Expose
    private Boolean filterLocked;

    public Boolean getFilterEnabled() {
        return filterEnabled;
    }

    public void setFilterEnabled(Boolean filterEnabled) {
        this.filterEnabled = filterEnabled;
    }

    public Boolean getFilterLocked() {
        return filterLocked;
    }

    public void setFilterLocked(Boolean filterLocked) {
        this.filterLocked = filterLocked;
    }

}
