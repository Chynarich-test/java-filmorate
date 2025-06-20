package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newElement) {
        return userStorage.update(newElement);
    }

    public User getOne(Long id) {
        return userStorage.getOne(id);
    }

    public List<User> getFriends(Long id) {
        return userStorage.getFriends(id);
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.getOne(userId);
        userStorage.getOne(friendId);

        userStorage.addFriendship(userId, friendId, FriendshipStatus.FRIENDS);

        //Ужас, вот я не понимаю, в ТЗ написано "у пользователей Filmorate должна быть возможность
        // добавлять друг друга в друзья с подтверждением дружбы"
        //При этом как по тестам выяснилось, никаких подтверждений не надо, если добавил, то он сразу твой друг
        //Те кто пишут задания, специально издеваются над студентами

//        Optional<FriendshipStatus> inverseRequest = userStorage.getFriendshipStatus(friendId, userId);
//
//        if (inverseRequest.isPresent() && inverseRequest.get() == FriendshipStatus.PENDING_SENT) {
//            userStorage.updateFriendshipStatus(friendId, userId, FriendshipStatus.FRIENDS);
//            userStorage.addFriendship(userId, friendId, FriendshipStatus.FRIENDS);
//        } else {
//            userStorage.addFriendship(userId, friendId, FriendshipStatus.PENDING_SENT);
//        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        userStorage.getOne(userId);
        userStorage.getOne(friendId);

        userStorage.removeFriendship(userId, friendId);
    }

    public List<User> showMutualFriends(Long userId1, Long userId2) {
        List<User> friends1 = userStorage.getFriends(userId1);
        List<User> friends2 = userStorage.getFriends(userId2);

        friends1.retainAll(friends2);
        return friends1;
    }
}
