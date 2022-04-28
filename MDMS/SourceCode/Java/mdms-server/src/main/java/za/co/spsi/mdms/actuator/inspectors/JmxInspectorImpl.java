package za.co.spsi.mdms.actuator.inspectors;

import javax.enterprise.context.ApplicationScoped;
import javax.management.ObjectName;
import java.lang.management.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class JmxInspectorImpl implements JmxInspector {

    Logger logger = java.util.logging.Logger.getLogger(JmxInspectorImpl.class.getName());

    public int getProcessCPU() {
        try {
            // No direct method in the operating system class to get the CPU load
            double cpuUsage = 0;
            return (int) (cpuUsage * 100);
        } catch (Exception e) {
            return -1;
        }
    }

    public Map<String, Long> getHeapMemory() {
        Map<String, Long> heapMemory = new HashMap<>();
        try {
            MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
            heapMemory.put("Used", memory.getHeapMemoryUsage().getUsed() / 1024);
            heapMemory.put("Committed", memory.getHeapMemoryUsage().getCommitted() / 1024);
            heapMemory.put("Init", memory.getHeapMemoryUsage().getInit() / 1024);
            heapMemory.put("Max", memory.getHeapMemoryUsage().getMax() / 1024);
            return heapMemory;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return heapMemory;
    }

    public Map<String, Integer> getThreadDetails() {
        Map<String, Integer> threadDetails = new HashMap<>();
        try {
            ThreadMXBean threadsMXBean = ManagementFactory.getThreadMXBean();
            threadDetails.put("Threads", threadsMXBean.getThreadCount());
            threadDetails.put("Threads Peak", threadsMXBean.getPeakThreadCount());
            threadDetails.put("Threads Daemon", threadsMXBean.getDaemonThreadCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return threadDetails;
    }

    public ThreadInfo[] getThreadInfo() {
        ThreadInfo[] info = new ThreadInfo[0];
        try {
            ThreadMXBean threadsMXBean = ManagementFactory.getThreadMXBean();
            info = threadsMXBean.getThreadInfo(threadsMXBean.getAllThreadIds());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public Map<String, Long> getLoadedClassesInfo() {
        Map<String, Long> loadedClassesInfo = new HashMap<>();
        try {
            ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
            loadedClassesInfo.put("Classes", classLoadingMXBean.getTotalLoadedClassCount());
            loadedClassesInfo.put("Classes Loaded", (long) classLoadingMXBean.getLoadedClassCount());
            loadedClassesInfo.put("Classes Unloaded", classLoadingMXBean.getUnloadedClassCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadedClassesInfo;
    }

    @Override
    public Map<String, Long> getGCInfo() {
        return null;
    }

    @Override
    public String getWildflyManagementPort() {
        String port = "9990";
        String offset = "0";
        try {
            port = ManagementFactory.getPlatformMBeanServer().getAttribute(new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=management-http"), "port").toString();
            offset = ManagementFactory.getPlatformMBeanServer().getAttribute(new ObjectName("jboss.as:socket-binding-group=standard-sockets"), "port-offset").toString();
        } catch(Exception ex) {
            logger.severe(ex.getMessage());
        }
        return String.format("%d",(Integer.parseInt(port) + Integer.parseInt(offset)));
    }
}
