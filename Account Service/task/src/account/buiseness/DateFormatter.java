package account.buiseness;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Component
public class DateFormatter implements StringFormatter<LocalDate> {
    @Override
    public LocalDate format(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        return YearMonth.parse(stringDate, formatter).atDay(1);
    }

    @Override
    public String formatToString(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM-yyyy"); // "March-2010"
        return localDate.format(formatter);
    }
}
