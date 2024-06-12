package org.example.model;

import java.util.Comparator;
import java.util.List;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

public class SystemModel {

  private final GlobalMemory memory;
  private final OperatingSystem os;

  public SystemModel() {
    SystemInfo si = new SystemInfo();
    memory = si.getHardware().getMemory();
    os = si.getOperatingSystem();
  }

  public long getTotalMemory() {
    return memory.getTotal() / 1024 / 1024;
  }

  public long getUsedMemory() {
    long availableMemory = memory.getAvailable() / 1024 / 1024;
    return getTotalMemory() - availableMemory;
  }

  public double getUsedPercentage() {
    return (double) getUsedMemory() / getTotalMemory() * 100;
  }

  public List<OSProcess> getProcesses() {
    List<OSProcess> procs = os.getProcesses();
    procs.sort(Comparator.comparingDouble(p -> -100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime()));
    return procs;
  }
}
