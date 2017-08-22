package poc;

public class OperationEvent {
    private String account;
    private String comment;
    private Double amount;

    public OperationEvent(String account, double amount, String comment) {
        this.account = account;
        this.amount = amount;
        this.comment = comment;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
