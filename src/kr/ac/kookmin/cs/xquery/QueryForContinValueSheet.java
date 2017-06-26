package kr.ac.kookmin.cs.xquery;

public class QueryForContinValueSheet {
	static String queryContinuousSheet(String ecuName){
		String query = 
				"xquery version \"3.1\";"
				+ "declare default element namespace \"http://autosar.org/schema/r4.0\";"
				+ "declare namespace xsi=\"http://www.w3.org/2001/XMLSchema-instance\";"
				+ "declare namespace schemaLocation=\"http://autosar.org/schema/r4.0 autosar_4-2-2.xsd\";"
				
				+ "let $CSWC := for $all in doc(\"nutoll/AUTOSAR_MOD_AISpecificationExamples.arxml\")//AR-PACKAGE/ELEMENTS/COMPOSITION-SW-COMPONENT-TYPE\n"
				+ "    where $all/SHORT-NAME = \""+ ecuName + "\"\n"
				+ "    return $all\n"
				
				+ "let $CSWCShortName := $CSWC/SHORT-NAME/data()\n"
				
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
				+ "    let $selectedCompuMethod := for $data in $primitiveDataType\n"
				+ "        where $data/SHORT-NAME = $distinctdataTypeName[$i]\n"
				+ "        return $data/SW-DATA-DEF-PROPS/SW-DATA-DEF-PROPS-VARIANTS/SW-DATA-DEF-PROPS-CONDITIONAL/COMPU-METHOD-REF\n"
				+ "    let $selectedDataConstr := for $data in $primitiveDataType\n"
				+ "        where $data/SHORT-NAME = $distinctdataTypeName[$i]\n"
				+ "        return $data/SW-DATA-DEF-PROPS/SW-DATA-DEF-PROPS-VARIANTS/SW-DATA-DEF-PROPS-CONDITIONAL/DATA-CONSTR-REF\n"
				
				+ "    let $compuMethodContent := for $allc in $allCompuMethod\n"
				+ "        where $allc/SHORT-NAME/data() = $selectedCompuMethod\n"
				+ "        return $allc\n"
				
				+ "    let $_linearCompuMethod := for $linear in $compuMethodContent\n"
				+ "        where $linear/CATEGORY = \"LINEAR\"\n"
				+ "        return $linear\n"
				
				+ "    let $linearCompuMethod := if (count($_linearCompuMethod) != 0)\n"
				+ "        then $_linearCompuMethod\n"
				+ "        else ()\n"
				
				+ "    let $linearLongName := data($linearCompuMethod/LONG-NAME/*)\n"
				+ "    let $linearDescription := data($linearCompuMethod/DESC/*)\n"
				+ "    let $linearResolution := data($linearCompuMethod/COMPU-PHYS-TO-INTERNAL/COMPU-SCALES/COMPU-SCALE/COMPU-RATIONAL-COEFFS/COMPU-DENOMINATOR/V)\n"
				+ "    let $linearLowerLimit := data($linearCompuMethod/COMPU-PHYS-TO-INTERNAL/COMPU-SCALES/COMPU-SCALE/LOWER-LIMIT)\n"
				+ "    let $linearUpperLimit := data($linearCompuMethod/COMPU-PHYS-TO-INTERNAL/COMPU-SCALES/COMPU-SCALE/UPPER-LIMIT)\n"
				+ "    let $linearUnit := data($linearCompuMethod/UNIT-REF)\n"
				
				+ "    return if(count($_linearCompuMethod) != 0)\n"
				+ "        then concat($distinctdataTypeName[$i],\",\",$linearLongName,\",\",$linearDescription,\",\",$linearResolution,\",\",$linearLowerLimit,\",\",$linearUpperLimit,\",\",$linearUnit)\n"
				+ "        else ()";
		
		return query;
	}
}
