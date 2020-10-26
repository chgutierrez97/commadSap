package ast.sap.connector.util;

import java.io.File;
import java.nio.file.attribute.PosixFilePermission;
import java.util.EnumSet;

import ch.qos.logback.core.rolling.RollingFileAppender;

public class WorldWritableFileAppender<E> extends RollingFileAppender<E> {
	@Override
	public void setFile(String file) {
		super.setFile(file);
		// Si el OS es windows, entonces no es necesario aplicar esto
		if(System.getProperty("os.name").matches("[W|w]indows.*")) return;
		
		File f = new File(file);
		try {
			// TODO : ARREGLAR PROBLEMA DE LOGBACK
			//if (f.exists()) java.nio.file.Files.setPosixFilePermissions(f.toPath(), EnumSet.allOf(PosixFilePermission.class));
		} catch (UnsupportedOperationException e) {
			System.err.println("No es posible modificar los permisos del archivo de log en este sistema");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
