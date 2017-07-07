package kr.ac.kookmin.cs.xquery;

public class QueryForEnumerationSheet {
	static String queryEnumerationSheet(String ecuName){
		String query = 
				"xquery version \"3.1\";"
				+ "declare default element namespace \"http://autosar.org/schema/r4.0\";"
				+ "declare namespace xsi=\"http://www.w3.org/2001/XMLSchema-instance\";"
				+ "declare namespace schemaLocation=\"http://autosar.org/schema/r4.0 autosar_4-2-2.xsd\";"
				
				+ "let $CSWC := for $all in doc(\"nutoll/AUTOSAR_MOD_AISpecificationExamples.arxml\")//AR-PACKAGE/ELEMENTS/COMPOSITION-SW-COMPONENT-TYPE\n"
				+ "    where $all/SHORT-NAME = \""+ ecuName + "\"\n"
				+ "    return $all\n"
				
				+ "let $deligationPorts := $CSWC/PORTS\n"
				+ "let $deligationPortShortName := $deligationPorts/*/SHORT-NAME/data()\n"
				
				+ "let $deligationPortInterfaceShortName := for $_deligationPortInterfaceShortName in $deligationPorts\n"
				+ "    where $_deligationPortInterfaceShortName/*/SHORT-NAME/data() = $deligationPortShortName\n"
				+ "    return $_deligationPortInterfaceShortName/*/*[name()=\"PROVIDED-INTERFACE-TREF\" or name()=\"REQUIRED-INTERFACE-TREF\"]/data()\n"
				
				+ "let $SR_CS_PortInterface := doc(\"nutoll/AUTOSAR_MOD_AISpecificationExamples.arxml\")//AR-PACKAGE/ELEMENTS/*[name()=\"SENDER-RECEIVER-INTERFACE\" or name()=\"CLIENT-SERVER-INTERFACE\"]\n"

				+ "let $dataTypeName := for $i in (1 to count($deligationPortShortName))\n"
				+ "    for $_dataTypeName in $SR_CS_PortInterface\n"
				+ "        where $_dataTypeName/SHORT-NAME/data() = $deligationPortInterfaceShortName[$i]\n"
				+ "        return if(boolean($_dataTypeName/DATA-ELEMENTS/VARIABLE-DATA-PROTOTYPE/TYPE-TREF))\n"
				+ "                then $_dataTypeName/DATA-ELEMENTS/VARIABLE-DATA-PROTOTYPE/TYPE-TREF/data()\n"
				+ "                else <tag>NULL</tag>/data()\n"
				
				+ "let $distinctdataTypeName := distinct-values($dataTypeName)\n"
				
				+ "let $primitiveDataType := for $pDataType in doc(\"nutoll/AUTOSAR_MOD_AISpecificationExamples.arxml\")//AR-PACKAGE/ELEMENTS/APPLICATION-PRIMITIVE-DATA-TYPE\n"
				+ "    where $pDataType/SHORT-NAME/data() = $distinctdataTypeName\n"
				+ "    return $pDataType\n"
				
				+ "let $allCompuMethod := doc(\"nutoll/AUTOSAR_MOD_AISpecificationExamples.arxml\")//AR-PACKAGE/ELEMENTS/COMPU-METHOD\n"
				
				+ "for $i in (1 to count($distinctdataTypeName))\n"
				+ "    let $discription := for $data in $primitiveDataType\n"
				+ "        where $data/SHORT-NAME = $distinctdataTypeName[$i]\n"
				+ "        return $data/DESC/data()\n"
				
				+ "    let $selectedCompuMethod := for $data in $primitiveDataType\n"
				+ "        where $data/SHORT-NAME = $distinctdataTypeName[$i]\n"
				+ "        return $data/SW-DATA-DEF-PROPS/SW-DATA-DEF-PROPS-VARIANTS/SW-DATA-DEF-PROPS-CONDITIONAL/COMPU-METHOD-REF\n"
				+ "    let $selectedDataConstr := for $data in $primitiveDataType\n"
				+ "        where $data/SHORT-NAME = $distinctdataTypeName[$i]\n"
				+ "        return $data/SW-DATA-DEF-PROPS/SW-DATA-DEF-PROPS-VARIANTS/SW-DATA-DEF-PROPS-CONDITIONAL/DATA-CONSTR-REF\n"
				
				+ "    let $compuMethodContent := for $allc in $allCompuMethod\n"
				+ "        where $allc/SHORT-NAME/data() = $selectedCompuMethod\n"
				+ "        return $allc\n"
				
				+ "    let $_textTableCompuMethod := for $texttable in $compuMethodContent\n"
				+ "        where $texttable/CATEGORY = \"TEXTTABLE\"\n"
				+ "        return $texttable\n"
				
				+ "    let $textTableCompuMethod := if (count($_textTableCompuMethod) != 0)\n"
				+ "        then $_textTableCompuMethod\n"
				+ "        else ()\n"
				
				+ "    let $textTableLongName := data($textTableCompuMethod/LONG-NAME/*)\n"
				
				+ "    let $numOfValue := count($textTableCompuMethod/COMPU-INTERNAL-TO-PHYS/COMPU-SCALES/COMPU-SCALE)\n"
				
				+ "    let $valueList :=\n"
				+ "    <tag>{\n"
				+ "        for $k in (1 to $numOfValue)\n"
				+ "        let $null := \"\"\n"
				+ "        return concat($null, $textTableCompuMethod/COMPU-INTERNAL-TO-PHYS/COMPU-SCALES/COMPU-SCALE[$k]/DESC,\"#\")}\n"
				+ "    </tag>/data()\n"
				
				+ "    return if (count($_textTableCompuMethod) != 0)\n"
				+ "        then concat($distinctdataTypeName[$i],\"@\",$textTableLongName,\"@\",$discription,\"@\",$valueList)\n"
				+ "        else ()";
				
		return query;
	}
}
