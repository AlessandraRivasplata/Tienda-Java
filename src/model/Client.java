
package model;
import main.Payable;

public class Client extends Person implements Payable {
    private int memberId;
    private double balance;
    private static final int MEMBER_ID = 456;
    private static final double BALANCE = 50.00;

    // Constructor
    public Client() {
        super();
        this.memberId = MEMBER_ID;
        this.balance = BALANCE;
    }

    // Implementación del método pay de la interfaz Payable
    @Override
    public boolean pay(double amount) {
        double finalBalance = balance - amount;
        if (finalBalance >= 0) {
            balance = finalBalance;
            return true;
        } else {
            return false;
        }
    }

    // Métodos adicionales específicos de la clase Client
    public int getMemberId() {
        return memberId;
    }

    public double getBalance() {
        return balance;
    }

	public void setMemberId(int i) {
		// TODO Auto-generated method stub
		
	}

	public void setBalance(double d) {
		// TODO Auto-generated method stub
		
	}
}
