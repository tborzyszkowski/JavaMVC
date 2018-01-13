package mvc.observer.url.edit;

import java.util.List;

import mvc.model.Url;

public interface UrlEditSubject {
	public void addUrlEditListener(UrlEditListener listener);
	public void removeUrlEditListener(UrlEditListener listener);
	
	public void addUrl(Url url);
	
	public void editUrls(List<Url> urls);
	
	public void deleteUrls(List<Url> urls);
}
