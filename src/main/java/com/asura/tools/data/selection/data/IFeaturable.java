package com.asura.tools.data.selection.data;

public interface IFeaturable {
	public Object getObject();

	public boolean hasFeature(String paramString);

	public String getFeatureValue(String paramString);
}
