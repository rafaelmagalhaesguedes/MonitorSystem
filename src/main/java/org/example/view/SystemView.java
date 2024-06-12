package org.example.view;

import static com.sun.tools.javac.util.Constants.format;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import oshi.software.os.OSProcess;
import oshi.util.FormatUtil;

public class SystemView extends JFrame {
  private JLabel timeLabel;
  private JLabel totalMemoryLabel;
  private JLabel usedMemoryLabel;
  private JLabel freeMemoryLabel;
  private JLabel usedPercentageLabel;
  private final JTable processTable;

  public SystemView() {
    setTitle("System Monitor");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(800, 700));

    JPanel systemInfoPanel = createSystemInfoPanel();
    add(systemInfoPanel, BorderLayout.WEST);

    processTable = new JTable();
    JScrollPane scrollPane = new JScrollPane(processTable);
    scrollPane.setBorder(BorderFactory.createTitledBorder("Top Processes"));
    add(scrollPane, BorderLayout.CENTER);
  }

  private JPanel createSystemInfoPanel() {
    JPanel systemInfoPanel = new JPanel(new GridLayout(5, 1));
    systemInfoPanel.setBorder(BorderFactory.createTitledBorder("System Information"));
    systemInfoPanel.setPreferredSize(new Dimension(250, 0));

    timeLabel = createLabel("Time: ");
    totalMemoryLabel = createLabel("Total Memory (MB): ");
    usedMemoryLabel = createLabel("Used Memory (MB): ");
    freeMemoryLabel = createLabel("Free Memory (MB): ");
    usedPercentageLabel = createLabel("Used Memory (%): ");

    systemInfoPanel.add(timeLabel);
    systemInfoPanel.add(totalMemoryLabel);
    systemInfoPanel.add(usedMemoryLabel);
    systemInfoPanel.add(freeMemoryLabel);
    systemInfoPanel.add(usedPercentageLabel);

    return systemInfoPanel;
  }

  private JLabel createLabel(String text) {
    JLabel label = new JLabel(text);
    label.setFont(label.getFont().deriveFont(Font.BOLD));
    return label;
  }

  public void updateData(long totalMemory, long usedMemory, double usedPercentage, List<OSProcess> processes) {
    DecimalFormat df = new DecimalFormat("#.##");

    timeLabel.setText("Time: " + java.time.LocalTime.now().toString());
    totalMemoryLabel.setText("Total Memory (MB): " + totalMemory);
    usedMemoryLabel.setText("Used Memory (MB): " + usedMemory);
    freeMemoryLabel.setText("Free Memory (MB): " + (totalMemory - usedMemory));
    usedPercentageLabel.setText("Used Memory (%): " + df.format(usedPercentage));

    updateProcessTable(processes);
  }

  private void updateProcessTable(List<OSProcess> processes) {
    DefaultTableModel tableModel = (DefaultTableModel) processTable.getModel();
    tableModel.setRowCount(0);

    for (OSProcess p : processes) {
      tableModel.addRow(new Object[]{
          p.getProcessID(),
          p.getName(),
          FormatUtil.formatBytes(p.getResidentSetSize())
      });
    }
  }
}
