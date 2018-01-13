package mvc.observer.category.update;

import java.util.List;
import java.util.Map;

import mvc.model.Subcategory;
import mvc.model.Category;

public interface CategoryUpdateListener {
	public void onCategoryUpdate(Map<Category, List<Subcategory>> categories);
}
