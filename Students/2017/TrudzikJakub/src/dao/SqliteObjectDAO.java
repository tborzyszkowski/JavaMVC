package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import interfejs.CompDAOInterf;
import interfejs.OrderDAOInterf;
import model.Company;
import model.Order;

public class SqliteObjectDAO implements CompDAOInterf, OrderDAOInterf {

	
	private static final String CREATE_COMPANY = 
			"INSERT INTO Company(name, street, number, postal, city)  VALUES (?,?,?,?,?)";
    private static final String GET_COMPANIES = 
    		"SELECT id, name, street, number, postal, city FROM Company";
    private static final String UPDATE_COMPANY = 
    		"UPDATE Company SET name=?, street=?, number=?, postal=?, city=? WHERE id = ?";
    private static final String DELETE_COMPANY = 
    		"DELETE FROM [Company] WHERE id = ?";
	private static final String CREATE_TABLE_COMPANY = 
			"CREATE TABLE Company (" + 
			"    id     INTEGER     PRIMARY KEY AUTOINCREMENT," + 
			"    name   STRING (50) NOT NULL," + 
			"    street STRING (50) NOT NULL," + 
			"    number INTEGER     NOT NULL," + 
			"    postal INTEGER     NOT NULL," +
			"    city   STRING (50) NOT NULL" + 
			");" 
			;
	
	private static final String CREATE_ORDER = 
			"INSERT INTO [Order] (companyID, description, quantity, price) VALUES (?,?,?,?);";
	private static final String GET_SPEC_ORDERS = 
    		"SELECT id, companyID, description, quantity, price FROM [Order] WHERE companyID = ?";	
    private static final String GET_ORDERS = 
    		"SELECT id, companyID, description, quantity, price FROM [Order]";
    private static final String UPDATE_ORDER = 
    		"UPDATE [Order] SET description=?, quantity=?, price=? WHERE id=?";
    private static final String DELETE_ORDER = 
    		"DELETE FROM [Order] WHERE id = ?";
	private static final String CREATE_TABLE_ORDER = 
			"CREATE TABLE [Order] (" +
				    "id          INTEGER     PRIMARY KEY AUTOINCREMENT," + 
				    "companyID   INTEGER     NOT NULL," +
				    "description STRING (50) NOT NULL," +
				    "quantity    INTEGER     NOT NULL," +
				    "price       DOUBLE      NOT NULL," +
				    "CONSTRAINT [Delete Condition] FOREIGN KEY (" +
				        "companyID)" +
				    "REFERENCES Company (id) ON DELETE CASCADE);" 
			;	
 
	public void createCompany(Company company) {
		Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
            con = SqliteDAO.Connect();
            preparedStatement = con.prepareStatement(CREATE_COMPANY,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getStreet());
            preparedStatement.setInt(3, company.getNumber());
            preparedStatement.setString(4, company.getPostal());
            preparedStatement.setString(5, company.getCity());
            preparedStatement.execute();
            result = preparedStatement.getGeneratedKeys();
            System.out.println("nowy rekord");
            
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
            	System.out.println(rse);
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
            	System.out.println(sse);
            }
            try {
                con.close();
            } catch (Exception cse) {
            	System.out.println(cse);
            }
        }
	}
	
	
	public List<Company> getAllCompanies() {
		List<Company> companyList = new ArrayList<Company>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
        	conn = SqliteDAO.Connect();
            preparedStatement = conn.prepareStatement(GET_COMPANIES);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
            
            if (result != null){
            while(result.next())
            	companyList.add(
            					new Company(
            							result.getInt(1), 
            							result.getString(2), 
            							result.getString(3), 
            							result.getInt(4), 
            							result.getString(5),
            							result.getString(6)
            							));
            
            } else {
                // 
            	System.out.println("Brak klientów");
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
            	System.out.println(rse.getMessage());
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
            	System.out.println(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
            	System.out.println(cse.getMessage());
            }
        }
        return companyList;
	}

	//TODO Not implemented method
	public void createTableCompany() {
		Connection conn = null;
		Statement stmt = null;
		try {
	    	conn = SqliteDAO.Connect();
	    	stmt = conn.createStatement();
	    	stmt.executeUpdate(CREATE_TABLE_COMPANY);
		} catch (SQLException e) {
	    	System.out.println(e.getMessage());
		} finally {
	    	try {
	    		stmt.close();
	    	} catch (Exception sse) {
	    		System.out.println(sse.getMessage());
	    	}
	    	try {
	    		conn.close();
	        } catch (Exception cse) {
	        	System.out.println(cse.getMessage());
	        }
		}
	}
		

	public int updateCompany(Company company) {
		Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
            con = SqliteDAO.Connect();
            preparedStatement = con.prepareStatement(UPDATE_COMPANY);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getStreet());
            preparedStatement.setInt(3, company.getNumber());
            preparedStatement.setString(4, company.getPostal());
            preparedStatement.setString(5, company.getCity());
            preparedStatement.setInt(6, company.getID());
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println(e);
        } finally {        	
            try {
                preparedStatement.close();
            } catch (Exception sse) {
            	System.out.println(sse);
            }
            try {
                con.close();
            } catch (Exception cse) {
            	System.out.println(cse);
            }
        }
        return -1;
	
	}	
	
	

	public void deleteCompany(int id) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
        	con = SqliteDAO.Connect();
            preparedStatement = con.prepareStatement(DELETE_COMPANY);
            preparedStatement.setInt(1, id);            
            preparedStatement.execute();
            System.out.println("usunięto firme o ID: " + id);
          
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception sse) {
            	System.out.println(sse.getMessage());
            }
            try {
                con.close();
            } catch (Exception cse) {
            	System.out.println(cse.getMessage());
            }
        }		
	}
	
	
	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@   O R D E R    @@@@@@@@@@@@@@@@@@@@@@@@@@@@@

	
	public int createOrder(Order order) {
		Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
            con = SqliteDAO.Connect();            
            preparedStatement = con.prepareStatement(CREATE_ORDER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, order.getCompanyID());
            preparedStatement.setString(2, order.getDescription());
            preparedStatement.setInt(3, order.getQuantity());
            preparedStatement.setFloat(4, order.getPrice());
            preparedStatement.execute();
            result = preparedStatement.getGeneratedKeys();            
            System.out.println("nowe zamówienie"); 
            if (result.next() && result != null) {
                return result.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
            	System.out.println(rse);
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
            	System.out.println(sse);
            }
            try {
                con.close();
            } catch (Exception cse) {
            	System.out.println(cse);
            }
        }
        return -1;
	}	
	
	public List<Order> getSpecOrders(int companyID) {
		List<Order> orderList = new ArrayList<Order>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
        	conn = SqliteDAO.Connect();
            preparedStatement = conn.prepareStatement(GET_SPEC_ORDERS);
            preparedStatement.setInt(1, companyID);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
            
            if (result != null){
            while(result.next())
            	orderList.add(	new Order(result.getInt(1),
            							result.getInt(2),
                						result.getString(3), 
                						result.getInt(4), 
                						result.getFloat(5))
            					);            
            } else { 
            	System.out.println("Brak zamówień");
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
            	System.out.println(rse.getMessage());
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
            	System.out.println(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
            	System.out.println(cse.getMessage());
            }
        }
        return orderList;
	}
	
	
	public List<Order> getAllOrders() {
		List<Order> orderList = new ArrayList<Order>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
        	conn = SqliteDAO.Connect();
            preparedStatement = conn.prepareStatement(GET_ORDERS);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();            
            if (result != null){
            while(result.next())
            	orderList.add(		new Order(result.getInt(1), 
            							result.getInt(2),
                						result.getString(3), 
                						result.getInt(4), 
                						result.getFloat(5))            							
            					);            
            } else {
            	System.out.println("Brak zamówień");
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
            	System.out.println(rse.getMessage());
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
            	System.out.println(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
            	System.out.println(cse.getMessage());
            }
        }
        return orderList;
	}

	//TODO Not implemented method
	public void createTableOrder() {
		Connection conn = null;
		Statement stmt = null;
		try {
	    	conn = SqliteDAO.Connect();
	    	stmt = conn.createStatement();
	    	stmt.executeUpdate(CREATE_TABLE_ORDER);
		} catch (SQLException e) {
	    	System.out.println(e.getMessage());
		} finally {
	    	try {
	    		stmt.close();
	    	} catch (Exception sse) {
	    		System.out.println(sse.getMessage());
	    	}
	    	try {
	    		conn.close();
	        } catch (Exception cse) {
	        	System.out.println(cse.getMessage());
	        }
		}
		System.out.println("dodano tabele");
	}
	

	public void deleteOrder(int id) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
        	con = SqliteDAO.Connect();
            preparedStatement = con.prepareStatement(DELETE_ORDER);
            preparedStatement.setInt(1, id);            
            preparedStatement.execute();
            System.out.println("usunięto zamówienie o ID: " + id);           
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception sse) {
            	System.out.println(sse.getMessage());
            }
            try {
                con.close();
            } catch (Exception cse) {
            	System.out.println(cse.getMessage());
            }
        }	
	}
	
	public int updateOrder(Order order) {
		Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
            con = SqliteDAO.Connect();
            preparedStatement = con.prepareStatement(UPDATE_ORDER);
            preparedStatement.setString(1, order.getDescription());
            preparedStatement.setInt(2, order.getQuantity());
            preparedStatement.setFloat(3, order.getPrice());
            preparedStatement.setInt(4, order.getId());
            preparedStatement.execute();
            System.out.println("Zamowienie zaktualizowane");
            
        } catch (SQLException e) {
            System.out.println(e);
        } finally {        	
            try {
                preparedStatement.close();
            } catch (Exception sse) {
            	System.out.println(sse);
            }
            try {
                con.close();
            } catch (Exception cse) {
            	System.out.println(cse);
            }
        }
        return -1;
	
	}	
}
	