package mvc.observer.toolbar;

public interface DatabaseChangeSubject {
	public void addDatabaseChangeListener(DatabaseChangeListener listener);
	public void removeDatabaseChangeListener(DatabaseChangeListener listener);
	
	public void databaseChanged();
}
