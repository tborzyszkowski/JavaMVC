package mvc.dao.model;

import java.util.List;

import mvc.model.Subcategory;
import mvc.model.Category;

public interface ISubcategoryDAO {
	public int INSERT_FAIL = -1;
	
	public boolean createTable();
	public int insert(Subcategory subcategory);
	
	public Subcategory get(int ID);
	public List<Subcategory> getWithCategory(Category category);
	public List<Subcategory> getAll();
	
	public boolean update(Subcategory subcategory);
	public boolean delete(int ID);
}
