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
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.time.format.DateTimeParseException;


public class Main {

    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);
        System.out.print("Welcome to Financial Office!!!\n");
        showMainMenu(inputScanner);
    }




    class InputValidator {

        public static boolean isValidDate(String date) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            try {
                LocalDate.parse(date, formatter);
                return true;
            } catch (DateTimeParseException e) {
                return false;
            }
        }

        public static boolean isValidTime(String time) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            try {
                LocalTime.parse(time, formatter);
                return true;
            } catch (DateTimeParseException e) {
                return false;
            }
        }

        public static boolean isValidAmount(double amount) {
            return amount >= 0;
        }

        // You can add more validation methods as needed
    }









    public static class DateComparator implements Comparator<TransactionRecord> {
        @Override
        public int compare(TransactionRecord o1, TransactionRecord o2) {
            if (o1.getDate().equals(o2.getDate())) {
                return o1.getTime().compareTo(o2.getTime());
            } else {
                return o1.getDate().compareTo(o2.getDate());
            }
        }
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
            case 'X', 'x' -> System.exit(0);
            default -> {
                System.out.println("Invalid choice. Please try again.");
                showMainMenu(inputScanner);
            }
        }
    }

        public static void showLedger (Scanner inputScanner){

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
            Collections.sort(transactions, new DateComparator());


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

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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


    public static void displayPrevioustMonth(ArrayList<TransactionRecord> transactions) {
        LocalDate previousMonthDate = LocalDate.now().minusMonths(1);
        System.out.print("""
            -Transactions for Previous Month-
            """);
        System.out.printf("%-16s %-10s %-35s %-30s %-10s%n", "Date", "Time", "Description", "Vendor", "Amt");
        for (TransactionRecord record : transactions) {
            if (record.getDate().getMonthValue() == previousMonthDate.getMonthValue() && record.getDate().getYear() == previousMonthDate.getYear()) {
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
        inputScanner.nextLine();  // This will consume the newline




        switch (reportChoice) {
            // ... [existing cases]

            case 'L', 'l' -> {
                displayLastYear(transactions);
                continueOrExitPrompt(inputScanner);
            }

            case 'V', 'v' -> {
                System.out.println("Enter Vendor Name:");
                String vendor = inputScanner.nextLine();
                displayByVendor(transactions, vendor);
                continueOrExitPrompt(inputScanner);
            }



            case 'M', 'm' -> {
                displayCurrentMonth(transactions);
                continueOrExitPrompt(inputScanner);
            }



            case 'P', 'p' -> {
                displayPrevioustMonth(transactions);
                continueOrExitPrompt(inputScanner);
            }



            case 'Y', 'y' -> {
                displayCurrentYear(transactions);
                continueOrExitPrompt(inputScanner);
            }



            case 'B', 'b' -> {
                showLedger(inputScanner);
            }






            // ... [existing cases]
        }
    }

    public static void displayCurrentYear(ArrayList<TransactionRecord> transactions) {
        int currentYear = LocalDate.now().getYear();
        System.out.print("\n-Transactions for This Year-\n");
        System.out.printf("%-16s %-10s %-35s %-30s %-10s%n", "Date", "Time", "Description", "Vendor", "Amt");
        for (TransactionRecord record : transactions) {
            if (record.getDate().getYear() == currentYear) {
                printTransactionRecord(record);
            }
        }
    }


    public static void displayLastYear(ArrayList<TransactionRecord> transactions) {
        int lastYear = LocalDate.now().getYear() - 1;
        System.out.print("\n-Transactions for Last Year-\n");
        System.out.printf("%-16s %-10s %-35s %-30s %-10s%n", "Date", "Time", "Description", "Vendor", "Amt");
        for (TransactionRecord record : transactions) {
            if (record.getDate().getYear() == lastYear) {
                printTransactionRecord(record);
            }
        }
    }

    public static void displayByVendor(ArrayList<TransactionRecord> transactions, String vendor) {
        System.out.print("\n-Transactions for Vendor: " + vendor + "-\n");
        System.out.printf("%-16s %-10s %-35s %-30s %-10s%n", "Date", "Time", "Description", "Vendor", "Amt");
        for (TransactionRecord record : transactions) {
            if (record.getVendor().equalsIgnoreCase(vendor)) {
                printTransactionRecord(record);
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