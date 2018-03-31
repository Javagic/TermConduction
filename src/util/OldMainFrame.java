package util;

import difference.DiffSolver;
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
import solvers.OldConductivitySolver;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

/**
 * Created by Ilya on 11/14/2017.
 */
public class OldMainFrame extends JFrame {
    JPanel panel = new JPanel();
    JLabel l_label = new JLabel("L, м: ");
    JSlider T_slider = new JSlider(0, 100, 10);
    JSlider N_slider = new JSlider(4, 1000, 6);
    JSlider t_N_slider = new JSlider(4, 1000, 300);
    JLabel k_label = new JLabel("k:");
    JLabel a_label = new JLabel("a:");
    JLabel c_label = new JLabel("c:");
    JLabel T_label = new JLabel("T, с: ");
    JLabel N_label = new JLabel("N:");
    JLabel t_N_label = new JLabel("t_N:");
    TextField l_field = new TextField();
    TextField T_field = new TextField();
    TextField k_field = new TextField();
    TextField a_field = new TextField();
    TextField c_field = new TextField();
    JButton button = new JButton("Рассчет");

    JPanel graphPanel = new JPanel();
    RealConstants  realConstants = new RealConstants();
    boolean evaluating;

    DatasetHelper datasetHelper;


    public OldMainFrame() {
        super("Term conductivity");
        initTSlider(T_slider);
        initSlider(N_slider);
        initSlider(t_N_slider);
        initChart();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(1300, 700);
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        panel.setSize(300, 300);
        this.setResizable(false);
        panel.add(graphPanel);
        panel.add(T_label);
        panel.add(N_label);
        addEmpty(350);
        panel.add(t_N_label);
        panel.add(T_slider);
        panel.add(N_slider);
        panel.add(t_N_slider);
        l_label.setPreferredSize(new Dimension(100, 23));
        l_field.setPreferredSize(new Dimension(50, 23));
        l_label.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(l_label);
        panel.add(l_field);
        addRightContent(k_label, k_field, true);
        addRightContent(a_label, a_field, false);
        addRightContent(c_label, c_field, true);

        T_label.setPreferredSize(new Dimension(500, 30));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                datasetHelper.evaluate();
            }
        });
        button.setPreferredSize(new Dimension(300, 40));
        addEmpty(600);
        panel.add(button);
//        panel.add(l_slider);
//        panel.add(l_tf);
//        panel.add(T_tf);
//        panel.add(T_slider);
//        panel.add(K_label);
//        panel.add(K_slider);
//        panel.add(K_tf);
//        panel.add(a_label);
//        panel.add(a_slider);

//        panel.add(a_tf);
//        panel.add(N_label);
//        panel.add(N_slider);

//        panel.add(N_tf);
        this.setVisible(true);
        this.add(panel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        init();
        evaluate();
    }

    private void addEmpty(int size){
        JLabel empty = new JLabel();
         empty.setPreferredSize(new Dimension(size,15));
        panel.add(empty);
    }
    private void evaluate(){
        if (evaluating) return;
        evaluating = true;
        realConstants.l = Double.parseDouble(l_field.getText());
        realConstants.T = T_slider.getValue();
        realConstants.N = N_slider.getValue();
        realConstants.t_N = t_N_slider.getValue();
        realConstants.k = (Double.parseDouble(k_field.getText()));
        realConstants.a = Double.parseDouble(a_field.getText());
        //realConstants.eps = Double.parseDouble(eps_field.getText());
        realConstants.c = Double.parseDouble(c_field.getText());
        datasetHelper.evaluate();
        refreshLabels();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                evaluating = false;
            }
        }, 100);

    }

    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Bar conductivity",      // chart title
                "Длина стержня, l",                      // x axis label
                "Температура стержня, U",                      // y axis label
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



    private void init() {
        l_field.setText(String.valueOf(realConstants.l));
        T_field.setText(String.valueOf(realConstants.T));
        k_field.setText(String.valueOf(realConstants.k));
        a_field.setText(String.valueOf(realConstants.a));
        //eps_field.setText(String.valueOf(realConstants.eps));
        c_field.setText(String.valueOf(realConstants.c));
//        l_tf.setText(String.valueOf(util.Constants.l));
//        T_tf.setText(String.valueOf(util.Constants.T));
//        K_tf.setText(String.valueOf(util.Constants.K));
//        a_tf.setText(String.valueOf(util.Constants.a));
//        N_tf.setText(String.valueOf(util.Constants.N));
    }

    private void addRightContent(JLabel label, Component textField, boolean right) {
        if (right) {
            label.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        label.setPreferredSize(new Dimension(50, 23));
        textField.setPreferredSize(new Dimension(50, 23));
        panel.add(label);
        panel.add(textField);
    }

    private void initSlider(JSlider slider) {
        slider.setPreferredSize(new Dimension(400, 45));
        slider.setMajorTickSpacing(100);
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                evaluate();
            }
        });
    }

    private void initTSlider(JSlider slider) {
        slider.setPreferredSize(new Dimension(400, 45));
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                evaluate();
            }
        });
    }


    private void initChart() {
        datasetHelper = new DatasetHelper(realConstants);
        JFreeChart chart = createChart(datasetHelper.createDataset());
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1100, 450));
        graphPanel.removeAll();
        graphPanel.add(chartPanel, BorderLayout.WEST);
        //graphPanel.validate();
    }

    private void refreshLabels() {
        l_label.setText("L:" + String.valueOf(realConstants.l));
        T_label.setText("T:" + String.valueOf(realConstants.T) + " c.");
        k_label.setText("k:" + String.valueOf(realConstants.k * realConstants.c * 4));
        a_label.setText("a:" + String.valueOf(realConstants.a));
        //eps_label.setText("Точность рассчета An: " + String.valueOf(realConstants.eps));
        c_label.setText("c:");
        N_label.setText("N:" + String.valueOf(realConstants.N) );
        t_N_label.setText("T_N:" + String.valueOf(realConstants.t_N));


    }
}
