package com.caiex.account.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.springframework.stereotype.Component;
@Component
public class MergeUtil {
	
	public  Object merge(Object o1,Object o2,Object o3) throws Exception{
		Field[] field = o1.getClass().getDeclaredFields();
		//Method[] mm = o1.getClass().getDeclaredMethods();
		
		for(int j=0 ; j<field.length ; j++){
	        String name = field[j].getName();    
	        String type = field[j].getGenericType().toString();   
	        System.out.println(type);
	        if(type.equals("class java.lang.Double")){   
	            Method m = o1.getClass().getMethod("get"+toUpperCaseFirstOne(name));
	            Double value1 = (Double) m.invoke(o1);
	            Double value2 = (Double) m.invoke(o2);
	            if(value1==null){
	            	value1=0.0;
	            }
	            if(value2==null){
	            	value2=0.0;
	            }
	            Double value = value1+value2;
	            
	            m = o1.getClass().getMethod("set"+toUpperCaseFirstOne(name), Double.class);
	            m.invoke(o3, value);
	        } 
	        if(type.equals("class java.lang.Integer")){   
	            Method m = o1.getClass().getMethod("get"+toUpperCaseFirstOne(name));
	            
	            System.out.println("get"+toUpperCaseFirstOne(name));
	            
	            Integer value1 = (Integer) m.invoke(o1);
	            Integer value2 = (Integer) m.invoke(o2);
	           if(value1==null){
	        	   value1=0;
	           }
	           if(value2==null){
	        	   value2=0;
	           } 
	            Integer value = value1+value2;
	            
	            m = o1.getClass().getMethod("set"+toUpperCaseFirstOne(name), Integer.class);
	            m.invoke(o3, value);
	        } 
	        if(type.equals("class java.lang.String")){   
	            Method m = o1.getClass().getMethod("get"+toUpperCaseFirstOne(name));
	            String value1 = (String) m.invoke(o1);
	            String value = value1;
	            
	            m = o1.getClass().getMethod("set"+toUpperCaseFirstOne(name), String.class);
	            m.invoke(o3, value);
	        } 
		}
		return o3;
	}
		
		
	public  String toUpperCaseFirstOne(String s){
		  if(Character.isUpperCase(s.charAt(0)))
		    return s;
		  else
		    return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
		}
	 
	
	
	
}
