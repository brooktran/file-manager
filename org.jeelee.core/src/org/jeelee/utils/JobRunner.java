package org.jeelee.utils;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.progress.UIJob;

public class JobRunner
{
  public static void runShortUserJob(Job job)
  {
    runJob(Job.SHORT, true, false, job);
  }
  public static void runJob(int priority, boolean user, Boolean system, Job job) {
    job.setPriority(priority);
    job.setUser(user);
    job.setSystem(system.booleanValue());
    job.schedule();
  }
  public static void runUIJob(UIJob job) {
    job.schedule();
  }
}

/* Location:           F:\backup\SystemDesktop\org.jeelee.core_2.1.0.201208282112.jar
 * Qualified Name:     org.jeelee.utils.JobRunner
 * JD-Core Version:    0.6.0
 */