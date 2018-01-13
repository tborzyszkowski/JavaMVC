package mvc.observer.category.update;

public interface CategoryUpdateSubject {
	public void addCategoryUpdateListener(CategoryUpdateListener listener);
	public void removeCategoryUpdateListener(CategoryUpdateListener listener);
	
	public void updateCategories();
}
