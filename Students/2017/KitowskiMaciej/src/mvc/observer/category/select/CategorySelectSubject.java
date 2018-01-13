package mvc.observer.category.select;

import java.util.List;

import mvc.model.Subcategory;
import mvc.model.Category;

public interface CategorySelectSubject {
	public void addCategorySelectListener(CategorySelectListener listener);
	public void removeCategorySelectListener(CategorySelectListener listener);
	
	public void selectCategory(List<Category> categories);
	public void selectSubcategory(List<Subcategory> subcategories);
	public void selectNothing();
}
