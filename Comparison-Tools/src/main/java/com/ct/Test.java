package com.ct;

import com.ct.model.POA_Employee;
import com.ct.model.POD_Employee_1;
import com.ct.utils.CommpoUtils;

public class Test {

	public static void main(String[] args) {
		POA_Employee poaObject1 = CommpoUtils.createObjectFromContent(POA_Employee.class, "./static/poa.json");
		POD_Employee_1 podObject = CommpoUtils.createObjectFromContent(POD_Employee_1.class, "./static/pod.json");

		POA_Employee poaObject2 = CommpoUtils.createObjectAsPerMaping(podObject, POA_Employee.class);

		System.out.println("poaObject1 == poaObject2 : " + (poaObject1 == poaObject2));

		System.out.println("poaObject1.equals(poaObject2) : " + (poaObject1.equals(poaObject2)));

	}

}
