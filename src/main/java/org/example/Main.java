package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);
        System.out.print("Welcome to Financial Office!!!\n");
        showMainMenu(inputScanner);
    }

    public static void showMainMenu(Scanner inputScanner) {

        System.out.print("""
                                                   -MAIN MENU-
                Follow the instructions to proceed:
                'D' Add Deposit
                'P' Make Payment
                'L' Ledger
                'X' Exit
                """);

        char selection = inputScanner.next().charAt(0);

        switch (selection) {
            case 'L', 'l' -> showLedger(inputScanner);
            case 'P', 'p' -> processPayment(inputScanner);
            case 'D', 'd' -> processDeposit(inputScanner);
        }
    }

    public static void showLedger(Scanner inputScanner) {

        System.out.print("""
                                                   -LEDGER MENU-
                Follow the instructions to proceed:
                'A' All entries
                'D' Deposits
                'P' Payments
                'R' Reports
                'H' Home
                """);

        char ledgerChoice = inputScanner.next().charAt(0);
        ArrayList<TransactionRecord> transactions = fetchAllEntries();
        // transactions.sort(new DateComparator());
        // todo; fix the sort method

        switch (ledgerChoice) {
            case 'H', 'h' -> showMainMenu(inputScanner);
            case 'D', 'd' -> {
                displayDeposits(transactions);
                continueOrExitPrompt(inputScanner);
            }
            case 'A', 'a' -> {
                displayAllEntries(transactions);
                continueOrExitPrompt(inputScanner);
            }
            case 'P', 'p' -> {
                displayPayments(transactions);
                continueOrExitPrompt(inputScanner);
            }
            case 'R', 'r' -> {
                showReportsMenu(inputScanner, transactions);
                continueOrExitPrompt(inputScanner);
            }
        }
    }

    public static void processPayment(Scanner inputScanner) {
        System.out.println("Enter the Date in DD/MM/YYYY format: ");
        String paymentDate = inputScanner.nextLine();
        paymentDate = inputScanner.nextLine();

        System.out.println("Enter the time in HH:mm format: ");
        String paymentTime = inputScanner.nextLine();

        System.out.println("Enter the Description: ");
        String paymentDescription = inputScanner.nextLine();

        System.out.println("Enter the receiver name: ");
        String receiver = inputScanner.nextLine();

        System.out.println("Enter the amount to make payment: ");
        double paymentAmount = inputScanner.nextDouble();

        saveTransaction(paymentDate, paymentTime, paymentDescription, receiver, "-$" + paymentAmount, inputScanner);
    }

    public static void processDeposit(Scanner inputScanner) {
        System.out.println("Enter the Date in DD/MM/YYYY format: ");
        String depositDate = inputScanner.nextLine();
        depositDate = inputScanner.nextLine();

        System.out.println("Enter the time in HH:mm format: ");
        String depositTime = inputScanner.nextLine();

        System.out.println("Enter the Description: ");
        String depositDescription = inputScanner.nextLine();

        System.out.println("Enter the Vendor name: ");
        String vendorName = inputScanner.nextLine();

        System.out.println("Enter the amount: ");
        double depositAmount = inputScanner.nextDouble();

        saveTransaction(depositDate, depositTime, depositDescription, vendorName, "$" + depositAmount, inputScanner);
    }

    private static void saveTransaction(String date, String time, String description, String vendor, String amount, Scanner inputScanner) {
        try {
            FileWriter transactionFileWriter = new FileWriter("Transactions.csv", true);
            String record = String.format("%s|%s|%s|%s|%s%n", date, time, description, vendor, amount);
            transactionFileWriter.write(record);
            transactionFileWriter.close();
            System.out.println("Transaction saved!");
            showMainMenu(inputScanner);
        } catch (IOException exception) {
            System.out.println("ERROR: File operation failed.");
        }
    }

    public static void displayDeposits(ArrayList<TransactionRecord> transactions) {
        System.out.print("""
                                -All Deposit Transactions-
             """);
        System.out.printf("%-16s %-10s %-35s %-30s %-10s%n", "Date", "Time", "Description", "Vendor", "Amt");
        for (TransactionRecord record : transactions) {
            if (record.getAmount() > 0) {
                printTransactionRecord(record);
            }
        }
    }

    public static ArrayList<TransactionRecord> fetchAllEntries() {
        ArrayList<TransactionRecord> entries = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("Transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split("\\|");
                String date = data[0];
                String time = data[1];
                String description = data[2];
                String vendor = data[3];
                double amount = Double.parseDouble(data[4].replace("$", ""));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                entries.add(new TransactionRecord(LocalDate.parse(date,formatter), LocalTime.parse(time), description, vendor, amount));
            }
            bufferedReader.close();
        } catch (IOException exception) {
            System.out.println("ERROR: File operation failed.");
        }
        return entries;
    }

    public static void displayAllEntries(ArrayList<TransactionRecord> transactions) {
        System.out.print("""
                                -All Transactions-
             """);
        System.out.printf("%-16s %-10s %-35s %-30s %-10s%n", "Date", "Time", "Description", "Vendor", "Amt");
        for (TransactionRecord record : transactions) {
            printTransactionRecord(record);
        }
    }

    public static void displayPayments(ArrayList<TransactionRecord> transactions) {
        System.out.print("""
                                -All Payment Transactions-
             """);
        System.out.printf("%-16s %-10s %-35s %-30s %-10s%n", "Date", "Time", "Description", "Vendor", "Amt");
        for (TransactionRecord record : transactions) {
            if (record.getAmount() < 0) {
                printTransactionRecord(record);
            }
        }
    }

    public static void showReportsMenu(Scanner inputScanner, ArrayList<TransactionRecord> transactions) {

        System.out.print("""
                                -REPORTS MENU-
             """);

        System.out.print("""
                Follow the instructions to proceed:
                'M' This Month
                'P' Previous Month
                'Y' This Year
                'L' Last Year
                'V' By Vendor
                'B' Back
                """);

        char reportChoice = inputScanner.next().charAt(0);
        inputScanner.nextLine();

        switch (reportChoice) {
            case 'M', 'm' -> {
                displayCurrentMonth(transactions);
                continueOrExitPrompt(inputScanner);
            }
/*
            case 'P', 'p' -> {

                displayPreviousMonth(transactions);
                continueOrExitPrompt(inputScanner);
            }
            case 'Y', 'y' -> {
                displayCurrentYear(transactions);
                continueOrExitPrompt(inputScanner);
            }
            case 'L', 'l' -> {
                displayPreviousYear(transactions);
                continueOrExitPrompt(inputScanner);
            }
            case 'V', 'v' -> {
                displayByVendor(inputScanner, transactions);
                continueOrExitPrompt(inputScanner);
            }

 */
            case 'B', 'b' -> {
                showLedger(inputScanner);
            }
        }
    }

    public static void displayCurrentMonth(ArrayList<TransactionRecord> transactions) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String currentMonth = currentDateTime.format(DateTimeFormatter.ofPattern("MM"));
        System.out.print("""
                                -Transactions for This Month-
             """);
        System.out.printf("%-16s %-10s %-35s %-30s %-10s%n", "Date", "Time", "Description", "Vendor", "Amt");
        for (TransactionRecord record : transactions) {
            if (record.getDate().getMonth().equals(LocalDate.now().getMonth())) {
                printTransactionRecord(record);
            }
        }
    }

    /*public static void displayPreviousMonth(ArrayList<TransactionRecord> transactions) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String previousMonth = String.valueOf(currentDateTime.minusMonths(1).getMonthValue());
        System.out.print("""
                                -Transactions for Previous Month-
             """);
        System.out.printf("%-16s %-10s %-35s %-30s %-10s%n", "Date", "Time", "Description", "Vendor", "Amt");
        for (TransactionRecord record : transactions) {
            if (record.getDate().split("/")[1].equals(previousMonth)) {
                printTransactionRecord(record);
            }
        }
    }

    public static void displayCurrentYear(ArrayList<TransactionRecord> transactions) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String currentYear = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy"));
        System.out.print("""
                                -Transactions for This Year-
             """);
        System.out.printf("%-16s %-10s %-35s %-30s %-10s%n", "Date", "Time", "Description", "Vendor", "Amt");
        for (TransactionRecord record : transactions) {
            if (record.getDate().split("/")[2].equals(currentYear)) {
                printTransactionRecord(record);
            }
        }
    }

    public static void displayPreviousYear(ArrayList<TransactionRecord> transactions) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String previousYear = String.valueOf(currentDateTime.minusYears(1).getYear());
        System.out.print("""
                                -Transactions for Last Year-
             """);
        System.out.printf("%-16s %-10s %-35s %-30s %-10s%n", "Date", "Time", "Description", "Vendor", "Amt");
        for (TransactionRecord record : transactions) {
            if (record.getDate().split("/")[2].equals(previousYear)) {
                printTransactionRecord(record);
            }
        }
    }

    public static void displayByVendor(Scanner inputScanner, ArrayList<TransactionRecord> transactions) {
        System.out.print("""
                                -Transactions by Vendor-
             """);
        displayVendorList(transactions);
        System.out.println("Enter the Vendor Name: ");
        String selectedVendor = inputScanner.nextLine();

        System.out.printf("%-16s %-10s %-35s %-30s %-10s%n", "Date", "Time", "Description", "Vendor", "Amt");
        for (TransactionRecord record : transactions) {
            if (record.getVendor().equalsIgnoreCase(selectedVendor)) {
                printTransactionRecord(record);
            }
        }
    }

    public static void displayVendorList(ArrayList<TransactionRecord> transactions) {
        HashSet<String> vendors = new HashSet<>();
        for (TransactionRecord record : transactions) {
            vendors.add(record.getVendor());
        }
        System.out.println("Available Vendors:");
        for (String vendor : vendors) {
            System.out.println(vendor);
        }
    }
*/
    public static void printTransactionRecord(TransactionRecord record) {
        System.out.printf("%-16s %-10s %-35s %-30s %-10s%n", record.getDate(), record.getTime(), record.getDescription(), record.getVendor(), record.getAmountFormatted());
    }

    public static void continueOrExitPrompt(Scanner inputScanner) {
        System.out.println("Do you wish to continue? (Y/N)");
        char choice = inputScanner.next().charAt(0);
        if (choice == 'Y' || choice == 'y') {
            showLedger(inputScanner);
        } else {
            System.exit(0);
        }
    }
}

// Class TransactionRecord and DateComparator can be kept the same as before.
