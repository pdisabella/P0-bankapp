package com.revature.driver;
import java.util.Scanner;
/**
 * This is the entry point to the application
 */
public class BankApplicationDriver {
	final static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		String home = "H";
		System.out.println("Hello User");
		while(home.equals("H")) {			
			System.out.println("Enter Input Code:");
			System.out.println("0 = Directory")
			String input = sc.nextLine();
			switch(input) {
			case "0":
				System.out.println("1 = Create Account");
				System.out.println("2 = Customer Login");
				System.out.println("3 = Employee login");
				break;
			case "1":
				CustomerDriver.create();
				break;
			case "2":
				CustomerDriver.login();
				break;
			case "3":
				EmployeeDriver.login();
				break;
			default:
				System.out.println("Invalid Input");
			}
			
			System.out.println("H = Start from home");
			home = sc.nextLine();
		}
	}
}
		
	}

}
