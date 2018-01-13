package mvc.observer.url.update;

import java.util.List;

import mvc.model.Url;

public interface UrlUpdateListener {
	public void onUrlUpdate(List<Url> urls);
}
