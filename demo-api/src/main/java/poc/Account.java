package poc;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Account {
    @Id
    private String id;

    private List<Operation> operations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
}

