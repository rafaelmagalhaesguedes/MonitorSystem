package org.example.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 * The type System view.
 */
public class SystemView extends JFrame {
  private final JLabel totalMemoryLabel;
  private final JLabel usedMemoryLabel;
  private final JLabel freeMemoryLabel;
  private final JLabel usedPercentageLabel;
  private final JPanel systemInfoPanel;
  private final ChartPanel memoryChartPanel;
  private final ChartPanel cpuChartPanel;
  private final JTable processTable;

  /**
   * Instantiates a new System view.
   */
  public SystemView() {
    super("System Monitor");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(800, 700));

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    // System Information Panel
    systemInfoPanel = new JPanel(new GridLayout(5, 1));
    systemInfoPanel.setBorder(BorderFactory.createTitledBorder("Memory Information"));
    systemInfoPanel.setPreferredSize(new Dimension(250, 0));
    mainPanel.add(systemInfoPanel, BorderLayout.WEST);

    totalMemoryLabel = createLabel("Total Memory (MB): ");
    usedMemoryLabel = createLabel("Used Memory (MB): ");
    freeMemoryLabel = createLabel("Free Memory (MB): ");
    usedPercentageLabel = createLabel("Used Memory (%): ");

    addLabel(totalMemoryLabel);
    addLabel(usedMemoryLabel);
    addLabel(freeMemoryLabel);
    addLabel(usedPercentageLabel);

    // Memory Chart
    memoryChartPanel = new ChartPanel(null);
    memoryChartPanel.setPreferredSize(new Dimension(300, 300));
    memoryChartPanel.setBorder(BorderFactory.createTitledBorder("Memory Usage"));
    mainPanel.add(memoryChartPanel, BorderLayout.CENTER);

    // CPU Chart
    cpuChartPanel = new ChartPanel(null);
    cpuChartPanel.setPreferredSize(new Dimension(300, 300));
    cpuChartPanel.setBorder(BorderFactory.createTitledBorder("CPU Usage"));
    mainPanel.add(cpuChartPanel, BorderLayout.EAST);

    // Process Table
    String[] columnNames = {"PID", "Process Name", "Memory (MB)", "CPU (%)"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    processTable = new JTable(tableModel);
    processTable.setFillsViewportHeight(true);
    JScrollPane scrollPane = new JScrollPane(processTable);
    scrollPane.setBorder(BorderFactory.createTitledBorder("Top Processes"));
    mainPanel.add(scrollPane, BorderLayout.SOUTH);

    add(mainPanel);
  }

  private JLabel createLabel(String text) {
    JLabel label = new JLabel(text);
    label.setFont(label.getFont().deriveFont(Font.BOLD));
    return label;
  }

  private void addLabel(JLabel label) {
    systemInfoPanel.add(label);
  }

  /**
   * Update memory chart.
   *
   * @param chart the chart
   */
  public void updateMemoryChart(JFreeChart chart) {
    memoryChartPanel.setChart(chart);
  }

  /**
   * Update cpu chart.
   *
   * @param chart the chart
   */
  public void updateCPUChart(JFreeChart chart) {
    cpuChartPanel.setChart(chart);
  }

  /**
   * Update process table.
   *
   * @param tableModel the table model
   */
  public void updateProcessTable(DefaultTableModel tableModel) {
    processTable.setModel(tableModel);
  }

  /**
   * Update system info.
   *
   * @param totalMemory    the total memory
   * @param usedMemory     the used memory
   * @param freeMemory     the free memory
   * @param usedPercentage the used percentage
   */
  public void updateSystemInfo(String totalMemory, String usedMemory, String freeMemory, String usedPercentage) {
    totalMemoryLabel.setText("Total Memory (MB): " + totalMemory);
    usedMemoryLabel.setText("Used Memory (MB): " + usedMemory);
    freeMemoryLabel.setText("Free Memory (MB): " + freeMemory);
    usedPercentageLabel.setText("Used Memory (%): " + usedPercentage);
  }

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      SystemView view = new SystemView();
      view.pack();
      view.setVisible(true);
    });
  }
}
