package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Collection<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    public Film create(Film film) {
        return inMemoryFilmStorage.create(film);
    }

    public Film update(Film newElement) {
        return inMemoryFilmStorage.update(newElement);
    }

    public Film getOne(Long id) {
        return inMemoryFilmStorage.getOne(id);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = inMemoryFilmStorage.getOne(filmId);
        if (inMemoryUserStorage.getOne(userId) == null) {
            log.info("addLike - пользователь с id: {}, не найден", userId);
            throw new NotFoundException("Добавляемый пользователь не найден");
        }

        film.getLikes().add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = inMemoryFilmStorage.getOne(filmId);
        if (userId == null || inMemoryUserStorage.getOne(userId) == null) {
            log.info("deleteLike - пользователь с id: {}, не найден", userId);
            throw new NotFoundException("Удаляемый пользователь не найден");
        }
        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("Лайк от пользователя не найден");
        }
        film.getLikes().remove(userId);
    }

    public List<Film> getTopTen(int count) {
        return inMemoryFilmStorage.findAll().stream()
                .sorted(Comparator.comparing((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .toList();
    }
}
