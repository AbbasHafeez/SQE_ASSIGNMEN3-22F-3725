package DAL;

public class MySQLDAOFactory extends AbstractDAOFactory {

	@Override
	public IDAO createDAO() {
		return new MySQLDAO();
	}

}
