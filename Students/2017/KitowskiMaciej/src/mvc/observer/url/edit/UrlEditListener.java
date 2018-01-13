package mvc.observer.url.edit;

import java.util.List;

import mvc.model.Url;

public interface UrlEditListener {
	public void onUrlAdd(Url url);
	
	public void onUrlEdit(List<Url> urls);
	
	public void onUrlDelete(List<Url> urls);
}
