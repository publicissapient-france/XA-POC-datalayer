package poc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class DemoApiApplication {

    @Autowired
    public DemoApiApplication(AccountRepository accountRepository) {this.accountRepository = accountRepository;}

    public static void main(String[] args) {
        SpringApplication.run(DemoApiApplication.class, args);
    }

    private final AccountRepository accountRepository;

    @GetMapping("/account")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/account/{id}")
    public Account getAccount(@PathVariable String id) {
        return accountRepository.findOne(id);
    }
}
