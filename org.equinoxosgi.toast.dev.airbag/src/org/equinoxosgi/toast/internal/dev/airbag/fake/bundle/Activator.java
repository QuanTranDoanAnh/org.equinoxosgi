package org.equinoxosgi.toast.internal.dev.airbag.fake.bundle;

import org.equinoxosgi.toast.dev.airbag.IAirbag;
import org.equinoxosgi.toast.internal.dev.airbag.fake.FakeAirbag;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {
	private FakeAirbag airbag;
	private ServiceRegistration<?> registration;
	
	@Override
	public void start(BundleContext context) throws Exception {
		airbag = new FakeAirbag();
		airbag.startup();
		registration = context.registerService(IAirbag.class.getName(), airbag, null);
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		registration.unregister();
		airbag.shutdown();
	}

}
