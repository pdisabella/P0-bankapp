package com.revature.driver;
import java.util.Scanner;
import com.revature.beans.User;
import com.revature.beans.Account;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoDB;
import com.revature.services.UserService;
import com.revature.dao.AccountDao;
import com.revature.dao.AccountDaoDB;
import com.revature.services.AccountService;
import com.revature.dao.TransactionDao;
import com.revature.dao.TransactionDaoDB;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class BankApplicationDriver {

	private static Scanner sc = new Scanner(System.in);

	private static int initialinput;
	private static int customerInput;
	private static int employeeInput;
	private static boolean programContinue;
	private static User currentUser;
	private static Account temp;
	private static Account temp2;
	private static int tempInput;
	private static double tempAmount;
	private static String tempString;
	private static UserDao userDAO = new UserDaoDB();
	private static AccountDao acctDAO = new AccountDaoDB();
	private static UserService userS = new UserService();
	private static AccountService acctS = new AccountService();
	private static TransactionDao tranDAO = new TransactionDaoDB();
	public static final Logger logger = LogManager.getLogger(BankApplicationDriver.class);

	public static void main(String[] args ) {
		programContinue = true;
		homeMenu();
		}
	
	public static void homeMenu() {
		System.out.println("Welcome User!");
		System.out.println("1 = Login ");
		System.out.println("2 = Sign Up ");
		System.out.println("3 = Quit");
		initialinput = sc.nextInt();
		parseInitialInput();
	}

	public static void parseInitialInput() {
		while(programContinue);{
		switch (initialinput) {
		case 1:
			programContinue = true;
			logInMenu();
			break;
		case 2:
			programContinue = true;
			newUserMenu();
			break;
		case 3:
			programContinue = false;
			break;
		default:
			System.out.println("Invalid Input");
			homeMenu();
			break;
		}
		}
	}
	public static void logInMenu() {
		System.out.println("Username:");
		String uName = sc.next();
		System.out.println("Password:");
		String uPass = sc.next();
		if(userDAO.getUser(uName,uPass) == null) {
			System.out.println("Invalid Credentials");
			logInMenu();
		}else {
			currentUser = userDAO.getUser(uName,uPass);	
			if (userS.logIn(currentUser, uName, uPass)) {

				if (uName.equals("admin") && uPass.equals("password")) {
					logger.info("Employee, " + currentUser.getFirstName() + " " + currentUser.getLastName() + ", has logged in.");
					employeeActionsMenu();
				}else {
					logger.info("Customer, " + currentUser.getFirstName() + " " + currentUser.getLastName() + ", has logged in.");
					customerActionsMenu();
				}
			}else {
				System.out.println("Invalid Credentials");
				homeMenu();
			} 
		}
	}
	public static void newUserMenu() {	
		System.out.println("New Username: ");
		String uName = sc.next();
		System.out.println("New Password: ");
		String uPass = sc.next();
		System.out.println("First Name: ");
		String uFName = sc.next();
		System.out.println("Last Name: ");
		String uLName = sc.next();
		User newUser = new User(uName, uPass, uFName, uLName, false);
		userDAO.addUser(newUser);
		currentUser = newUser;
		System.out.println("User Account Created");
		logger.info("New Customer, " + currentUser.getFirstName() + " " + currentUser.getLastName() + ", account created.");
		logInMenu();
	}
	public static void customerActionsMenu() {
		if (acctDAO.getAccountsByUser(currentUser) == null) {
			System.out.println("Your current accounts: None");
		}
		System.out.println("1 = New Account");
		System.out.println("2 = View Account Balance");		
		System.out.println("3 = Deposit");
		System.out.println("4 = Withdraw");	
		System.out.println("5 = Transfer funds");	
		System.out.println("6 = Log Out");
		customerInput = sc.nextInt();
		parseCustomerActions();
	}
	public static void parseCustomerActions() {
		switch (customerInput) {
		case 1:
			Account newAcct = new Account();
			newAcct.setOwnerId(currentUser.getId());
			newAcct.setBalance(0.0d);
			newAcct.setApproved(true);
			acctDAO.addAccount(newAcct);
			logger.info("New checking account created for " + currentUser.getFirstName() + " " + currentUser.getLastName());
			System.out.println("Account is now pending review.");
			break;
		case 2:
			System.out.println("Your available accounts:");
			System.out.println(acctDAO.getAccountsByUser(currentUser).toString());
			System.out.println("Select Account:");
			tempInput = sc.nextInt();
			if (acctDAO.getAccount(tempInput)!=null) {
				System.out.println("Account balance: " + acctDAO.getBalance(acctDAO.getAccount(tempInput)));
			} else {

				System.out.println("Invalid Input");
				System.out.println("Returning to Customer Menu");
				customerActionsMenu();
			}
			break;

		case 3:

			System.out.println("Your available accounts:");
			System.out.println(acctDAO.getAccountsByUser(currentUser).toString());
			System.out.println("Select Account:");
			tempInput = sc.nextInt();

			if (acctDAO.getAccount(tempInput)!= null) {

				temp = acctDAO.getAccount(tempInput);

				if (temp.isApproved() == false) {

					while (true) {

						System.out.println("Amount to Deposit:");
						tempAmount = sc.nextDouble();

						if (tempAmount > 0.0d) {
							acctS.deposit(acctDAO.getAccount(tempInput), tempAmount);
							logger.info("New deposit created for Account (" + tempInput + ") in the amount of $" + tempAmount);
							System.out.println("Deposit complete!");
							break;

							

						} else {

							System.out.println("Deposit amount must be greater than zero.");
							customerActionsMenu();
						}
					}
				} else {

					System.out.println("Account is Pending Approval.");
					customerActionsMenu();
				}

			} else {			
				System.out.println("No Account Found");
				customerActionsMenu();
			}	
			break;
		case 4: 
			System.out.println("Your available accounts:");
			System.out.println(acctDAO.getAccountsByUser(currentUser).toString());
			System.out.println("Select Account:");
			tempInput = sc.nextInt();
			if (acctDAO.getAccount(tempInput) != null) {

				temp = acctDAO.getAccount(tempInput);

				if (temp.isApproved() == false) {

					while (true) {
						System.out.println("Amount to withdraw:");
						tempAmount = sc.nextDouble();
						if (tempAmount < temp.getBalance()) {
							acctS.withdraw(acctDAO.getAccount(tempInput), tempAmount); 
							logger.info("New withdrawal created for Account (" + tempInput + ") in the amount of $" + tempAmount);
							System.out.println("Withdrawal Complete.");
							break;
						} else {
							System.out.println("Insufficient Funds");
							customerActionsMenu();
						}
					}

				} else {

					System.out.println("Account Pending Approval");
					customerActionsMenu();
				}
			} else {			
				System.out.println("No Account Found.");
				customerActionsMenu();
			}	
			break;
		case 5: 
			System.out.println("Your available accounts:");
			System.out.println(acctDAO.getAccountsByUser(currentUser).toString());
			System.out.println("Select Account");
			tempInput = sc.nextInt();
			if (acctDAO.getAccount(tempInput) != null) {
				temp = acctDAO.getAccount(tempInput);
				if (temp.isApproved() == false) {
					System.out.println("Select Account to Transfer to:");
					tempInput = sc.nextInt();
					if (acctDAO.getAccount(tempInput) != null) {
						temp2 = acctDAO.getAccount(tempInput);
						if (temp2.isApproved() == false) {
							while (true) {
								System.out.println("Amount to transfer:");
								tempAmount = sc.nextFloat();
								if (tempAmount < temp.getBalance()) {
									acctS.transfer(temp, temp2, tempAmount); 
									logger.info("New transfer created for Account (" + temp.getAccountNum() + ") to Account (" + temp2.getAccountNum() + ") in the amount of $" + tempAmount);
									System.out.println("Transfer complete! Returning to the main menu.");
									break;
									
								} else {
									System.out.println("Insufficient Funds");
									customerActionsMenu();
								}
							}
						} else {

							System.out.println("Account Pending Approval");
							customerActionsMenu();
						}

					} else {
						System.out.println("No Account Found");
						customerActionsMenu();		
					}
				} else {

					System.out.println("Account Pending Approval");
					customerActionsMenu();
				}
			} else {
				System.out.println("No Account Found");
				customerActionsMenu();
			}
			break;
		case 6: 
			logger.info("Customer, " + currentUser.getFirstName() + " " + currentUser.getLastName() + ", has logged out.");
			currentUser = null;
			homeMenu();
			break;
		default:
			System.out.println("Invalid Input");
			customerActionsMenu();
			break;
		}
	}
	public static void employeeActionsMenu() {

		System.out.println("1 = Review Pending Accounts");
		System.out.println("2 = View User Accounts");
		System.out.println("3 = View All Accounts");
		System.out.println("4 = View All Recent Transactions");
		System.out.println("5 = Log out");
		employeeInput = sc.nextInt();
		parseEmployeeActions();
	}
	public static void parseEmployeeActions() {
		switch (employeeInput) {
		case 1: 
			System.out.println("\nAll accounts:\n\n"
					+ acctDAO.getAccounts() + "\n\n"
					+ "Select Account");
			tempInput = sc.nextInt();

			if (acctDAO.getAccount(tempInput) != null) {
				temp = acctDAO.getAccount(tempInput);
				if (temp.isApproved() == true) {
					acctDAO.updateAccount(temp);
					System.out.println("Account (" + tempInput + ") successfully updated!\n");
				} else {
					System.out.println("Account Already Approved.");					
					employeeActionsMenu();
				}
			} else {
				System.out.println("No Account Found");
				employeeActionsMenu();
			}
			break;
		case 2: 

			System.out.println("\nAll Users:\n\n"
					+ userDAO.getAllUsers() + "\n\n"
					+ "Select User");
			tempInput = sc.nextInt();
			if (userDAO.getUser(tempInput) != null) {
				System.out.println(acctDAO.getAccountsByUser(userDAO.getUser(tempInput)));
			} else {
				System.out.println("No Account Found");
				employeeActionsMenu();
			}
			break;
		case 3: 
			System.out.println("\nAll accounts:\n\n"
					+ acctDAO.getAccounts() + "\n\n");
			break;
		case 4: 

			System.out.println("All User transactions:\n\n"
					+ tranDAO.getAllTransactions() + "\n\n");
			employeeActionsMenu();
			break;
		case 5: 

			logger.info("Employee, " + currentUser.getFirstName() + " " + currentUser.getLastName() + ", has logged out.");
			currentUser = null;
			homeMenu();
			break;
		default:
			System.out.println("Invalid input");
			employeeActionsMenu();
			break;
		}
	}
}