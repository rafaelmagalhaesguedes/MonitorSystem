package org.example.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import org.example.model.SystemModel;
import org.example.view.SystemView;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSProcess;
import oshi.util.FormatUtil;

/**
 * The type System controller.
 */
public class SystemController {
  private final SystemModel model;
  private final SystemView view;

  /**
   * Instantiates a new System controller.
   *
   * @param model the model
   * @param view  the view
   */
  public SystemController(SystemModel model, SystemView view) {
    this.model = model;
    this.view = view;
  }

  /**
   * Update data.
   */
  public void updateData() {
    updateMemoryChart();
    updateCPUChart();
    updateProcessTable();
    updateSystemInfo();
  }

  private void updateMemoryChart() {
    GlobalMemory memory = model.getMemory();
    long totalMemory = memory.getTotal() / 1024 / 1024;
    long availableMemory = memory.getAvailable() / 1024 / 1024;
    long usedMemory = totalMemory - availableMemory;

    DefaultCategoryDataset memoryDataset = new DefaultCategoryDataset();
    memoryDataset.addValue(usedMemory, "Memory", "Used");
    memoryDataset.addValue(availableMemory, "Memory", "Free");
    JFreeChart memoryChart = ChartFactory.createBarChart(null, null, "Memory (MB)", memoryDataset);
    view.updateMemoryChart(memoryChart);
  }

  private void updateCPUChart() {
    DefaultCategoryDataset cpuDataset = new DefaultCategoryDataset();
    cpuDataset.addValue(getTotalCpuUsage(), "CPU", "Usage");
    JFreeChart cpuChart = ChartFactory.createBarChart(null, null, "CPU (%)", cpuDataset);
    view.updateCPUChart(cpuChart);
  }

  private void updateProcessTable() {
    DecimalFormat df = new DecimalFormat("#.##");
    DefaultTableModel tableModel = new DefaultTableModel();
    tableModel.addColumn("PID");
    tableModel.addColumn("Process Name");
    tableModel.addColumn("Memory (MB)");
    tableModel.addColumn("CPU (%)");

    List<OSProcess> procs = model.getProcesses();
    procs.sort(Comparator.comparingDouble(p -> -p.getProcessCpuLoadCumulative()));

    for (OSProcess p : procs) {
      tableModel.addRow(new Object[]{
          p.getProcessID(),
          p.getName(),
          FormatUtil.formatBytes(p.getResidentSetSize()),
          df.format(100d * p.getProcessCpuLoadCumulative())
      });
    }
    view.updateProcessTable(tableModel);
  }

  private void updateSystemInfo() {
    GlobalMemory memory = model.getMemory();
    long totalMemory = memory.getTotal() / 1024 / 1024;
    long availableMemory = memory.getAvailable() / 1024 / 1024;
    long usedMemory = totalMemory - availableMemory;
    double usedPercentage = (double) usedMemory / totalMemory * 100;
    DecimalFormat df = new DecimalFormat("#.##");

    view.updateSystemInfo(String.valueOf(totalMemory), String.valueOf(usedMemory),
        String.valueOf(availableMemory), df.format(usedPercentage));
  }

  private double getTotalCpuUsage() {
    double totalCpuUsage = 0.0;
    List<OSProcess> procs = model.getProcesses();
    for (OSProcess p : procs) {
      totalCpuUsage += p.getProcessCpuLoadCumulative();
    }
    return totalCpuUsage;
  }

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    SystemModel model = new SystemModel();
    SystemView view = new SystemView();
    SystemController controller = new SystemController(model, view);

    view.pack();
    view.setVisible(true);

    Timer timer = new Timer(1000, e -> controller.updateData());
    timer.start();
  }
}
