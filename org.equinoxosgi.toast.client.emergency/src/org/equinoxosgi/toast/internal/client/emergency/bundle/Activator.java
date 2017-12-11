package org.equinoxosgi.toast.internal.client.emergency.bundle;

import org.equinoxosgi.toast.dev.airbag.IAirbag;
import org.equinoxosgi.toast.dev.gps.IGps;
import org.equinoxosgi.toast.internal.client.emergency.EmergencyMonitor;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

@SuppressWarnings("rawtypes")
public class Activator implements BundleActivator {

	private IAirbag airbag;
	private ServiceTracker airbagTracker;
	private BundleContext context;
	private IGps gps;
	private ServiceTracker gpsTracker;
	private EmergencyMonitor monitor;
	
	private void bind() {
		if (gps == null) {
			gps = (IGps) gpsTracker.getService();
			if (gps == null) {
				return; // No IGps service
			}
		}
		if (airbag == null) {
			airbag = (IAirbag) airbagTracker.getService();
			if (airbag == null) {
				return; // No IAirbag service
			}
		}
		monitor.setGps(gps);
		monitor.setAirbag(airbag);
		monitor.startup();
	}
	
	private ServiceTrackerCustomizer createAirbagCustomizer() {
		return new ServiceTrackerCustomizer() {

			@SuppressWarnings("unchecked")
			@Override
			public Object addingService(ServiceReference reference) {
				Object service = context.getService(reference);
				synchronized (Activator.this) {
					if (Activator.this.airbag == null) {
						Activator.this.airbag = (IAirbag) service;
						Activator.this.bind();
					}
				}
				return service;
			}

			@Override
			public void modifiedService(ServiceReference reference, Object service) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void removedService(ServiceReference reference, Object service) {
				synchronized (Activator.this) {
					if (service != Activator.this.airbag) {
						return;
					}
					Activator.this.unbind();
					Activator.this.bind();
				}
				
			}
			
		};
	}
	
	private ServiceTrackerCustomizer createGpsCustomizer() {
		return new ServiceTrackerCustomizer() {

			@SuppressWarnings("unchecked")
			@Override
			public Object addingService(ServiceReference reference) {
				Object service = context.getService(reference);
				synchronized (Activator.this) {
					if (Activator.this.gps == null) {
						Activator.this.gps = (IGps) service;
						Activator.this.bind();
					}
				}
				return service;
			}

			@Override
			public void modifiedService(ServiceReference reference, Object service) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void removedService(ServiceReference reference, Object service) {
				synchronized (Activator.this) {
					if (service != Activator.this.gps) {
						return;
					}
					Activator.this.unbind();
					Activator.this.bind();
				}
				
			}
			
		};
	}
	
	protected void unbind() {
		if (gps == null || airbag == null) {
			return;
		}
		monitor.shutdown();
		gps = null;
		airbag = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(BundleContext context) throws Exception {
		this.context = context;
		monitor = new EmergencyMonitor();
		ServiceTrackerCustomizer gpsCustomizer = createGpsCustomizer();
		gpsTracker = new ServiceTracker<>(context, IGps.class.getName(), gpsCustomizer);
		ServiceTrackerCustomizer airbagCustomizer = createAirbagCustomizer();
		airbagTracker = new ServiceTracker<>(context, IAirbag.class.getName(), airbagCustomizer);
		gpsTracker.open();
		airbagTracker.open();

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		airbagTracker.close();
		gpsTracker.close();
	}

}
