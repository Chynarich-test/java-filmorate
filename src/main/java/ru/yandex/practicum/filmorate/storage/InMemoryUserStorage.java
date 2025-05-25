package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@Component
public class InMemoryUserStorage extends InMemoryBaseStorage<User> implements UserStorage {

    public InMemoryUserStorage() {
        super(User.class);
    }

    public List<User> getFriends(Long id) {
        List<Long> friendsIds = getOne(id).getFriends().stream().toList();
        return findAll().stream()
                .filter(e -> friendsIds.contains(e.getId()))
                .toList();
    }

    @Override
    protected User modifiedBeforeAdd(User element) {
        if (element.getName() == null || element.getName().isEmpty()) element.setName(element.getLogin());
        return element;
    }

    @Override
    protected void setElementValue(User target, User source) {
        if (source.getEmail() != null) target.setEmail(source.getEmail());
        if (source.getName() != null) target.setName(source.getName());
        if (source.getLogin() != null) target.setLogin(source.getLogin());
        if (source.getBirthday() != null) target.setBirthday(source.getBirthday());
    }

    @Override
    protected boolean isNotValidateNewElement(User element) {
        if (element == null) return true;
        if (element.getEmail() == null || element.getEmail().isEmpty()) return true;
        if (element.getLogin() == null || element.getLogin().isEmpty()) return true;
        return isNotValidateElementValues(element);
    }

    @Override
    protected boolean isNotValidateElementValues(User element) {
        if (element == null) return true;
        if (element.getEmail() != null && !element.getEmail().contains("@")) return true;
        if (element.getLogin() != null && !element.getLogin().isEmpty()
                && element.getLogin().contains(" ")) return true;
        return element.getBirthday() != null && element.getBirthday().isAfter(LocalDate.now());
    }

}
