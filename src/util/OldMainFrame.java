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
    JLabel l_label = new JLabel("Длина стержня, м: ");
    //    JTextField l_tf = new JTextField(6);
//    JSlider l_slider = new JSlider(1, 100, 5);
    JLabel T_label = new JLabel("Время, с: ");
    //    JTextField T_tf = new JTextField(6);
    JSlider T_slider = new JSlider(0, 100, 10);
    JSlider N_slider = new JSlider(4, 1000, 10);
    JSlider t_N_slider = new JSlider(4, 1000, 10);
    JLabel K_label = new JLabel("Коэфицент теплопроводности:");
    //    JSlider K_slider = new JSlider(1, 100, 40);
    //    JTextField K_tf = new JTextField(6);
    JLabel a_label = new JLabel("Коэфицент теплообмена среды:");
    JLabel N_label = new JLabel("Количество членов ряда");
    JLabel eps_label = new JLabel("Точность вычисления An");
    JLabel c_label = new JLabel("Коэффициент объемной теплоемкости");
    //    JSlider a_slider = new JSlider(1, 100000, 30);
    //    JTextField a_tf = new JTextField(6);
//    JLabel N_label = new JLabel("Количество членов Фурье: ");
//    JSlider N_slider = new JSlider(10, 100, 30);
    TextField l_field = new TextField();
    TextField T_field = new TextField();
    TextField K_field = new TextField();
    TextField a_field = new TextField();
    TextField N_field = new TextField();
    TextField eps_field = new TextField();
    TextField c_field = new TextField();
    JLabel emptyLabel = new JLabel("                                                 ");
    JButton button = new JButton("Рассчет");

    //    JTextField N_tf = new JTextField(6);
    JPanel graphPanel = new JPanel();
    XYSeriesCollection dataset = new XYSeriesCollection();
    RealConstants  realConstants = new RealConstants();
    OldConductivitySolver solver = new OldConductivitySolver();
    DiffSolver diffSolver = new DiffSolver();
    boolean evaluating;


    public OldMainFrame() {
        super("Term conductivity");
        initSlider(T_slider);
        initSlider(N_slider);
        initSlider(t_N_slider);
//        initSlider(l_slider);
//        initSlider(K_slider);
//        initSlider(a_slider);
//        initSlider(N_slider);
        initChart();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(1300, 700);
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        panel.setSize(300, 300);
        this.setResizable(false);
        panel.add(graphPanel);
        emptyLabel.setPreferredSize(new Dimension(550, 23));
        panel.add(emptyLabel);
        l_label.setPreferredSize(new Dimension(200, 23));
        l_field.setPreferredSize(new Dimension(50, 23));
        l_label.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(l_label);
        panel.add(l_field);
//        addRightContent(T_label, T_field, true);
        addRightContent(K_label, K_field, true);
        addRightContent(a_label, a_field, false);
        addRightContent(N_label, N_field, true);
        addRightContent(eps_label, eps_field, false);
        addRightContent(c_label, c_field, true);
        panel.add(emptyLabel);

        T_label.setPreferredSize(new Dimension(500, 30));
        panel.add(T_slider);
        panel.add(N_slider);
        panel.add(t_N_slider);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                evaluate();
            }
        });
        button.setPreferredSize(new Dimension(300, 40));
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
        final XYSeries series2 = diffSolver.createDataset();
        double x = 0;
        for (int i = 0; i <= 10; i++) {
            double res = solver.find_V(x, solver.T);
            series1.add(x, res);
            series0.add(x, sin(PI * x / solver.l) + Constants.u0);
            System.out.println();
            x += solver.l / 10;
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
        dataset.addSeries(series2);
    }

    private void init() {
        l_field.setText(String.valueOf(realConstants.l));
        T_field.setText(String.valueOf(realConstants.T));
        K_field.setText(String.valueOf(realConstants.K));
        a_field.setText(String.valueOf(realConstants.a));
        N_field.setText(String.valueOf(realConstants.N));
        eps_field.setText(String.valueOf(realConstants.eps));
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
        realConstants.l = Double.parseDouble(l_field.getText());
        realConstants.T = T_slider.getValue();
        realConstants.N = N_slider.getValue();
        realConstants.t_N = t_N_slider.getValue();
        realConstants.K = (Double.parseDouble(K_field.getText()) / 4) / Double.parseDouble(c_field.getText());
        realConstants.a = Double.parseDouble(a_field.getText());
        realConstants.N = Integer.parseInt(N_field.getText());
        realConstants.eps = Double.parseDouble(eps_field.getText());
        realConstants.c = Double.parseDouble(c_field.getText());
        solver.refresh(realConstants);
        diffSolver.refresh(realConstants);
//        solver.N = N_slider.getValue();
    }

    private void initSlider(JSlider slider) {
        slider.setPreferredSize(new Dimension(900, 45));
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
        chartPanel.setPreferredSize(new Dimension(1100, 450));
        graphPanel.removeAll();
        graphPanel.add(chartPanel, BorderLayout.WEST);
        //graphPanel.validate();
    }

    private void refreshLabels() {
        l_label.setText("Длина стержня: " + String.valueOf(realConstants.l));
        T_label.setText("Время " + String.valueOf(realConstants.T) + " c.");
        K_label.setText("Коэфицент теплопроводности: " + String.valueOf(realConstants.K * realConstants.c * 4));
        a_label.setText("Коэфицент теплообмена среды: " + String.valueOf(realConstants.a));
        N_label.setText("Количество членов ряда: " + String.valueOf(realConstants.N));
        eps_label.setText("Точность рассчета An: " + String.valueOf(realConstants.eps));
        c_label.setText("Коэффициент объемной теплоемкости: ");

    }
}
