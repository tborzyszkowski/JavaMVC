package mvc.observer.category.edit;

import java.util.List;

import mvc.model.Category;
import mvc.model.Subcategory;

public interface CategoryEditSubject {
	public void addCategoryEditListener(CategoryEditListener listener);
	public void removeCategoryEditListener(CategoryEditListener listener);
	
	public void addCategory(Category category);
	public void addSubcategory(Subcategory subcategory);
	
	public void editCategories(List<Category> categories);
	public void editSubcategories(List<Subcategory> subcategories);
	
	public void deleteCategories(List<Category> categories);
	public void deleteSubcategories(List<Subcategory> subcategories);
}
