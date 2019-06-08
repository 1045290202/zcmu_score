package com.sjk.zcmu.score.model;

import java.util.List;

public class Info {
    private static List<ScoreTableBean> scoreTableBeanList;
    private static List<String> schoolYearList;

    public static List<ScoreTableBean> getScoreTableBeanList() {
        return scoreTableBeanList;
    }

    public static void setScoreTableBeanList(List<ScoreTableBean> scoreTableBeanList) {
        Info.scoreTableBeanList = scoreTableBeanList;
    }

    public static List<String> getSchoolYearList() {
        return schoolYearList;
    }

    public static void setSchoolYearList(List<String> schoolYearList) {
        Info.schoolYearList = schoolYearList;
    }
}
