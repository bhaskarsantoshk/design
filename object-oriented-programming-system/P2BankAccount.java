public class P2BankAccount {
    String accountNumber;
    double balance;

    P2BankAccount(String accountNumber, double balance){
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    void deposit (double amount){
        this.balance += amount;
    }

    void withdraw (double amount){
        if ( amount > balance){
            System.out.println("Insufficient funds!");
        } else {
            this.balance -= amount;
        }
    }

    void displayDetails(){
        System.out.println("Account Number : "+ this.accountNumber);
        System.out.printf("Balance : %.2f\n", this.balance);
    }
}
