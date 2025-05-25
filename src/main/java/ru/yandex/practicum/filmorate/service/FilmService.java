package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public void addLike(Long filmId, Long userId) {
        Film film = inMemoryFilmStorage.getOne(filmId);

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
