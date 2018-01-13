package mvc.dao.model;

import java.util.List;

import mvc.model.Category;

public interface ICategoryDAO {
	public int INSERT_FAIL = -1;
	
	public boolean createTable();
	public int insert(Category category);
	
	public Category get(int ID);
	public List<Category> getAll();
	
	public boolean update(Category category);
	public boolean delete(int ID);
}
