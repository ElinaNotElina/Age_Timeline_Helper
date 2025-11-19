import java.time.LocalDate;
import java.time.Period;

public class Character {
    private String name;
    private LocalDate birthDate;

    public Character() {
    }

    public Character(String name, LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAgeAt(LocalDate currentDate) {
        Period age = Period.between(birthDate, currentDate);
        if (age.getYears() < 0) return "ещё не родился";
        return age.getYears() + " лет, " + age.getMonths() + " мес.";
    }
}
