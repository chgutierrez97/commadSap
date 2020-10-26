package ast.sap.connector.util;

public enum Selector {
	GET;
	
	/**
	 * Retorna el primer elemento si no es nulo, caso contrario, retorna el segundo.
	 * 
	 * @param item1 Primer item.
	 * @param item2 Segundo item.
	 * @return el primer elemento si no es nulo, caso contrario, retorna el segundo.
	 */
	public <E> E firstOrSecond(E item1, E item2) {
		return item1 == null ? item2 : item1;
	}
}
