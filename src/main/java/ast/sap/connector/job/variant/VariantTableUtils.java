package ast.sap.connector.job.variant;

import java.lang.reflect.Field;
import java.util.List;

import ast.sap.connector.func.InTableParam;

public class VariantTableUtils {

	/*METODO PARA SETEAR LOS VALORES DE LA TABLA VARIANT_INFO A PARTIR DE LOS ATRIBUTOS DE LA CLASE*/
	
	public static void setParametersfromList(InTableParam inTableParam, List<VariantEntry> list) {
		for (VariantEntry variantInfo : list) {
			inTableParam.appendRow();
			for (Field field : variantInfo.getClass().getDeclaredFields()) {
				System.out.println(field);
			}
		}
	}

}
