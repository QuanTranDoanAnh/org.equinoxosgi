package org.equinoxosgi.toast.dev.airbag;

public interface IAirbag {

	void addLIstener(IAirbagListener listener);

	void removeListener(IAirbagListener listener);

}