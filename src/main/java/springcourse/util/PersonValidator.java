package springcourse.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import springcourse.dao.PersonDAO;
import springcourse.models.Person;

@Component
public class PersonValidator implements Validator {
    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }


    // Пишем это для того чтобы нельзя было использовать на любых других класса, только на Person
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        //Сделали даун каст

        //Теперь нужно проверить есть ли в бд человек с таким же email
        if(personDAO.show(person.getEmail())!=null){
            errors.rejectValue("email", "", "This email is already in use");
        }
    }
}
