package edu.umls.fhir.server;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

public class UMLSFHIRServer {

	private Integer ourPort;
	private Server ourServer;
	private FhirContext ourCtx;
	private IGenericClient ourClient;

	private static final Logger log = LoggerFactory.getLogger(UMLSFHIRServer.class); 
	
	public static void main(String args[]) {
		
		try {
			new UMLSFHIRServer().startServer();
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		
	}

	public void startServer() throws Exception {
		
		ourPort = RandomServerPortProvider.findFreePort();
		ourServer = new Server(ourPort);
		String base = "http://localhost:" + ourPort + "/umlsfhir";
		System.out.println(base);
		
		WebAppContext root = new WebAppContext();
		root.setAllowDuplicateFragmentNames(true);
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.replaceAll("classes", "");
		root.setWar(new File(path+"umlsfhir.war").toURI().toString());
		root.setContextPath("/");
		ourServer.setHandler(root);

		ourServer.start();

		ourCtx = FhirContext.forDstu3();
		ourClient = ourCtx.newRestfulGenericClient(base);
	}

}
