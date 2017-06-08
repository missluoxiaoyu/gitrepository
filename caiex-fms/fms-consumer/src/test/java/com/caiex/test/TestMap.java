package com.caiex.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysql.fabric.xmlrpc.base.Array;

public class TestMap {
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("112324", 2);
		map.put("313234", 1);
		map.put("413123", 3);
		map.put("21312", 1);
		
		Object [] keys =   map.keySet().toArray();    
		Arrays.sort(keys);    
		
		for (Object key : keys) {
			System.out.println(key);
		}
		/*  
		for (String key : map.keySet()) {
		System.out.println(key);
		}
		
		 Map<String, Object> 	map1 = sortMap(map);
		 
		for (String key : map1.keySet()) {
		System.out.println(key);
		}
		*/
	}
	
	public static Map<String, Object> sortMap(Map<String, Object> map){
		List<Map.Entry<String, Object>> keys =  new ArrayList<Map.Entry<String, Object>>(map.entrySet());
		Collections.sort(keys, new Comparator<Map.Entry<String, Object>>() {   
		    public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {      
		        return (o1.getKey()).toString().compareTo(o2.getKey());
		    }
		}); 
		return map;
	}
}
