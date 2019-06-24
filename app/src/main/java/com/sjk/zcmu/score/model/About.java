package com.sjk.zcmu.score.model;

public class About {
    private String infoTitle;
    private String detailInfo;

    public About(String infoTitle, String detailInfo) {
        this.infoTitle = infoTitle;
        this.detailInfo = detailInfo;
    }

    public String getInfoTitle() {
        return infoTitle;
    }

    public String getDetailInfo() {
        return detailInfo;
    }
}
