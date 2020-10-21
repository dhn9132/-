package com.dhnsoft.testandroid;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Position;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.json.GsonOption;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EchartOptionUtil {

    public static GsonOption getLineChartOptions(String list,String time) {
        EnhancedOption option = new EnhancedOption();
        option.title("温度(单位:摄氏度)");
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        option.xAxis(new CategoryAxis().data(time));
        option.yAxis(new ValueAxis());
        Bar bar = new Bar();
        bar.itemStyle().normal().label().show(true).position("top");
        bar.data(list);
        option.series(bar);
        option.exportToHtml("bar1.html");
        option.view();
        return option;
    }

}