/*******************************************************************************
 * Copyright (C) 2012 Raphfrk
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.raphfrk.bukkit.serverportwarps.database;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.Transaction;
import com.avaje.ebeaninternal.api.SpiEbeanServer;

public class DatabaseManager {

	public static void setupDatabaseManual(JavaPlugin p, String prefix) {

		Logger l = Logger.getLogger("Minecraft");

		StringBuilder createString = new StringBuilder();
		StringBuilder updateString = new StringBuilder();
		SpiEbeanServer serv = (SpiEbeanServer) p.getDatabase();

		for(Class<?> c : p.getDatabaseClasses()) {
			LinkedHashMap<String,Boolean> methodNames = new LinkedHashMap<String,Boolean>();
			HashMap<String,Method> methodLookup = new HashMap<String,Method>();
			if(c.isAnnotationPresent(Entity.class)) {

				String tableName = convertName(c.getSimpleName());
				createString.append("CREATE TABLE IF NOT EXISTS " + tableName + " (");

				for(Method m : c.getMethods()) {
					if(m.isAnnotationPresent(Transient.class)) {
						l.info(prefix + " " + m.getName() + " is transient");
					} else {
						methodNames.put(m.getName(), m.isAnnotationPresent(Id.class));
						methodLookup.put(m.getName(), m);
					}
				}
				boolean first = true;
				for(Entry<String,Boolean> entry: methodNames.entrySet()) {
					String methodName = entry.getKey();
					//Boolean primaryKey = entry.getValue();
					if(!methodName.startsWith("set")) {
						continue;
					}
					String fieldName = methodName.substring(3);
					if(methodNames.containsKey("get" + fieldName) || methodNames.containsKey("is" + fieldName)) {
						fieldName = fieldName.toLowerCase();
						//l.info(prefix + " " + fieldName + " is a valid field " + primaryKey);
						Method m = methodLookup.get(methodName);
						Class<?>[] params = m.getParameterTypes();
						Class<?> type = params[0];
						String typeName = null;

						if(type.equals(Double.class) || type.equals(double.class)) {
							typeName = "double";
						} else if(type.equals(Integer.class) || type.equals(int.class)) {
							typeName = "integer";
						} else if(type.equals(String.class)) {
							typeName = "varchar(255)";
						} else if(type.equals(float.class) || type.equals(Float.class)) {
							typeName = "float";
						}

						if(typeName != null) {
							if(!first) {
								createString.append(", ");
							} else {
								first = false;
							}
							createString.append(fieldName + " " + typeName);
							updateString.append("ALTER TABLE " + tableName + " ADD COLUMN " + fieldName + " " + typeName + ";");
						}
					}
				}
				createString.append(");");
			}
		}

		String[] updateStrings = (createString.toString() + updateString).toString().split(";");

		for(String s : updateStrings) {
			Transaction t = serv.beginTransaction();

			try {
				Statement st = null;
				try {
					st = t.getConnection().createStatement();
					st.execute(s);
					l.info(prefix + " Executed: " + s);
				} catch (SQLException se) {
					String message = se.getMessage();
					if(!message.contains("duplicate column name")) {
						throw new RuntimeException(se);
					}
				} finally {
					if(st != null) {
						try {
							st.close();
						} catch (SQLException e) {
							l.info(prefix + " Unable to close statement");
						}
					}
				}
				t.commit();
			} finally {
				t.end();
			}
		}

	}

	public static String convertName(String name) {

		StringBuilder sb = new StringBuilder(name.length());

		boolean first = true;
		for(int cnt=0;cnt<name.length();cnt++) {
			char c = name.charAt(cnt);
			if(Character.isUpperCase(c)) {
				if(!first) {
					sb.append("_");
				}
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
			first = false;
		}

		return sb.toString();

	}

}
