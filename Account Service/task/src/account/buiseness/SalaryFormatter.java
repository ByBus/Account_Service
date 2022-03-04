package account.buiseness;

import org.springframework.stereotype.Component;

@Component
public class SalaryFormatter implements StringFormatter<Long>{
    @Override
    public Long format(String string) {
        return null;
    }

    @Override
    public String formatToString(Long salary) {
        long dollars = salary / 100;
        long cents = salary % 100;
        return String.format("%d dollar(s) %d cent(s)", dollars, cents);
    }
}
