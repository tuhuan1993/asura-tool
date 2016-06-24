package com.asura.tools.data.selection;

public class TestFeature implements IFeaturable {
	private long id;

	public TestFeature() {
	}

	public TestFeature(long id) {
		this.id = id;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFeatureValue(String feature) {
		return String.valueOf(this.id);
	}

	public Object getObject() {
		return Long.valueOf(this.id);
	}

	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = 31 * result + (int) (this.id ^ this.id >>> 32);
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (super.getClass() != obj.getClass())
			return false;
		TestFeature other = (TestFeature) obj;

		return (this.id == other.id);
	}

	public String toString() {
		return String.valueOf(this.id);
	}

	public boolean hasFeature(String feature) {
		return false;
	}
}