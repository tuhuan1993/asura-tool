package com.asura.tools.data.selection;

public interface IFeaturable {
	public Object getObject();

	public boolean hasFeature(String paramString);

	public String getFeatureValue(String paramString);
}
