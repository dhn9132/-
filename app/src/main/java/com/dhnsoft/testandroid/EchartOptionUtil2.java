package com.dhnsoft.testandroid;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.json.GsonOption;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EchartOptionUtil2 {

    public static GsonOption getLineChartOptions(String list,String time) {
        EnhancedOption option = new EnhancedOption();
        option.title("湿度(单位:百分比)");
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        option.xAxis(new CategoryAxis().data(time));
        option.yAxis(new ValueAxis());
        Bar bar = new Bar("蒸发量");
        System.out.println("哈哈哈哈哈"+list);
        bar.itemStyle().normal().label().show(true).position("top");
        bar.data(list);
        option.series(bar);
        option.exportToHtml("bar1.html");
        option.view();
        return option;
    }



}