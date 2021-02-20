
package com.harperskebab.network.retrofit.responsemodels;

import com.google.gson.annotations.SerializedName;
import com.harperskebab.model.Language;

import java.util.List;

public class RmLanguageListResponse {

    @SerializedName("LanguageListList")
    private List<Language> language;

    public List<Language> getLanguage() {
        return language;
    }

    public void setLanguage(List<Language> language) {
        this.language = language;
    }

}
