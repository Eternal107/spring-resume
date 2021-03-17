package net.study.resume.service;

import lombok.Data;
import net.study.resume.entity.Hobby;
import net.study.resume.entity.Profile;
import net.study.resume.model.LanguageLevel;
import net.study.resume.model.LanguageType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StaticDataService {

    @Value("${practic.years}")
    private int practicYears;

    @Value("${education.years}")
    private int educationYears;

    @Value("${course.years}")
    private int courseYears;

    private final Set<Hobby> allHobbies;

    private final Set<String> allHobbyNames;

    public StaticDataService() {
        this.allHobbies    = Collections.unmodifiableSet(createAllHobbiesSet());
        this.allHobbyNames = Collections.unmodifiableSet(createAllHobbiNamesSet());
    }

    protected Set<Hobby> createAllHobbiesSet() {
        return new HashSet<>(Arrays.asList(new HobbyReadOnlyEntity("Cycling"), new HobbyReadOnlyEntity("Handball"), new HobbyReadOnlyEntity("Football"), new HobbyReadOnlyEntity("Basketball"),
                new HobbyReadOnlyEntity("Bowling"), new HobbyReadOnlyEntity("Boxing"), new HobbyReadOnlyEntity("Volleyball"), new HobbyReadOnlyEntity("Baseball"), new HobbyReadOnlyEntity("Skating"),
                new HobbyReadOnlyEntity("Skiing"), new HobbyReadOnlyEntity("Table tennis"), new HobbyReadOnlyEntity("Tennis"), new HobbyReadOnlyEntity("Weightlifting"),
                new HobbyReadOnlyEntity("Automobiles"), new HobbyReadOnlyEntity("Book reading"), new HobbyReadOnlyEntity("Cricket"), new HobbyReadOnlyEntity("Photo"),
                new HobbyReadOnlyEntity("Shopping"), new HobbyReadOnlyEntity("Cooking"), new HobbyReadOnlyEntity("Codding"), new HobbyReadOnlyEntity("Animals"), new HobbyReadOnlyEntity("Traveling"),
                new HobbyReadOnlyEntity("Movie"), new HobbyReadOnlyEntity("Painting"), new HobbyReadOnlyEntity("Darts"), new HobbyReadOnlyEntity("Fishing"), new HobbyReadOnlyEntity("Kayak slalom"),
                new HobbyReadOnlyEntity("Games of chance"), new HobbyReadOnlyEntity("Ice hockey"), new HobbyReadOnlyEntity("Roller skating"), new HobbyReadOnlyEntity("Swimming"),
                new HobbyReadOnlyEntity("Diving"), new HobbyReadOnlyEntity("Golf"), new HobbyReadOnlyEntity("Shooting"), new HobbyReadOnlyEntity("Rowing"), new HobbyReadOnlyEntity("Camping"),
                new HobbyReadOnlyEntity("Archery"), new HobbyReadOnlyEntity("Pubs"), new HobbyReadOnlyEntity("Music"), new HobbyReadOnlyEntity("Computer games"), new HobbyReadOnlyEntity("Authorship"),
                new HobbyReadOnlyEntity("Singing"), new HobbyReadOnlyEntity("Foreign lang"), new HobbyReadOnlyEntity("Billiards"), new HobbyReadOnlyEntity("Skateboarding"),
                new HobbyReadOnlyEntity("Collecting"), new HobbyReadOnlyEntity("Badminton"), new HobbyReadOnlyEntity("Disco")));
    }

    protected Set<String> createAllHobbiNamesSet() {
        Set<String> set = new HashSet<>();
        for (Hobby h : allHobbies) {
            set.add(h.getName());
        }
        return set;
    }

    public Set<Hobby> listAllHobbies() {
        return allHobbies;
    }

    public List<Hobby> createHobbyEntitiesByNames(List<String> names) {
        List<Hobby> result = new ArrayList<>(names.size());
        for (String name : names) {
            if (allHobbyNames.contains(name)) {
                result.add(new Hobby(name));
            }
        }
        return result;
    }

    public List<Integer> listEducationYears() {
        return listYears(educationYears);
    }

    public List<Integer> listPracticsYears() {
        return listYears(practicYears);
    }

    public List<Integer> listCourcesYears() {
        return listYears(courseYears);
    }

    protected List<Integer> listYears(int count) {
        List<Integer> years = new ArrayList<>();
        int now = DateTime.now().getYear();
        for (int i = now; i >= count; i--) {
            years.add(i);
        }
        return years;
    }

    public Map<Integer, String> mapMonths() {
        String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
        Map<Integer, String> map = new LinkedHashMap<>();
        for (int i = 0; i < months.length; i++) {
            map.put(i + 1, months[i]);
        }
        return map;
    }

    public Collection<LanguageType> getAllLanguageTypes() {
        return EnumSet.allOf(LanguageType.class);
    }

    public Collection<LanguageLevel> getAllLanguageLevels() {
        return EnumSet.allOf(LanguageLevel.class);
    }



    protected static final class HobbyReadOnlyEntity extends Hobby {

        protected HobbyReadOnlyEntity(String name) {
            super(name);
        }

        @Override
        public void setId(Long id) {
            throw new UnsupportedOperationException("This hobby instance is readonly instance!");
        }

        @Override
        public void setName(String name) {
            throw new UnsupportedOperationException("This hobby instance is readonly instance!");
        }

        @Override
        public void setProfile(Profile profile) {
            throw new UnsupportedOperationException("This hobby instance is readonly instance!");
        }

        @Override
        public void setSelected(boolean selected) {
            throw new UnsupportedOperationException("This hobby instance is readonly instance!");
        }



    }
}
