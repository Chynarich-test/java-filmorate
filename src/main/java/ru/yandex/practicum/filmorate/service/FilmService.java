package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;
    private final Logger log;

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.log = LoggerFactory.getLogger(FilmService.class);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = inMemoryFilmStorage.getOne(filmId);
        if (userId == null || inMemoryFilmStorage.getOne(userId) == null) {
            log.info("addLike - пользователь с id: {}, не найден", userId);
            throw new NotFoundException("Добавляемый пользователь не найден");
        }

        film.getLikes().add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = inMemoryFilmStorage.getOne(filmId);
        film.getLikes().remove(userId);
    }

    public List<Film> getTopTen(int count) {
        return inMemoryFilmStorage.findAll().stream()
                .sorted(Comparator.comparing((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .toList();
    }
}
