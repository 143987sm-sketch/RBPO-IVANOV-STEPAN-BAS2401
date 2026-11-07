package com.example.service;

import com.example.annotation.Cumponent;
import com.example.facade.UserFacade;
import com.example.factory.UserFactory;
import com.example.model.Article;
import com.example.model.User;
import com.example.repository.ArticleRepository;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Cumponent
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    public UserService(
            UserRepository userRepository,
            UserFactory userFactory,
            ArticleRepository articleRepository,
            ArticleService articleService
    ) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.articleRepository = articleRepository;
        this.articleService = articleService;
    }

    public User create(UserFacade facade) {
        User draft = userFactory.create(0L, facade);
        return userRepository.insert(draft);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> update(Long id, UserFacade facade) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(facade.username());
            user.setDisplayName(facade.displayName());
            return user;
        });
    }

    public boolean delete(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        List<Article> articles = articleRepository.findByAuthorId(id);
        for (Article article : articles) {
            articleService.delete(article.getId());
        }
        return userRepository.deleteById(id);
    }
}
