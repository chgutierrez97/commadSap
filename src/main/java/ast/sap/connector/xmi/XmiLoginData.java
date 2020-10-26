package ast.sap.connector.xmi;

/**
 * Datos de inicio de sesion con XMI.
 * 
 * @author martin.zaragoza
 *
 */
public final class XmiLoginData {
	private String company = "AST";
	private String product = "sapConnector";
	private String xmiInterface = "XBP";
	private String version = "3.0";

	/**
	 * "XMI logging: company name of external management tool "
	 * 
	 * @return "company name of external management tool"
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * "XMI logging: Program name of external management tool "
	 * 
	 * @return "Program name of external management tool"
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * "Interface ID (for example, XBP)"
	 * 
	 * @return Interface ID.
	 */
	public String getXmiInterface() {
		return xmiInterface;
	}

	/**
	 * "Version of XMI interface delivered for check purposes "
	 * 
	 * @return Version of XMI interface.
	 */
	public String getVersion() {
		return version;
	}

	public XmiLoginData(String company, String product, String xmiInterface, String version) {
		this.company = company;
		this.product = product;
		this.xmiInterface = xmiInterface;
		this.version = version;
	}

	public XmiLoginData(String company, String product, String xmiInterface) {
		this.company = company;
		this.product = product;
		this.xmiInterface = xmiInterface;
	}

	public XmiLoginData(String company, String product) {
		this.company = company;
		this.product = product;
	}

	public XmiLoginData(String company) {
		this.company = company;
	}

	public XmiLoginData() {
	}
}
