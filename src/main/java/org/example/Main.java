package org.example;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

public class Main extends JFrame {
  private final JLabel totalMemoryLabel;
  private final JLabel usedMemoryLabel;
  private final JLabel freeMemoryLabel;
  private final JLabel usedPercentageLabel;
  private final JPanel systemInfoPanel;
  private final ChartPanel memoryChartPanel;
  private final JTable processTable;

  private final GlobalMemory memory;
  private final OperatingSystem os;

  public Main() {
    super("System Monitor");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(800, 700));

    // System Information Panel
    systemInfoPanel = new JPanel(new GridLayout(5, 1));
    systemInfoPanel.setBorder(BorderFactory.createTitledBorder("Memory Information"));
    systemInfoPanel.setPreferredSize(new Dimension(250, 0));
    add(systemInfoPanel, BorderLayout.WEST);

    totalMemoryLabel = createLabel("Total Memory (MB): ");
    usedMemoryLabel = createLabel("Used Memory (MB): ");
    freeMemoryLabel = createLabel("Free Memory (MB): ");
    usedPercentageLabel = createLabel("Used Memory (%): ");

    addLabel(totalMemoryLabel);
    addLabel(usedMemoryLabel);
    addLabel(freeMemoryLabel);
    addLabel(usedPercentageLabel);

    // Memory Chart
    JFreeChart memoryChart = createMemoryChart();
    memoryChartPanel = new ChartPanel(memoryChart);
    memoryChartPanel.setPreferredSize(new Dimension(300, 300));
    memoryChartPanel.setBorder(BorderFactory.createTitledBorder("Memory Usage"));
    add(memoryChartPanel, BorderLayout.CENTER);

    // Process Table
    String[] columnNames = {"PID", "Process Name", "Memory (MB)", "CPU (%)"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    processTable = new JTable(tableModel);
    processTable.setFillsViewportHeight(true);
    JScrollPane scrollPane = new JScrollPane(processTable);
    scrollPane.setBorder(BorderFactory.createTitledBorder("Top Processes"));
    add(scrollPane, BorderLayout.SOUTH);

    SystemInfo si = new SystemInfo();
    memory = si.getHardware().getMemory();
    os = si.getOperatingSystem();
  }

  private JLabel createLabel(String text) {
    JLabel label = new JLabel(text);
    label.setFont(label.getFont().deriveFont(Font.BOLD));
    return label;
  }

  private void addLabel(JLabel label) {
    systemInfoPanel.add(label);
  }

  private JFreeChart createMemoryChart() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    dataset.addValue(0, "Memory", "Used");
    dataset.addValue(0, "Memory", "Free");

    return ChartFactory.createBarChart(
        null, null, "Memory (MB)", dataset);
  }

  public void updateData() {
    long totalMemory = memory.getTotal() / 1024 / 1024;
    long availableMemory = memory.getAvailable() / 1024 / 1024;
    long usedMemory = totalMemory - availableMemory;
    double usedPercentage = (double) usedMemory / totalMemory * 100;

    DecimalFormat df = new DecimalFormat("#.##");
    usedPercentageLabel.setText("Used Memory (%): " + df.format(usedPercentage));

    totalMemoryLabel.setText("Total Memory (MB): " + totalMemory);
    usedMemoryLabel.setText("Used Memory (MB): " + usedMemory);
    freeMemoryLabel.setText("Free Memory (MB): " + availableMemory);

    DefaultCategoryDataset dataset = (DefaultCategoryDataset) memoryChartPanel.getChart().getCategoryPlot().getDataset();
    dataset.setValue(usedMemory, "Memory", "Used");
    dataset.setValue(availableMemory, "Memory", "Free");

    DefaultTableModel tableModel = (DefaultTableModel) processTable.getModel();
    tableModel.setRowCount(0);

    List<OSProcess> procs = os.getProcesses();
    procs.sort(Comparator.comparingDouble(p -> -100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime()));

    for (OSProcess p : procs) {
      tableModel.addRow(new Object[]{
          p.getProcessID(),
          p.getName(),
          FormatUtil.formatBytes(p.getResidentSetSize()),
          df.format(100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime())
      });
    }
  }

  public static void main(String[] args) {
    Main main = new Main();
    main.pack();
    main.setVisible(true);

    Timer timer = new Timer(1000, e -> main.updateData());
    timer.start();
  }
}
