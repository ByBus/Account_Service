package account.buiseness;

import account.exception.BadRequestException;

import java.util.List;

public class PasswordBreachedChecker implements Checker<String>{
    private final List<String> breachedPasswords = List.of(
            "PasswordForJanuary",
            "PasswordForFebruary",
            "PasswordForMarch",
            "PasswordForApril",
            "PasswordForMay",
            "PasswordForJune",
            "PasswordForJuly",
            "PasswordForAugust",
            "PasswordForSeptember",
            "PasswordForOctober",
            "PasswordForNovember",
            "PasswordForDecember");

    @Override
    public boolean check(String password) {
        if (breachedPasswords.contains(password)) {
            throw new BadRequestException("The password is in the hacker's database!");
        }
        return true;
    }
}
