package util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class Graphic {

    static JPanel panel = new JPanel();

    static JPanel graphPanel = new JPanel();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Смешанная схема");
        frame.setSize(2500, 1000);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        panel.setSize(300, 300);
        frame.setResizable(false);

        JFreeChart chart = createChart(createDataset());
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(2500, 1000));
        graphPanel.removeAll();
        graphPanel.add(chartPanel, BorderLayout.WEST);
        panel.add(graphPanel);
        frame.add(panel);
        frame.setVisible(true);
    }

    private static JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Bar conductivity",      // chart title
                "Размер блока",                      // x axis label
                "Время расчета",                      // y axis label
                dataset,                  // data
                PlotOrientation.VERTICAL,
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        );
        chart.setBackgroundPaint(Color.white);
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.black);
        plot.setRangeGridlinePaint(Color.black);

        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;

    }

    public static XYSeriesCollection createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        final XYSeries series0 = createSeries1();
        final XYSeries series1 = createSeries2();
        final XYSeries series2 = createSeries3();
        dataset.addSeries(series0);
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;
    }

    public static XYSeries createSeries1() {
        final XYSeries series1 =  new XYSeries("Замер 1");
        series1.add(  32,22.5450);
        series1.add(  40,20.7925);
        series1.add(  50,19.5668);
        series1.add(  64,18.6394);
        series1.add(  80,18.1046);
        series1.add(  100,19.6276);
        series1.add(  128,18.4637);
        series1.add(  160,17.6123);
        series1.add(  200,16.9533);
        series1.add(  250,16.3798);
        series1.add(  256,16.3802);
        series1.add(  320,15.9441);
        series1.add(  400,15.7164);
        series1.add(  500,15.3912);
        series1.add(  512,15.3432);
        series1.add(  640,15.1673);
        series1.add(  800,14.9893);
        series1.add( 1000,14.8687);
        series1.add( 1250,14.7718);
        series1.add( 1280,14.7774);
        series1.add( 1600,14.6887);
        series1.add( 2000,14.6239);
        series1.add( 2500,14.5738);
        series1.add( 2560,14.5705);
        series1.add( 3200,14.5739);
        series1.add( 4000,14.5592);
        series1.add( 5000,15.4812);
        series1.add( 6250,15.5129);
        return series1;
    }
    public static XYSeries createSeries2() {
        final XYSeries series1 =  new XYSeries("Замер 2");
        series1.add(   100, 19.8367);
        series1.add(   200, 17.1284);
        series1.add(   400, 15.7888);
        series1.add(   500, 15.5499);
        series1.add(   800, 15.1369);
        series1.add(  1000, 15.0244);
        series1.add(  1600, 14.8467);
        series1.add(  2000, 14.6469);
        series1.add(  2500, 14.5892);
        series1.add(  3200, 14.7249);
        series1.add(  4000, 14.5862);
        series1.add(  5000, 15.5487);
        series1.add(  6400, 15.6738);

        return series1;
    }
    public static XYSeries createSeries3() {
        final XYSeries series1 =  new XYSeries("Замер 3");
        series1.add( 10000, 15.8867);
        series1.add( 20000, 15.5698);
        series1.add( 40000, 16.7378);
        series1.add( 50000, 17.1060);
        series1.add( 80000, 17.2734);
        series1.add(100000, 17.2772);
        series1.add(160000, 17.2920);
        series1.add(200000, 17.3108);
        series1.add(250000, 17.3351);
        series1.add(320000, 17.3980);
        series1.add(400000, 17.5970);
        series1.add(500000, 18.1274);
        series1.add(800000, 21.6675);
        series1.add(1000000,23.6970);
        return series1;
    }


}
