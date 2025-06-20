package ru.yandex.practicum.filmorate.storage.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class InMemoryBaseStorage<T extends Entity> implements BaseStorage<T> {
    protected final Map<Long, T> elements = new HashMap<>();
    private final Logger log;

    protected InMemoryBaseStorage(Class<T> clazz) {
        this.log = LoggerFactory.getLogger(clazz);
    }

    public T getOne(Long id) {
        if (!elements.containsKey(id)) {
            log.info("Create - элемент с айди: {}, не найден", id);
            throw new NotFoundException("Элемент не найден");
        }
        return elements.get(id);
    }

    public Collection<T> findAll() {
        return elements.values();
    }

    public T create(T element) {

        if (isNotValidateNewElement(element)) {
            log.warn("Create - Ошибка валидации введенного элемента: {}", element.toString());
            throw new ValidationException("Ошибка валидации введенного элемента");
        }

        element = modifiedBeforeAdd(element);

        element.setId(getNextId());

        elements.put(element.getId(), element);
        return element;
    }

    public T update(T newElement) {
        if (isNotValidateElementValues(newElement)) {
            log.warn("Update - Ошибка валидации введенного элемента: {}", newElement.toString());
            throw new ValidationException("Ошибка валидации введенного элемента");
        }

        if (newElement.getId() == null) {
            log.warn("Id при вставке равен null");
            throw new ValidationException("Id не должен быть null");
        }

        T existingElement = elements.get(newElement.getId());

        if (existingElement == null) {
            log.warn("Update - Элемент с ID {} не найден для обновления", newElement.getId());
            throw new NotFoundException("Элемент с ID " + newElement.getId() + " не найден");
        }

        return Optional.ofNullable(elements.get(newElement.getId()))
                .map(element -> {
                    setElementValue(element, newElement);
                    log.info("Update - успешное обновление элемента в {}", element);
                    return element;
                }).orElse(null);
    }

    protected abstract T modifiedBeforeAdd(T element);


    protected abstract void setElementValue(T target, T source);

    protected abstract boolean isNotValidateNewElement(T element);

    protected abstract boolean isNotValidateElementValues(T element);

    private long getNextId() {
        long currentMaxId = elements.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
