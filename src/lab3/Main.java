//package lab3;
//
//import lab1.Account;
//import lab1.Customer;
//import lab1.Transaction;
//import lab2.AccountDAOImpl;
//import lab2.CustomerDAOImpl;
//import lab2.TransactionDAO;
//import lab2.TransactionDAOImpl;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        CustomerDAOImpl customerDAO = new CustomerDAOImpl();
//        AccountDAOImpl accountDAO = new AccountDAOImpl();
//        TransactionDAOImpl transactionDAO = new TransactionDAOImpl();
//
//        System.out.println("****** Welcome to IgirePay ********");
//
//        while (true) {
//            System.out.println("Choose an option: ");
//            System.out.println("1. Register Customer: ");
//            System.out.println("2. Create Account: ");
//            System.out.println("3. View Accounts: ");
//            System.out.println("4. Deposit Money: ");
//            System.out.println("5. Withdraw Money: ");
//            System.out.println("6. Transfer Money: ");
//            System.out.println("7. View Transaction History: ");
//            System.out.println("8. Delete Account: ");
//            System.out.println("9. Exit: ");
//
//            int choice = sc.nextInt();
//            sc.nextLine();
//
//            try {
//                switch (choice) {
//                    case 1 : {
//                        System.out.print("Enter customer ID: ");
//                        String customerId = sc.nextLine();
//                        System.out.print("Enter full name: ");
//                        String customername = sc.nextLine();
//                        System.out.print("Enter email: ");
//                        String email = sc.nextLine();
//                        System.out.print("Enter phone: ");
//                        String phoneNumber = sc.nextLine();
//                        System.out.print("Enter PIN: ");
//                        String pin = sc.nextLine();
//
//                        Customer customer = new Customer(customerId,customername,email,phoneNumber);
//                        customerDAO.registerCustomer(customer);
//                        System.out.println("Customer registered successfully!");
//                    }
//                    case 2 : {
//                        System.out.print("Enter account ID: ");
//                        String accountId = sc.nextLine();
//                        System.out.print("Enter customer ID: ");
//                        String customerId = sc.nextLine();
//                        System.out.print("Enter account number: ");
//                        String accountNumber = sc.nextLine();
//                        System.out.print("Enter account type: ");
//                        String accountType = sc.nextLine();
//                        System.out.print("Enter initial balance: ");
//                        double balance = sc.nextDouble();
//
//                        Account account = new Account(accountId,accountNumber,accountType,balance, LocalDateTime.now(),customerId);
//                        accountDAO.registerAccount(account);
//                        System.out.println("Account created successfully!");
//                    }
//                    case 3:{
//                        List<Account> accounts = accountDAO.findAll();
//                        accounts.forEach(System.out::println);
//                    }
//                    case 4 : {
//                        System.out.print("Enter account ID: ");
//                        String accountId = sc.nextLine();
//                        System.out.print("Enter account ID: ");
//                        String transactionType = sc.nextLine();
//                        System.out.print("Enter transaction type: ");
//                        double amount = sc.nextDouble();
//
//                        Transaction tx = new Transaction("tx",accountId,transactionType,amount, LocalDateTime.now());
//                      tx.getTransactionType().equals("deposit");
//                        System.out.println("Deposit successful!");
//                    }
//                    case 5 : {
//                        System.out.print("Enter account ID: ");
//                        String accId = sc.nextLine();
//                        System.out.print("Enter withdrawal amount: ");
//                        double amount = sc.nextDouble();
//
//                        Transaction tx = new Transaction("TX" + System.currentTimeMillis(), accId, "WITHDRAW", amount, LocalDateTime.now());
//                        transactionDAO.processTransaction(tx);
//                        System.out.println("Withdrawal successful!");
//                    }
//                    case 6 : {
//                        System.out.print("Enter source account ID: ");
//                        String srcId = sc.nextLine();
//                        sc.next();
//                        System.out.print("Enter destination account ID: ");
//                        String destId = sc.nextLine();
//                        sc.next();
//                        System.out.print("Enter transfer amount: ");
//                        double amount = sc.nextDouble();
//
//                        transactionDAO.transferMoney(srcId, destId, amount);
//                        System.out.println("Transfer successful!");
//                    }
//                    case 7 : {
//                        System.out.print("Enter account ID: ");
//                        String accId = sc.nextLine();
//
//                    }
//                    case 8 : {
//                        System.out.print("Enter account ID to delete: ");
//                        String accId = sc.nextLine();
//                        accountDAO.deleteAccount(accId);
//                        System.out.println("Account deleted successfully!");
//                    }
//                    case 9 :{
//                        System.out.println("Goodbye!");
//                        return;
//                    }
//                    default :System.out.println("Invalid choice, try again.");
//                }
//            } catch (Exception exception) {
//                System.out.println("Error: " + exception.getMessage());
//            }
//        }
//    }
//}
