package kr.ac.kookmin.cs.Xquery;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;
import java.util.Vector;

import javax.xml.transform.OutputKeys;

import org.exist.Resource;
import org.exist.util.serializer.SAXSerializer;
import org.exist.util.serializer.SerializerPool;
import org.exist.xmldb.XQueryService;
import org.exist.xmldb.XmldbURI;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

public class NetworkingExist {
	
	final private String URI = "";
	final private String driver = "org.exist.xmldb.DatabaseImpl";

	private XQueryService service;
	
	public NetworkingExist() {

		try {
			Class<?> cl = Class.forName(driver);
			Database database = (Database) cl.newInstance();
			database.setProperty("create-database", "true");
			DatabaseManager.registerDatabase(database);

			// get root-collection
			Collection col = DatabaseManager.getCollection(URI + XmldbURI.ROOT_COLLECTION);
			// get query-service
			service = (XQueryService) col.getService("XQueryService", "1.0");

			// set pretty-printing on
			service.setProperty(OutputKeys.INDENT, "yes");
			service.setProperty(OutputKeys.ENCODING, "UTF-8");

			Properties outputProperties = new Properties();
			outputProperties.setProperty(OutputKeys.INDENT, "yes");
			SAXSerializer serializer = (SAXSerializer) SerializerPool.getInstance().borrowObject(SAXSerializer.class);
			serializer.setOutput(new OutputStreamWriter(System.out), outputProperties);

			SerializerPool.getInstance().returnObject(serializer);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void SendQueryToExistDB(String ecuName) throws XMLDBException{
		
		String query = "declare default element namespace \"http://autosar.org/schema/r4.0\";"
				+ "declare namespace xsi = \"http://www.w3.org/2001/XMLSchema-instance\";"
				+ "declare namespace schemaLocation = \"http://autosar.org/schema/r4.0 autosar_4-2-2.xsd\";"
				+ "for $CSWC in doc(\"/db/nutoll/AUTOSAR_MOD_AISpecificationExamples.arxml\")//AR-PACKAGE/ELEMENTS/COMPOSITION-SW-COMPONENT-TYPE\n"
				+ "where $CSWC/SHORT-NAME = \"" + ecuName +"\"\n"
				+ "return $CSWC/SHORT-NAME";
		CompiledExpression compiled = service.compile(query);

		// execute query and get results in ResourceSet
		ResourceSet result = service.execute(compiled);
		
		ResourceIterator i = result.getIterator();
		while (i.hasMoreResources()) {
			org.xmldb.api.base.Resource r = i.nextResource();
			String value = (String) r.getContent();
			System.out.println(value);
		}
	}

	/*
	 * String readFile(String file) throws IOException { try (BufferedReader f =
	 * new BufferedReader(new FileReader(file))) { String line; StringBuffer xml
	 * = new StringBuffer(); while ((line = f.readLine()) != null) {
	 * xml.append(line); } return xml.toString(); } }
	 */
}
