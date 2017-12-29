package com.caiex.fms.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import com.caiex.dbservice.model.OrderTicketDetailSGLModel;
@Component
public class MergeUtil {
	
	public  Object merge(Object o1,Object o2,Object o3) throws Exception{
		Field[] field = o1.getClass().getDeclaredFields();
		//Method[] mm = o1.getClass().getDeclaredMethods();
		
		for(int j=0 ; j<field.length ; j++){
	        String name = field[j].getName();    
	        String type = field[j].getGenericType().toString();   
	       
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
	 
	
	
	public Object getNumberAccordingToPercision(Object o) throws Exception{
		
		Field[] field = o.getClass().getDeclaredFields();
		for(int j=0 ; j<field.length ; j++){
	        String name = field[j].getName();    
	        String type = field[j].getGenericType().toString(); 
	        if(type.equals("class java.lang.Double")){ 
	        	 Method m = o.getClass().getMethod("get"+toUpperCaseFirstOne(name));
		            Double value = (Double) m.invoke(o);
		            if(value==null){
		            	value=0.0;
		            }
		            value=NumberUtil.getNumberAccordingToPercision(value, 3);
		           
		            m = o.getClass().getMethod("set"+toUpperCaseFirstOne(name), Double.class);
		            m.invoke(o, value);
	        }
		}
		return o;
	}
	
	
	
	public Object mergeList(List<OrderTicketDetailSGLModel> list,Object total) throws Exception{
		
		
		Field[] field = total.getClass().getDeclaredFields();
	
		for(int j=0 ; j<field.length ; j++){
	        String name = field[j].getName();    
	        String type = field[j].getGenericType().toString();   
	        if(type.equals("class java.lang.Double")){
	        	 Double param =0.0;	
	 	        Method m = total.getClass().getMethod("get"+toUpperCaseFirstOne(name));
	 	        
	 	        for (Object model : list) {
	 	    	  	Double value = (Double) m.invoke(model);
	 	    	  	if(value==null){
	 	    	  		value =0.0;
	 	    	  	}
	 	    	  	param +=value;
	 	       }
	 	        
	 	            
	 	            m = total.getClass().getMethod("set"+toUpperCaseFirstOne(name), Double.class);
	 	            m.invoke(total, param); 
	        }
	       
		}
		return total;
		
		
	}
	
	
	
	
}
