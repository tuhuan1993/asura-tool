package com.asura.tools.permission;

import java.util.HashMap;

public class SimplePermission {
	private HashMap<String, Integer> map;
	private int current = 1;

	public SimplePermission() {
		this.map = new HashMap();
	}

	public void addPermission(String permission) {
		if (!(this.map.containsKey(permission))) {
			this.map.put(permission, Integer.valueOf(this.current));

			this.current *= 2;
		}
	}

	public void addPermissions(String[] permissions) {
		for (String p : permissions)
			addPermission(p);
	}

	public boolean hasPermission(String permission) {
		return false;
	}
}
