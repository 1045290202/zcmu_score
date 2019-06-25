package com.sjk.zcmu.score;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.column.ColumnInfo;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.draw.ImageResDrawFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnClickListener;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.bin.david.form.utils.DensityUtils;
import com.sjk.zcmu.score.model.Info;
import com.sjk.zcmu.score.model.ScoreTableBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import fr.ganfra.materialspinner.MaterialSpinner;

public class ScoreActivity extends AppCompatActivity implements View.OnClickListener {

    private SmartTable<ScoreTableBean> table;
    private List<ScoreTableBean> scoreTableBeanList;
    private List<ScoreTableBean> nowScoreTableBeanList;
    private List<String> schoolYearList;
    private String schoolYears = "全部学年";
    private String terms = "全部学期";

    private boolean radioChecked = false;
    private boolean schoolYearChecked = false;
    private boolean termChecked = false;

    private Column<Boolean> operation;
    private Column<String> schoolYear;
    private Column<String> term;
    private Column<String> courseCode;
    private Column<String> courseName;
    private Column<String> courseNature;
    private Column<String> courseAttach;
    private Column<String> credit;
    private Column<String> point;
    private Column<String> score;
    private Column<String> minorMark;
    private Column<String> makeUpScore;
    private Column<String> retakeScore;
    private Column<String> collegeName;
    private Column<String> remark;
    private Column<String> retakeMark;
    private Column<String> englishNameOfCourse;

    private List<Integer> clickViews = Arrays.asList(
            R.id.clear,
            R.id.compute
    );

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info: {
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.reload: {
                MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                        .title("刷新")
                        .content("确定要刷新吗？")
                        .negativeText("取消")
                        .positiveText("确定")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                setResult(1);
                                finish();
                            }
                        })
                        .build();
                materialDialog.show();
                break;
            }
            default: {
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.score_activity_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("请输入课程名称...");

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setTableDataBySpinner();
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RadioButton normal = findViewById(R.id.normal);
                normal.setChecked(true);

                setTableDataBySearchView(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RadioButton normal = findViewById(R.id.normal);
                normal.setChecked(true);

                if (newText == null || newText.length() == 0) {
                    setTableDataBySpinner();
                } else {
                    setTableDataBySearchView(newText);
                }
                return false;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void setTableDataBySpinner() {
        List<ScoreTableBean> list = new ArrayList<>();
        for (ScoreTableBean scoreTableBean : scoreTableBeanList) {
            if (schoolYears.equals("全部学年") && terms.equals("全部学期")) {
                list.add(scoreTableBean);
            } else if (schoolYears.equals("全部学年") && scoreTableBean.getTerm().equals(terms)) {
                list.add(scoreTableBean);
            } else if (scoreTableBean.getSchoolYear().equals(schoolYears) && terms.equals("全部学期")) {
                list.add(scoreTableBean);
            } else if (scoreTableBean.getSchoolYear().equals(schoolYears) && scoreTableBean.getTerm().equals(terms)) {
                list.add(scoreTableBean);
            }
        }
        nowScoreTableBeanList = list;
        TableData<ScoreTableBean> tableData = getTableData(list);
        table.setTableData(tableData);
        table.invalidate();
    }

    private void setTableDataBySearchView(String query) {
        List<ScoreTableBean> list = new ArrayList<>();
        for (ScoreTableBean scoreTableBean : scoreTableBeanList) {
            if (scoreTableBean.getCourseName().contains(query)) {
                list.add(scoreTableBean);
            }
        }
        nowScoreTableBeanList = list;
        TableData<ScoreTableBean> tableData = getTableData(list);
        table.setTableData(tableData);
        table.invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        init();
        initSpinner();
        initRadio();

        table.invalidate();
        setViewClick();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0);
    }

    private void setViewClick() {
        for (int clickView : clickViews) {
            findViewById(clickView).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear: {
                MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                        .title("清除")
                        .content("真要要清除当前筛选列表中已选中的项目吗？")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                for (int i = 0, l = nowScoreTableBeanList.size(); i < l; i++) {
                                    nowScoreTableBeanList.get(i).setOperation(false);
                                }
                                TableData<ScoreTableBean> tableData = getTableData(nowScoreTableBeanList);
                                table.setTableData(tableData);
                                table.invalidate();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .build();
                materialDialog.show();
                break;
            }
            case R.id.compute: {//计算加权平均几绩点
                List<ScoreTableBean> list = new ArrayList<>();
                for (ScoreTableBean scoreTableBean : scoreTableBeanList) {
                    if (scoreTableBean.getOperation()) {
                        list.add(scoreTableBean);
                    }
                }

                StringBuilder checkCourse = new StringBuilder();
                double creditPoint = 0, credit = 0;
                for (ScoreTableBean scoreTableBean : list) {
                    creditPoint += Double.valueOf(scoreTableBean.getCredit()) * Double.valueOf(scoreTableBean.getPoint());
                    credit += Double.valueOf(scoreTableBean.getCredit());
                    checkCourse.append(scoreTableBean.getCourseName()).append("\n");
                }
                double weightAverage = creditPoint / credit;

                MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title("计算")
                        .content(String.format("加权平均绩点：\n" +
                                        "%s\n" +
                                        "\n" +
                                        "选择课程：\n" +
                                        "%s",
                                list.size() > 0 ? weightAverage : "未选择课程",
                                checkCourse
                        ))
                        .positiveText("确定")
                        .build();
                dialog.show();
                break;
            }
            default: {
                break;
            }
        }
    }

    private void initRadio() {
        final LinearLayout spinners = findViewById(R.id.spinners);
        final TextView tips = findViewById(R.id.tips);

        RadioButton normal = findViewById(R.id.normal);
        normal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (radioChecked) {
                        setTableDataBySpinner();
                    }
                    spinners.setVisibility(View.VISIBLE);
                    tips.setVisibility(View.GONE);
                    radioChecked = true;
                }
            }
        });

        RadioButton checked = findViewById(R.id.checked);
        checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinners.setVisibility(View.GONE);
                    tips.setVisibility(View.VISIBLE);
                    List<ScoreTableBean> list = new ArrayList<>();
                    for (ScoreTableBean scoreTableBean : scoreTableBeanList) {
                        if (scoreTableBean.getOperation()) {
                            list.add(scoreTableBean);
                        }
                    }
                    nowScoreTableBeanList = list;
                    TableData<ScoreTableBean> tableData = getTableData(list);
                    table.setTableData(tableData);
                    table.invalidate();
                    radioChecked = true;
                }
            }
        });

        RadioButton notChecked = findViewById(R.id.not_checked);
        notChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinners.setVisibility(View.GONE);
                    tips.setVisibility(View.VISIBLE);
                    List<ScoreTableBean> list = new ArrayList<>();
                    for (ScoreTableBean scoreTableBean : scoreTableBeanList) {
                        if (!scoreTableBean.getOperation()) {
                            list.add(scoreTableBean);
                        }
                    }
                    nowScoreTableBeanList = list;
                    TableData<ScoreTableBean> tableData = getTableData(list);
                    table.setTableData(tableData);
                    table.invalidate();
                    radioChecked = true;
                }
            }
        });
    }

    private void initSpinner() {
        final String[] schoolYearItems = schoolYearList.toArray(new String[0]);
        ArrayAdapter<String> schoolYearAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Objects.requireNonNull(schoolYearItems));
        schoolYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        MaterialSpinner schoolYearSpinner = findViewById(R.id.school_year_spinner);
        schoolYearSpinner.setAdapter(schoolYearAdapter);
        schoolYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                schoolYears = schoolYearItems[position].trim();
                if (schoolYearChecked) {
                    setTableDataBySpinner();
                }
                schoolYearChecked = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final String[] termItems = {"全部学期", "第一学期", "第二学期"};
        ArrayAdapter<String> termAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Objects.requireNonNull(termItems));
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner termSpinner = findViewById(R.id.term_spinner);
        termSpinner.setAdapter(termAdapter);
        termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (termItems[position].equals("第一学期")) {
                    terms = "1";
                } else if (termItems[position].equals("第二学期")) {
                    terms = "2";
                } else {
                    terms = termItems[position];
                }
                if (termChecked) {
                    setTableDataBySpinner();
                }
                termChecked = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initColumn() {
        int length = 16;
        String[] columnName = {
                "学年", "学期", "课程代码", "课程名称", "课程性质",
                "课程归属", "学分", "绩点", "成绩", "辅修标记",
                "补考成绩", "重修成绩", "学院名称", "备注", "重修标记",
                "课程英文名称"
        };
        String[] fieldName = {
                "schoolYear", "term", "courseCode", "courseName", "courseNature",
                "courseAttach", "credit", "point", "score", "minorMark",
                "makeUpScore", "retakeScore", "collegeName", "remark", "retakeMark",
                "englishNameOfCourse"
        };

        OnColumnItemClickListener onColumnItemClickListener = new OnColumnItemClickListener() {
            @Override
            public void onClick(Column column, String value, Object o, int position) {
                if (operation.getDatas().get(position)) {
                    operation.getDatas().set(position, false);
                    nowScoreTableBeanList.get(position).setOperation(false);
//                    scoreTableBeanList.get(position).setOperation(false);
                } else {
                    operation.getDatas().set(position, true);
                    nowScoreTableBeanList.get(position).setOperation(true);
//                    scoreTableBeanList.get(position).setOperation(true);
                }
//                table.refreshDrawableState();
                table.invalidate();
            }
        };

        int size = DensityUtils.dp2px(this, 24);
        operation = new Column<>("选择", "operation",
                new ImageResDrawFormat<Boolean>(size, size) {
                    @Override
                    protected Context getContext() {
                        return ScoreActivity.this;
                    }

                    @Override
                    protected int getResourceID(Boolean isCheck, String value, int position) {
                        if (isCheck) {
                            return R.mipmap.ic_check_box;
                        }
                        return R.mipmap.ic_check_box_outline;
                    }
                });
        operation.setFixed(true);
        operation.setOnColumnItemClickListener(onColumnItemClickListener);

        schoolYear = new Column<>("学年", "schoolYear");
        schoolYear.setOnColumnItemClickListener(onColumnItemClickListener);

        term = new Column<>("学期", "term");
        term.setOnColumnItemClickListener(onColumnItemClickListener);

        courseCode = new Column<>("课程代码", "courseCode");
        courseCode.setOnColumnItemClickListener(onColumnItemClickListener);

        courseName = new Column<>("课程名称", "courseName");
        courseName.setWidth(180);
        courseName.setFixed(true);
        courseName.setOnColumnItemClickListener(onColumnItemClickListener);

        courseNature = new Column<>("课程性质", "courseNature");
        courseNature.setOnColumnItemClickListener(onColumnItemClickListener);

        courseAttach = new Column<>("课程归属", "courseAttach");
        courseAttach.setOnColumnItemClickListener(onColumnItemClickListener);

        credit = new Column<>("学分", "credit");
        credit.setOnColumnItemClickListener(onColumnItemClickListener);

        point = new Column<>("绩点", "point");
        point.setOnColumnItemClickListener(onColumnItemClickListener);

        score = new Column<>("成绩", "score");
        score.setOnColumnItemClickListener(onColumnItemClickListener);

        minorMark = new Column<>("辅修标记", "minorMark");
        minorMark.setOnColumnItemClickListener(onColumnItemClickListener);

        makeUpScore = new Column<>("补考成绩", "makeUpScore");
        makeUpScore.setOnColumnItemClickListener(onColumnItemClickListener);

        retakeScore = new Column<>("重修成绩", "retakeScore");
        retakeScore.setOnColumnItemClickListener(onColumnItemClickListener);

        collegeName = new Column<>("学院名称", "collegeName");
        collegeName.setWidth(180);
        collegeName.setOnColumnItemClickListener(onColumnItemClickListener);

        remark = new Column<>("备注", "remark");
        remark.setOnColumnItemClickListener(onColumnItemClickListener);

        retakeMark = new Column<>("重修标记", "retakeMark");
        retakeMark.setOnColumnItemClickListener(onColumnItemClickListener);

        englishNameOfCourse = new Column<>("课程英文名称", "englishNameOfCourse");
        englishNameOfCourse.setOnColumnItemClickListener(onColumnItemClickListener);
    }

    private void init() {
        scoreTableBeanList = Info.getScoreTableBeanList();
        schoolYearList = Info.getSchoolYearList();

        initColumn();

        TableData<ScoreTableBean> tableData = getTableData(scoreTableBeanList);
        nowScoreTableBeanList = scoreTableBeanList;

        table = findViewById(R.id.table);
        table.setTableData(tableData);
        FontStyle style = new FontStyle();
        style.setTextSpSize(this, 13);
        table.getConfig().setContentStyle(style);
        table.setZoom(false);
        table.getConfig().setZoom(1);
        table.getConfig().setShowTableTitle(false);
        table.setOnColumnClickListener(new OnColumnClickListener() {
            @Override
            public void onClick(ColumnInfo columnInfo) {
                if (columnInfo.column.isFixed()) {
                    columnInfo.column.setFixed(false);
                    Toast.makeText(ScoreActivity.this, String.format("当前列“%s”已取消固定", columnInfo.value), Toast.LENGTH_SHORT).show();
                } else {
                    columnInfo.column.setFixed(true);
                    Toast.makeText(ScoreActivity.this, String.format("当前列“%s”已固定", columnInfo.value), Toast.LENGTH_SHORT).show();
                }
                table.invalidate();
            }
        });
        table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(final CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(ScoreActivity.this, R.color.grey200);
                } else {
                    return TableConfig.INVALID_COLOR;
                }
            }

            @Override
            public int getTextColor(final CellInfo cellInfo) {
                //不合格标红
                if (cellInfo.col == 9 || cellInfo.col == 11 || cellInfo.col == 12) {
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

    private TableData<ScoreTableBean> getTableData(List<ScoreTableBean> scoreTableBeanList) {
        return new TableData<>("成绩查询", scoreTableBeanList,
                operation,
                schoolYear,
                term,
                courseCode,
                courseName,
                courseNature,
                courseAttach,
                credit,
                point,
                score,
                minorMark,
                makeUpScore,
                retakeScore,
                collegeName,
                remark,
                retakeMark,
                englishNameOfCourse
        );
    }
}
