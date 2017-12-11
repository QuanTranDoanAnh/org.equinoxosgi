package org.equinoxosgi.toast.internal.dev.gps.fake.bundle;

import org.equinoxosgi.toast.dev.gps.IGps;
import org.equinoxosgi.toast.internal.dev.gps.fake.FakeGps;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {
	private ServiceRegistration<?> registration;
	
	@Override
	public void start(BundleContext context) throws Exception {
		FakeGps gps = new FakeGps();
		registration = context.registerService(IGps.class.getName(), gps, null);
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		registration.unregister();
	}

}
