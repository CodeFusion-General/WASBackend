package com.codefusion.wasbackend.Transaction.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TransactionTriggerUtil implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public void createTransactionTrigger() {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TRIGGERS WHERE TRIGGER_NAME = 'track_transaction'",
                Integer.class);

        if (count != null && count == 0) {
            String triggerSQL = "CREATE TRIGGER track_transaction AFTER INSERT ON transaction " +
                    "FOR EACH ROW BEGIN " +
                    "IF NEW.is_buying THEN " +
                    "UPDATE product SET profit = profit - NEW.price WHERE id = NEW.product_id; " +
                    "ELSE " +
                    "UPDATE product SET profit = profit + NEW.price WHERE id = NEW.product_id; " +
                    "END IF; " +
                    "END";
            jdbcTemplate.execute(triggerSQL);
            System.out.println("Transaction trigger created successfully.");
        } else {
            System.out.println("Trigger already exists. Skipping creation.");
        }
    }



    @Override
    public void run(String... args) throws Exception {
        createTransactionTrigger();
    }
}
