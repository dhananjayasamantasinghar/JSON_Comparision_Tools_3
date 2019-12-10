package com.ct.model;

public class POA_Employee {

	private String name;
	private CommunicationAddress communicationAddress;
	private String mobile;
	private double salary;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CommunicationAddress getCommunicationAddress() {
		return communicationAddress;
	}

	public void setCommunicationAddress(CommunicationAddress communicationAddress) {
		this.communicationAddress = communicationAddress;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public static class Address {
		private String addressLine1;

		public String getAddressLine1() {
			return addressLine1;
		}

		public void setAddressLine1(String addressLine1) {
			this.addressLine1 = addressLine1;
		}

		@Override
		public String toString() {
			return "Address [addressLine1=" + addressLine1 + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((addressLine1 == null) ? 0 : addressLine1.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Address other = (Address) obj;
			if (addressLine1 == null) {
				if (other.addressLine1 != null)
					return false;
			} else if (!addressLine1.equals(other.addressLine1))
				return false;
			return true;
		}
		
		
	}

	public static class CommunicationAddress {
		private Address address;

		public Address getAddress() {
			return address;
		}

		public void setAddress(Address address) {
			this.address = address;
		}

		@Override
		public String toString() {
			return "CommunicationAddress [address=" + address + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((address == null) ? 0 : address.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CommunicationAddress other = (CommunicationAddress) obj;
			if (address == null) {
				if (other.address != null)
					return false;
			} else if (!address.equals(other.address))
				return false;
			return true;
		}
		

	}

	@Override
	public String toString() {
		return "POA_Employee [name=" + name + ", communicationAddress=" + communicationAddress + ", mobile=" + mobile
				+ ", salary=" + salary + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((communicationAddress == null) ? 0 : communicationAddress.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(salary);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		POA_Employee other = (POA_Employee) obj;
		if (communicationAddress == null) {
			if (other.communicationAddress != null)
				return false;
		} else if (!communicationAddress.equals(other.communicationAddress))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(salary) != Double.doubleToLongBits(other.salary))
			return false;
		return true;
	}
	
	
}
