package com.caiex.test;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
public class TestMerge {

	
	static void copy(Object o1, Object o2) throws Exception {
		Field[] field = o1.getClass().getDeclaredFields();
		//Method[] mm = o1.getClass().getDeclaredMethods();
		
		for(int j=0 ; j<field.length ; j++){
            String name = field[j].getName();    
            String type = field[j].getGenericType().toString();    
            if(type.equals("class java.lang.String")){   
                Method m = o1.getClass().getMethod("get"+toUpperCaseFirstOne(name));
                String value = (String) m.invoke(o1);
                String value2 = (String) m.invoke(o2);
                if(value == null && value2!=null){
                    m = o1.getClass().getMethod("set"+toUpperCaseFirstOne(name), String.class);
                    m.invoke(o1, value2);
                }
            } else if("int".equals(type)) {
            	Method m = o1.getClass().getMethod("get"+toUpperCaseFirstOne(name));
                Integer value = (Integer) m.invoke(o1);    
                Integer value2 = (Integer) m.invoke(o2);   
                if(value == null){
                    m = o1.getClass().getMethod("set"+toUpperCaseFirstOne(name), Integer.class);
                    m.invoke(o1, value2);
                }
            }
		}
	}

	public static String toUpperCaseFirstOne(String s){
		  if(Character.isUpperCase(s.charAt(0)))
		    return s;
		  else
		    return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
		}
	
	public static void main(String[] args) throws Exception {
		People o1 = new People();
		People o2 = new People();
		People o3 = new People();
		o1.setAge(12.0);
		o2.setAge(2.0);
		o1.setId(12);
		o2.setId(12);
		o1.setName("xiaobai");
		test(o1, o2,o3);
		System.out.println(o3.getAge());
		System.out.println(o3.getId());
		System.out.println(o3.getName());
	}
	
	public  static void test(Object o1,Object o2,Object o3) throws Exception{
		Field[] field = o1.getClass().getDeclaredFields();
		Method[] mm = o1.getClass().getDeclaredMethods();
		
		for(int j=0 ; j<field.length ; j++){
            String name = field[j].getName();    
            String type = field[j].getGenericType().toString();   
            System.out.println(type);
            if(type.equals("class java.lang.Double")){   
                Method m = o1.getClass().getMethod("get"+toUpperCaseFirstOne(name));
                Double value1 = (Double) m.invoke(o1);
                Double value2 = (Double) m.invoke(o2);
                Double value = value1+value2;
                
                m = o1.getClass().getMethod("set"+toUpperCaseFirstOne(name), Double.class);
                m.invoke(o3, value);
            } 
            if(type.equals("class java.lang.Integer")){   
                Method m = o1.getClass().getMethod("get"+toUpperCaseFirstOne(name));
                Integer value1 = (Integer) m.invoke(o1);
                Integer value2 = (Integer) m.invoke(o2);
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
	}
}
