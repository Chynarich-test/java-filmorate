package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class BaseController<T extends Entity> {
    private final Map<Long, T> elements = new HashMap<>();
    private final Logger log;

    protected BaseController(Class<T> clazz) {
        this.log = LoggerFactory.getLogger(clazz);
    }

    @GetMapping
    public Collection<T> findAll() {
        return elements.values();
    }

    @PostMapping
    public T create(@RequestBody T element) {

        if (isNotValidateNewElement(element)) {
            log.warn("Create - Ошибка валидации введенного элемента: {}", element.toString());
            throw new ValidationException("Ошибка валидации введенного элемента");
        }

        element = modifiedBeforeAdd(element);

        element.setId(getNextId());

        elements.put(element.getId(), element);
        return element;
    }

    @PutMapping
    public T update(@RequestBody T newElement) {
        if (isNotValidateElementValues(newElement)) {
            log.warn("Update - Ошибка валидации введенного элемента: {}", newElement.toString());
            throw new ValidationException("Ошибка валидации введенного элемента");
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

    protected abstract boolean isNotValidateNewElement(@RequestBody T element);

    protected abstract boolean isNotValidateElementValues(@RequestBody T element);


    private long getNextId() {
        long currentMaxId = elements.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
