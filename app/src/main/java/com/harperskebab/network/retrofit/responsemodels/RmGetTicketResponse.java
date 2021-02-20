
package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.ComplaintsHistory;

import java.util.List;

public class RmGetTicketResponse {

    @SerializedName("ComplaintsHistory")
    private List<ComplaintsHistory> complaintsHistory;

    public List<ComplaintsHistory> getComplaintsHistory() {
        return complaintsHistory;
    }

    public void setComplaintsHistory(List<ComplaintsHistory> complaintsHistory) {
        this.complaintsHistory = complaintsHistory;
    }

}
