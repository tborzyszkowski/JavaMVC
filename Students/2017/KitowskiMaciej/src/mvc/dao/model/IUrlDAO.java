package mvc.dao.model;

import java.util.List;

import mvc.model.Subcategory;
import mvc.model.Url;

public interface IUrlDAO {
	public int INSERT_FAIL = -1;
	
	public boolean createTable();
	public int insert(Url url);
	
	public Url get(int ID);
	public List<Url> getAllWithSubcategory(Subcategory subcategory);
	public List<Url> getAll();
	
	public boolean update(Url url);
	public boolean delete(int ID);
}
