package com.system.started.db;

import java.util.HashMap;

public interface IDao {

	public void insert(HashMap<String, Object> paramMap);

	public void update(HashMap<String, Object> paramMap);
}
