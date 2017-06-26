package kr.ac.kookmin.cs.xquery;

public class QueryForAssemblySheet {
	static String queryAssemblySheet(String ecuName){
		String query = 
				"xquery version \"3.1\";"
				+ "declare default element namespace \"http://autosar.org/schema/r4.0\";"
				+ "declare namespace xsi=\"http://www.w3.org/2001/XMLSchema-instance\";"
				+ "declare namespace schemaLocation=\"http://autosar.org/schema/r4.0 autosar_4-2-2.xsd\";"
				
				+ "let $allCSWC := doc(\"nutoll/AUTOSAR_MOD_AISpecificationExamples.arxml\")//AR-PACKAGE/ELEMENTS/COMPOSITION-SW-COMPONENT-TYPE\n"
				
				+ "let $selectedCSWC := for $CSWC in $allCSWC\n"
				+ "    where $CSWC/SHORT-NAME = \"" + ecuName +"\"\n"
				+ "    return $CSWC\n"
				
				+ "let $assemblyConnector := $selectedCSWC/CONNECTORS/ASSEMBLY-SW-CONNECTOR\n"
				
				+ "let $assemblyTargetPport := $assemblyConnector/PROVIDER-IREF/TARGET-P-PORT-REF\n"
				
				+ "let $tokenTargetPport := for $i in (1 to count($assemblyTargetPport))\n"
				+ "    return fn:tokenize(string($assemblyTargetPport[$i]), '/')\n"
				+ "let $targetPportASWC := for $i in (1 to count($tokenTargetPport))\n"
				+ "    return if($i mod 2 = 1)\n"
				+ "        then $tokenTargetPport[$i]\n"
				+ "        else ()\n"
				+ "let $targetPportName := for $i in (1 to count($tokenTargetPport))\n"
				+ "    return if($i mod 2 = 0)\n"
				+ "        then $tokenTargetPport[$i]\n"
				+ "        else ()\n"
				+ "let $PportCategory := $assemblyTargetPport/@DEST\n"
				
				+ "let $pPortASWCcontent := for $CSWC in $allCSWC\n"
				+ "    where $CSWC/SHORT-NAME = $targetPportASWC\n"
				+ "    return $CSWC\n"
				
				+ "let $assemblyTargetRport := $assemblyConnector/REQUESTER-IREF/TARGET-R-PORT-REF\n"
				
				+ "let $tokenTargetRport := for $i in (1 to count($assemblyTargetRport))\n"
				+ "    return fn:tokenize(string($assemblyTargetRport[$i]), '/')\n"
				+ "let $targetRportASWC := for $i in (1 to count($tokenTargetRport))\n"
				+ "    return if($i mod 2 = 1)\n"
				+ "        then $tokenTargetRport[$i]\n"
				+ "        else ()\n"
				+ "let $targetRportName := for $i in (1 to count($tokenTargetRport))\n"
				+ "    return if($i mod 2 = 0)\n"
				+ "        then $tokenTargetRport[$i]\n"
				+ "        else ()\n"
				+ "let $RportCategory := $assemblyTargetRport/@DEST\n"
				
				+ "let $rPortASWCcontent := for $CSWC in $allCSWC\n"
				+ "    where $CSWC/SHORT-NAME = $targetRportASWC\n"
				+ "    return $CSWC\n"
				
				+ "for $i in (1 to count($targetPportASWC))\n"
				+ "    let $pPortSelectedASWC := for $p in (1 to count($pPortASWCcontent))\n"
				+ "                            where $targetPportASWC[$i] = $pPortASWCcontent[$p]/SHORT-NAME/data()\n"
				+ "                            return $pPortASWCcontent[$p]\n"
				+ "    let $pPortSelectedInterface := for $ASWC in $pPortSelectedASWC/PORTS/P-PORT-PROTOTYPE\n"
				+ "                                where $targetPportName[$i] = $ASWC/SHORT-NAME\n"
				+ "                                return $ASWC/PROVIDED-INTERFACE-TREF/data()\n"
				
				+ "    let $rPortSelectedASWC := for $p in (1 to count($rPortASWCcontent))\n"
				+ "                            where $targetRportASWC[$i] = $rPortASWCcontent[$p]/SHORT-NAME/data()\n"
				+ "                            return $rPortASWCcontent[$p]\n"
				+ "    let $rPortSelectedInterface := for $ASWC in $rPortSelectedASWC/PORTS/R-PORT-PROTOTYPE\n"
				+ "                                where $targetRportName[$i] = $ASWC/SHORT-NAME\n"
				+ "                                return $ASWC/REQUIRED-INTERFACE-TREF/data()\n"
				
				+ "    return concat($pPortSelectedInterface,\",\",$targetPportName[$i],\",\",$PportCategory[$i],\",\",$targetPportASWC[$i]\n"
				+ "            ,\",\",$rPortSelectedInterface,\",\",$targetRportName[$i],\",\",$RportCategory[$i],\",\",$targetRportASWC[$i])";
		
		return query;
	}
}
