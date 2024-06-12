package org.example.model;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.List;

public class SystemModel {
  private final GlobalMemory memory;
  private final OperatingSystem os;

  public SystemModel() {
    SystemInfo si = new SystemInfo();
    memory = si.getHardware().getMemory();
    os = si.getOperatingSystem();
  }

  public GlobalMemory getMemory() {
    return memory;
  }

  public OperatingSystem getOperatingSystem() {
    return os;
  }

  public List<OSProcess> getProcesses() {
    return os.getProcesses();
  }
}
