package kr.ac.kookmin.cs.xquery;

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

import kr.ac.kookmin.cs.javaToExcel.ExtractExcel;

public class NetworkingExist {
	
	final private String URI = "xmldb:exist://192.168.0.16:8080/exist/xmlrpc";
	final private String driver = "org.exist.xmldb.DatabaseImpl";

	private XQueryService service;
	private ExtractExcel excel = null;
	
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
		
		String query = null;
		excel = new ExtractExcel();
		
		{
		query = QueryForDelegationSheet.queryDelegationSheet(ecuName);
		CompiledExpression compiled = service.compile(query);

		// execute query and get results in ResourceSet
		ResourceSet queryResult = service.execute(compiled);
		excel.createDelegationSheet(queryResult, ecuName);		
		}
		
		{
			query = QueryForAssemblySheet.queryAssemblySheet(ecuName);
			CompiledExpression compiled = service.compile(query);

			// execute query and get results in ResourceSet
			ResourceSet queryResult = service.execute(compiled);
			excel.createAssemblySheet(queryResult, ecuName);
		}
//		ResourceIterator i = queryResult.getIterator();
//		
//		while (i.hasMoreResources()) {
//			org.xmldb.api.base.Resource r = i.nextResource();
//			String result = (String) r.getContent();
//			System.out.println(result);
//		}
		
		excel.createExcelFile(ecuName);
		excel = null;
	}

	/*
	 * String readFile(String file) throws IOException { try (BufferedReader f =
	 * new BufferedReader(new FileReader(file))) { String line; StringBuffer xml
	 * = new StringBuffer(); while ((line = f.readLine()) != null) {
	 * xml.append(line); } return xml.toString(); } }
	 */
}
