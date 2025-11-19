import java.time.LocalDate;
import java.time.Period;

public class Character {
    private String name;
    private LocalDate birthDate;

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getAgeAt(LocalDate currentDate) {
        Period age = Period.between(birthDate, currentDate);
        if (age.getYears() < 0) return "ещё не родился";
        return age.getYears() + " лет, " + age.getMonths() + " мес.";
    }
}
