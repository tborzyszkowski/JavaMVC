package mvc.observer.url.select;

import java.util.List;

import mvc.model.Url;

public interface UrlSelectListener {
	public void onSelectUrl(List<Url> urls);
	public void onUnselectAllUrls();
}
