package org.example.model;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.List;

/**
 * The type System model.
 */
public class SystemModel {
  private final GlobalMemory memory;
  private final OperatingSystem os;

  /**
   * Instantiates a new System model.
   */
  public SystemModel() {
    SystemInfo si = new SystemInfo();
    memory = si.getHardware().getMemory();
    os = si.getOperatingSystem();
  }

  /**
   * Gets memory.
   *
   * @return the memory
   */
  public GlobalMemory getMemory() {
    return memory;
  }

  /**
   * Gets operating system.
   *
   * @return the operating system
   */
  public OperatingSystem getOperatingSystem() {
    return os;
  }

  /**
   * Gets processes.
   *
   * @return the processes
   */
  public List<OSProcess> getProcesses() {
    return os.getProcesses();
  }
}
