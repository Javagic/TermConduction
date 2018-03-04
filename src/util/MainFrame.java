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
import solvers.ConductivitySolver;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
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
public class MainFrame extends JFrame {

    JPanel panel = new JPanel();
    JLabel l_label = new JLabel("Длина стержня, м * E-1: ");
    //    JTextField l_tf = new JTextField(6);
    JSlider l_slider = new JSlider(1, 100, 5);
    JLabel T_label = new JLabel("Время, с: ");
    //    JTextField T_tf = new JTextField(6);
    JSlider T_slider = new JSlider(0, 100, 10);
    JLabel K_label = new JLabel("Коэфицент теплопроводности: E-3");
    JSlider K_slider = new JSlider(1, 100, 40);
    //    JTextField K_tf = new JTextField(6);
    JLabel a_label = new JLabel("Коэфицент теплообмена среды: E-7");
    JSlider a_slider = new JSlider(1, 100000, 30);
    //    JTextField a_tf = new JTextField(6);
//    JLabel N_label = new JLabel("Количество членов Фурье: ");
//    JSlider N_slider = new JSlider(10, 100, 30);

    //    JTextField N_tf = new JTextField(6);
    JPanel graphPanel = new JPanel();
    XYSeriesCollection dataset = new XYSeriesCollection();
    ConductivitySolver solver = new ConductivitySolver();
    boolean evaluating;


    public MainFrame() {
        super("Term conductivity");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        initSlider(T_slider);
        initSlider(l_slider);
        initSlider(K_slider);
        initSlider(a_slider);
//        initSlider(N_slider);
        initChart();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(1500, 900);
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2 + 50);
        panel.setSize(300, 300);
        this.setResizable(false);
        panel.add(graphPanel);
        panel.add(l_label);
        panel.add(l_slider);
//        panel.add(l_tf);
        panel.add(T_label);
//        panel.add(T_tf);
        panel.add(T_slider);
        panel.add(K_label);
        panel.add(K_slider);
//        panel.add(K_tf);
        panel.add(a_label);
        panel.add(a_slider);

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

    private void createDataset() {
        final XYSeries series0 = new XYSeries("Начальная температура");
        final XYSeries series1 = new XYSeries("Распределение температуры по длине стержня");
        try {
            FileWriter fileWriter = new FileWriter("res.txt");
            FileWriter fileWriter2 = new FileWriter("start.txt");
            double x = 0;
            for (int i = 0; i <= 10; i++) {
                double res = solver.find_V(i, solver.T);
                series1.add(x, res);
                fileWriter.write(res + "\n");
                fileWriter2.write(sin(PI * x / solver.l) + "\n");
                series0.add(x, sin(PI * x / solver.l) + Constants.u0);
                System.out.println();
                x += solver.l / 10;
            }
            fileWriter.close();
            fileWriter2.close();
        } catch (IOException er) {
            er.printStackTrace();
        }
        series0.remove(series0.getItemCount() - 1);
        series0.add(solver.l, 0);
        dataset.removeAllSeries();
        if (solver.T == 0) {
            dataset.addSeries(series0);
        } else {
            dataset.addSeries(series1);
        }
        dataset.addSeries(series0);
    }

    private void init() {
//        l_tf.setText(String.valueOf(util.Constants.l));
//        T_tf.setText(String.valueOf(util.Constants.T));
//        K_tf.setText(String.valueOf(util.Constants.K));
//        a_tf.setText(String.valueOf(util.Constants.a));
//        N_tf.setText(String.valueOf(util.Constants.N));
    }

    private void evaluate() {
        if (evaluating) return;
        evaluating = true;
        refreshSolver();
        refreshLabels();
        createDataset();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                evaluating = false;
            }
        }, 100);
    }

    private void refreshSolver() {
        solver.l = l_slider.getValue() / 10d;
        solver.T = T_slider.getValue();
        solver.K = K_slider.getValue() / 1000d;
        solver.a = a_slider.getValue() / 10000000d;
//        solver.N = N_slider.getValue();
    }

    private void initSlider(JSlider slider) {
        slider.setSize(900, 100);
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
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(850, 450));
        graphPanel.removeAll();
        graphPanel.add(chartPanel, BorderLayout.WEST);
        graphPanel.validate();
    }

    private void refreshLabels() {
        l_label.setText("Длина стержня: " + String.valueOf(solver.l) + " м * E-1");
        T_label.setText("Время " + String.valueOf(solver.T) + " c.");
        K_label.setText("Коэфицент теплопроводности: " + String.valueOf(solver.K));
        a_label.setText("Коэфицент теплообмена среды: " + String.valueOf(solver.a));

    }
}
