package com.ct.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommpoUtils {

	private static void doSimpleMapping(Object podEmployee, Object poa_Employee, Field field, String mappingValue) {
		try {
			Method[] declaredMethods = poa_Employee.getClass().getDeclaredMethods();
			for (Method method : declaredMethods) {
				if (method.getName().equalsIgnoreCase("set" + mappingValue)) {
					field.setAccessible(true);
					method.invoke(poa_Employee, ReflectionUtils.getField(field, podEmployee));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void doComplexMapping(Object podEmployee, Object poa_Employee, Field field, String mappingValue)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException {
		String[] values = mappingValue.split("/");
		Map<String, Object> objectFactory = new HashMap<>();
		for (int i = 0; i < values.length; i++) {
			String value = values[i];
			if (i == 0) {
				//create a objectFactory and Store the object
				setFirstComplexType(podEmployee, poa_Employee, field, values, value, objectFactory);
			} else if (i > 0 && i != values.length - 1) {
				//Get the previous object from Object factory and set current object with previous
				setComplexTypeExceptFirst(values, i, value, objectFactory);
			} else {
				//Get the previous object from Object factory and set variable value in that
				setVariableData(podEmployee, field, values, i, value, objectFactory);
			}
		}

	}

	private static void setVariableData(Object podEmployee, Field field, String[] values, int i, String value,
			Map<String, Object> objectFactory) throws IllegalAccessException, InvocationTargetException {
		Object obj1 = objectFactory.get(values[i - 1]);
		Method[] declaredMethods = obj1.getClass().getDeclaredMethods();
		for (Method method : declaredMethods) {
			if (method.getName().equalsIgnoreCase("set" + value)) {
				field.setAccessible(true);
				method.invoke(obj1, ReflectionUtils.getField(field, podEmployee));
			}
		}
	}

	private static void setComplexTypeExceptFirst(String[] values, int i, String value,
			Map<String, Object> objectFactory)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, InvocationTargetException {
		Object obj2 = objectFactory.get(values[i - 1]);

		Field[] declaredFields1 = obj2.getClass().getDeclaredFields();

		for (Field field2 : declaredFields1) {
			Method[] declaredMethods = obj2.getClass().getDeclaredMethods();
			String[] split = field2.toGenericString().split(" ");
			String className = split[1];
			String newValue = value;
			if (className.contains(value)) {
				newValue = field2.getName();
			}
			for (Method method : declaredMethods) {
				if (method.getName().equalsIgnoreCase("set" + newValue)) {
					Object obj = Class.forName(className).newInstance();
					method.invoke(obj2, obj);
					objectFactory.put(values[i - 1], obj2);
					objectFactory.put(value, obj);
				}
			}
		}
	}

	private static void setFirstComplexType(Object podEmployee, Object poa_Employee, Field field, String[] values,
			String value, Map<String, Object> objectFactory)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, InvocationTargetException {
		Field[] declaredFields = poa_Employee.getClass().getDeclaredFields();
		for (Field field2 : declaredFields) {
			String[] split = field2.toGenericString().split(" ");
			String className = split[1];
			String newValue = value;
			if (className.contains(value)) {
				newValue = field2.getName();
			}
			if (field2.getName().equalsIgnoreCase(newValue)) {
				Object obj = Class.forName(className).newInstance();
				for (Method method : poa_Employee.getClass().getDeclaredMethods()) {
					if (method.getName().equalsIgnoreCase("set" + newValue)) {
						method.invoke(poa_Employee, obj);
						objectFactory.put(value, obj);
						break;
					}
				}
			}
		}
	}

	public static <T> T createObjectAsPerMaping(Object podEmployee, Class<T>  clazz) {
		try {
			return getPodEmployee(podEmployee, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static <T> T getPodEmployee(Object podEmployee, Class<T> newObjectClass) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
		Field[] declaredFields = podEmployee.getClass().getDeclaredFields();
		T poa_Employee = (T) newObjectClass.newInstance();
		for (Field field : declaredFields) {
			if (field.isAnnotationPresent(MapTo.class)) {
				String mappingValue = field.getAnnotation(MapTo.class).value();
				if (mappingValue.contains("/")) {
					CommpoUtils.doComplexMapping(podEmployee, poa_Employee, field, mappingValue);
				} else {
					CommpoUtils.doSimpleMapping(podEmployee, poa_Employee, field, mappingValue);
				}
			}
		}
		return poa_Employee;
	}

	public static <T> T createObjectFromContent(Class<T> clazz, String path) {
		try {
			String content = AppReader.getContentFromFile(path);
			return new ObjectMapper().readValue(content, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
