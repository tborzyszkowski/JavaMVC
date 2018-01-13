package mvc.observer.url.update;

import java.util.List;

import mvc.model.Url;

public interface UrlUpdateSubject {
	public void addUrlUpdateListener(UrlUpdateListener listener);
	public void removeUrlUpdateListener(UrlUpdateListener listener);
	
	public void updateUrls(List<Url> urls);
}
