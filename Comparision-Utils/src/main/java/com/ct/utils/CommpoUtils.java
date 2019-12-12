package com.ct.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class CommpoUtils {

	private static Map<String, Object> objectFactory = new HashMap<>();

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

		for (int i = 0; i < values.length; i++) {
			String value = values[i];
			if (i == 0) {
				// create a objectFactory and Store the object
				setFirstComplexType(podEmployee, poa_Employee, field, values, value, objectFactory);
			} else if (i > 0 && i != values.length - 1) {
				// Get the previous object from Object factory and set current
				// object with previous
				setComplexTypeExceptFirst(values, i, value, objectFactory);
			} else {
				// Get the previous object from Object factory and set variable
				// value in that
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
				break;
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

			if (field2.getName().contains(value) || field2.getName().equalsIgnoreCase(value)) {
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
	}

	private static void setFirstComplexType(Object podEmployee, Object poa_Employee, Field field, String[] values,
			String value, Map<String, Object> objectFactory)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, InvocationTargetException {
		if (objectFactory.isEmpty() || (!objectFactory.isEmpty() && objectFactory.get(value) != null)) {
			Field[] declaredFields = poa_Employee.getClass().getDeclaredFields();
			for (Field field2 : declaredFields) {
				String[] split = field2.toGenericString().split(" ");
				String className = split[1];
				String newValue = value;
				if (className.contains(value)) {
					newValue = field2.getName();
				}
				if ((field2.getName().contains(newValue) || field2.getName().equalsIgnoreCase(newValue))
						&& objectFactory.get(value) == null) {
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
	}

	public static <T> T createObjectAsPerMaping(Object podEmployee, Class<T> clazz) {
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

	public static List<String> getNodeList(String content) throws Exception {
		JsonNode nodes = new ObjectMapper().readTree(content);
		return getNodeList("/", nodes, new ArrayList<>());

	}

	private static List<String> getNodeList(String rootName, JsonNode node, List<String> list) throws Exception {
		Iterator<Entry<String, JsonNode>> fields = node.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> map = (Map.Entry<String, JsonNode>) fields.next();
			if (isObjectNode(map.getValue())) {
				getNodeList(rootName + map.getKey() + "/", map.getValue(), list);
			} else {
				list.add(rootName + map.getKey());
			}
		}
		//String content = AppReader.getContentFromFile("./static/insured-person.json");
		//List<String> list = CommpoUtils.getNodeList(content);
		//list.forEach(System.out::println);
		return list;

	}

	private static boolean isObjectNode(JsonNode node) {
		return node.getNodeType() == JsonNodeType.OBJECT;
	}

}
