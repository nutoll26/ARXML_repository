package kr.ac.kookmin.cs.xquery;

public class QueryForAssemblySheet {
	static String queryAssemblySheet(String ecuName){
		String query = 
				"xquery version \"3.1\";"
				+ "declare default element namespace \"http://autosar.org/schema/r4.0\";"
				+ "declare namespace xsi=\"http://www.w3.org/2001/XMLSchema-instance\";"
				+ "declare namespace schemaLocation=\"http://autosar.org/schema/r4.0 autosar_4-2-2.xsd\";"
				
				+ "for $CSWC in doc(\"nutoll/AUTOSAR_MOD_AISpecificationExamples.arxml\")//AR-PACKAGE/ELEMENTS/COMPOSITION-SW-COMPONENT-TYPE\n"
				+ "where $CSWC/SHORT-NAME = \"" + ecuName + "\"\n"
				
				+ "";
		return query;
	}
}
