package com.github.taichi3012.thelowbuffhud.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TaskScheduler {

  public static final int ENDLESS_LOOP = -1;

  private static final List<TaskData> tasks = new LinkedList<>();

  public static int scheduleTask(Runnable task, int waitTicks, int interval, int loopCount) {
    if (waitTicks < 0 || interval <= 0 || (loopCount <= 0 && loopCount != ENDLESS_LOOP))
      throw new IllegalArgumentException();
    TaskData taskData = new TaskData(task, waitTicks, interval, loopCount);
    tasks.add(taskData);
    return taskData.hashCode();
  }

  public static int scheduleTask(Runnable task, int waitTicks) {
    if (waitTicks < 0)
      throw new IllegalArgumentException();
    return scheduleTask(task, waitTicks, 1, 1);
  }

  public static int scheduleTaskAtTickEnd(Runnable task) {
    return scheduleTask(task, 0);
  }

  public static boolean isAliveTask(int taskHash) {
    return tasks.stream().anyMatch(taskData -> taskData.hashCode() == taskHash);
  }

  public static boolean cancelTask(int taskHash) {
    return tasks.removeIf(taskData -> taskData.hashCode() == taskHash);
  }

  @SubscribeEvent
  public void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END) {
      return;
    }

    Iterator<TaskData> itr = tasks.listIterator();
    while (itr.hasNext()) {
      TaskData data = itr.next();
      if (data.remainingTicks-- > 0) {
        continue;
      }
      data.task.run();
      if (data.remainingCount == ENDLESS_LOOP || --data.remainingCount > 0) {
        data.remainingTicks = data.interval - 1;
        continue;
      }
      itr.remove();
    }
  }

  private static class TaskData {

    Runnable task;

    int remainingTicks;

    int interval;

    int remainingCount;

    private TaskData(Runnable task, int waitTicks, int interval, int loopCount) {
      this.task = task;
      this.interval = interval;
      this.remainingTicks = waitTicks;
      this.remainingCount = loopCount;
    }
  }
}
