
Financial Office

A simple Java-based financial ledger system that lets users add, manage, and view financial transactions.

Features
Add deposits
Make payments
View transaction ledger
View reports based on date or vendor
Validate date and time formats
Sort transactions based on date and time


File Structure
Main.java - The primary class containing the main method and various functionalities.

InputValidator - A static nested class to validate user input for dates, times, and amounts.

DateComparator - A comparator for sorting transactions based on date and time.

TransactionRecord.java - A class representing individual transactions, encapsulating details like date, time, vendor, amount, and description.


Data Persistence
All transactions are saved to a file named Transactions.csv. Each record in this file contains the date, time, description, vendor/receiver, and amount of the transaction.
