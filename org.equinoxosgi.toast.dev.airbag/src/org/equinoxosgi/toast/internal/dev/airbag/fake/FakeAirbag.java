package org.equinoxosgi.toast.internal.dev.airbag.fake;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.equinoxosgi.toast.dev.airbag.IAirbag;
import org.equinoxosgi.toast.dev.airbag.IAirbagListener;

public class FakeAirbag implements IAirbag {
	private List<IAirbagListener> listeners = new ArrayList<>();
	private Job job;
	private boolean isRunning;
	
	@Override
	public synchronized void addLIstener(IAirbagListener listener) {
		listeners.add(listener);
	}
	
	private synchronized void deploy() {
		for (Iterator<IAirbagListener> i = listeners.iterator(); i.hasNext();) {
			((IAirbagListener) i.next()).deployed();
		}
	}
	
	@Override
	public synchronized void removeListener(IAirbagListener listener) {
		listeners.remove(listener);
	}
	
	public synchronized void shutdown() {
		isRunning = false;
		job.cancel();
		try {
			job.join();
		} catch (InterruptedException e) {
			// shutting down, safe to ignore
		}
	}
	
	public synchronized void startup() {
		isRunning = true;
		job = new Job("FakeAirbag") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				deploy();
				if (isRunning) {
					schedule(5000);
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule(5000);
	}
}
