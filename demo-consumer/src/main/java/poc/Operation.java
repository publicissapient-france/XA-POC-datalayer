package poc;

public class Operation {
    private String comment;
    private Double amount;

    public Operation() {
    }

    public Operation(Double amount, String comment) {
        this.amount = amount;
        this.comment = comment;
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
