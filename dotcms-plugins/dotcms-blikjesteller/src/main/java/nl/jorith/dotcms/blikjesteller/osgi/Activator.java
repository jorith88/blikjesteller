package nl.jorith.dotcms.blikjesteller.osgi;

import com.dotcms.repackage.org.osgi.framework.BundleContext;
import com.dotmarketing.util.Logger;

import nl.jorith.dotcms.blikjesteller.rest.BlikjestellerBD;

public class Activator extends ExtendedGenericBundleActivator {

	@Override
	public void start(BundleContext ctx) throws Exception {
		try {
			initializeServices(ctx);
			
			addRestService(ctx, BlikjestellerBD.class);
			

			Logger.info(this, "Deployed Blikjesteller server plugin");
		} catch (Throwable t) {
			Logger.error(this, "Error in Blikjesteller server plugin activator", t);
			stop(ctx);
		}
	}

	@Override
	public void stop(BundleContext ctx) throws Exception {
		Logger.info(this, "Undeployed Blikjesteller server plugin");
		unregisterServices(ctx);
	}
}
