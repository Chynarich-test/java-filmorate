package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("genreDbStorage") GenreStorage genreStorage,
                       @Qualifier("mpaDbStorage") MpaStorage mpaStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        validateFilmGenres(film);
        mpaStorage.getOne(film.getMpa().getId());
        return filmStorage.create(film);
    }

    public Film update(Film newElement) {
        validateFilmGenres(newElement);
        mpaStorage.getOne(newElement.getMpa().getId());
        return filmStorage.update(newElement);
    }

    public Film getOne(Long id) {
        return filmStorage.getOne(id);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getOne(filmId);
        if (userStorage.getOne(userId) == null) {
            log.info("addLike - пользователь с id: {}, не найден", userId);
            throw new NotFoundException("Добавляемый пользователь не найден");
        }

        film.getLikes().add(userId);
        filmStorage.update(film);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getOne(filmId);
        if (userId == null || userStorage.getOne(userId) == null) {
            log.info("deleteLike - пользователь с id: {}, не найден", userId);
            throw new NotFoundException("Удаляемый пользователь не найден");
        }
        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("Лайк от пользователя не найден");
        }
        film.getLikes().remove(userId);
        filmStorage.update(film);
    }

    private void validateFilmGenres(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }

        Set<Long> existingGenreIds = genreStorage.findAll().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        for (Genre requestedGenre : film.getGenres()) {
            if (!existingGenreIds.contains(requestedGenre.getId())) {
                throw new NotFoundException("Жанр с id = " + requestedGenre.getId() + " не найден.");
            }
        }
    }

    //TODO: Сделать отдельный sql запрос
    public List<Film> getTopTen(int count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparing((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .toList();
    }
}
