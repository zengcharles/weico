package com.android.base.upgrade.db_upgrade;

import java.util.HashMap;

public class DBUpgradeSql {
	/**
	 * 脚本编写规则，
	 * 在每次数据库升级的时候，
	 * 按版本号添加在相应位置处，
	 * 出现多条脚本时用"|"竖线分开如：
	 * put(2, " alter table_name add column_name varchar(12)|update table_name set column_name =\"example\"");
	 *   
	 */
	HashMap<Integer, String> DBUpgradeScript = new HashMap<Integer, String>() {
		{
			put(1, "");// 数据库版本1为基础数据库，无需添加版本变更脚本，直接留空
			put(2, "");
			put(3, "");
			put(4, "");
			put(5, "");
			put(6, "");
		}
	};

	public HashMap<Integer, String> getUpdateDBScript() {
		return DBUpgradeScript;
	}
}
