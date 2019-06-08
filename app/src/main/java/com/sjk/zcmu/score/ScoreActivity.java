package com.sjk.zcmu.score;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sjk.zcmu.score.model.Info;
import com.sjk.zcmu.score.model.ScoreTableBean;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends BaseActivity {

    private SmartTable<ScoreTableBean> table;
    private List<ScoreTableBean> scoreTableBeanList;
    private List<String> schoolYearList;
    private String schoolYear = "全部学年";
    private String term = "全部学期";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        init();
        initSpinner();
    }

    private void initSpinner() {
        MaterialSpinner schoolYearSpinner = findViewById(R.id.school_year_spinner);
        schoolYearSpinner.setItems(schoolYearList);
        schoolYearSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                schoolYear = item;
                List<ScoreTableBean> list = new ArrayList<>();
                for (ScoreTableBean scoreTableBean : scoreTableBeanList) {
                    if (schoolYear.equals("全部学年") && term.equals("全部学期")) {
                        list.add(scoreTableBean);
                    } else if (schoolYear.equals("全部学年") && scoreTableBean.getTerm().equals(term)) {
                        list.add(scoreTableBean);
                    } else if (scoreTableBean.getSchoolYear().equals(schoolYear) && term.equals("全部学期")) {
                        list.add(scoreTableBean);
                    } else if (scoreTableBean.getSchoolYear().equals(schoolYear) && scoreTableBean.getTerm().equals(term)) {
                        list.add(scoreTableBean);
                    }
                }
                table.setData(list);
            }
        });

        MaterialSpinner termSpinner = findViewById(R.id.term_spinner);
        termSpinner.setItems("全部学期", "1", "2");
        termSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                term = item;
                List<ScoreTableBean> list = new ArrayList<>();
                for (ScoreTableBean scoreTableBean : scoreTableBeanList) {
                    if (schoolYear.equals("全部学年") && term.equals("全部学期")) {
                        list.add(scoreTableBean);
                    } else if (schoolYear.equals("全部学年") && scoreTableBean.getTerm().equals(term)) {
                        list.add(scoreTableBean);
                    } else if (scoreTableBean.getSchoolYear().equals(schoolYear) && term.equals("全部学期")) {
                        list.add(scoreTableBean);
                    } else if (scoreTableBean.getSchoolYear().equals(schoolYear) && scoreTableBean.getTerm().equals(term)) {
                        list.add(scoreTableBean);
                    }
                }
                table.setData(list);
            }
        });
    }

    private void init() {
        scoreTableBeanList = Info.getScoreTableBeanList();
        schoolYearList = Info.getSchoolYearList();

        table = findViewById(R.id.table);
        table.setData(scoreTableBeanList);
        table.setZoom(true);
        table.getConfig().setShowTableTitle(false);
        table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(ScoreActivity.this, R.color.grey200);
                } else {
                    return TableConfig.INVALID_COLOR;
                }
            }

            @Override
            public int getTextColor(CellInfo cellInfo) {
                if (cellInfo.col == 8 || cellInfo.col == 10 || cellInfo.col == 11) {
                    String data = (String) cellInfo.data;
                    int value;
                    try {
                        value = Integer.parseInt(data);
                        if (value < 60) {
                            return ContextCompat.getColor(ScoreActivity.this, R.color.red400);
                        }
                    } catch (Exception e) {
                        if (data.equals("不合格")) {
                            return ContextCompat.getColor(ScoreActivity.this, R.color.red400);
                        }
                    }
                }
                return super.getTextColor(cellInfo);
            }
        });
    }
}
