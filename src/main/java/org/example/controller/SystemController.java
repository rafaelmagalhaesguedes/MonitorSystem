package org.example.controller;

import java.util.List;
import javax.swing.Timer;

import org.example.model.SystemModel;
import org.example.view.SystemView;
import oshi.software.os.OSProcess;

public class SystemController {
  private final SystemModel model;
  private final SystemView view;

  public SystemController(SystemModel model, SystemView view) {
    this.model = model;
    this.view = view;
  }

  public void initialize() {
    Timer timer = new Timer(1000, e -> {
      long totalMemory = model.getTotalMemory();
      long usedMemory = model.getUsedMemory();
      double usedPercentage = model.getUsedPercentage();
      List<OSProcess> processes = model.getProcesses();
      view.updateData(totalMemory, usedMemory, usedPercentage, processes);
    });
    timer.start();
  }
}
