package mvc.observer.url.select;

import java.util.List;

import mvc.model.Url;

public interface UrlSelectSubject {
	public void addUrlSelectListener(UrlSelectListener listener);
	public void removeUrlSelectListener(UrlSelectListener listener);
	
	public void selectUrl(List<Url> urls);
	public void selectNothing();
}
