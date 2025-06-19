package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;

@Component
public class InMemoryFilmStorage extends InMemoryBaseStorage<Film> implements FilmStorage {

    public InMemoryFilmStorage() {
        super(Film.class);
    }

    @Override
    protected Film modifiedBeforeAdd(Film element) {
        return element;
    }

    @Override
    protected void setElementValue(Film target, Film source) {
        if (source.getName() != null) target.setName(source.getName());
        if (source.getDescription() != null) target.setDescription(source.getDescription());
        if (source.getReleaseDate() != null) target.setReleaseDate(source.getReleaseDate());
        if (source.getDuration() != null) target.setDuration(source.getDuration());
    }

    @Override
    protected boolean isNotValidateNewElement(Film element) {
        if (element == null) return true;
        if (element.getName() == null) return true;
        return isNotValidateElementValues(element);
    }

    @Override
    protected boolean isNotValidateElementValues(Film element) {
        if (element == null) return true;
        if (element.getName() != null && element.getName().isEmpty()) return true;
        if (element.getDescription() != null && !element.getDescription().isEmpty()
                && element.getDescription().length() > 200) return true;
        if (element.getReleaseDate() != null
                && element.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) return true;
        return element.getDuration() != null && element.getDuration() <= 0;
    }
}
