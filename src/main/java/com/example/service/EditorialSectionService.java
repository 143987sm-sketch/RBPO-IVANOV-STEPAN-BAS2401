package com.example.service;

import com.example.annotation.Cumponent;
import com.example.facade.EditorialSectionFacade;
import com.example.factory.EditorialSectionFactory;
import com.example.model.Article;
import com.example.model.EditorialSection;
import com.example.repository.ArticleRepository;
import com.example.repository.EditorialSectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Cumponent
@Service
public class EditorialSectionService {

    private final EditorialSectionRepository sectionRepository;
    private final EditorialSectionFactory sectionFactory;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    public EditorialSectionService(
            EditorialSectionRepository sectionRepository,
            EditorialSectionFactory sectionFactory,
            ArticleRepository articleRepository,
            ArticleService articleService
    ) {
        this.sectionRepository = sectionRepository;
        this.sectionFactory = sectionFactory;
        this.articleRepository = articleRepository;
        this.articleService = articleService;
    }

    public EditorialSection create(EditorialSectionFacade facade) {
        EditorialSection draft = sectionFactory.create(0L, facade);
        return sectionRepository.insert(draft);
    }

    public List<EditorialSection> findAll() {
        return sectionRepository.findAll();
    }

    public Optional<EditorialSection> findById(Long id) {
        return sectionRepository.findById(id);
    }

    public Optional<EditorialSection> update(Long id, EditorialSectionFacade facade) {
        return sectionRepository.findById(id).map(section -> {
            section.setName(facade.name());
            section.setAvailableForPublication(facade.availableForPublication());
            return section;
        });
    }

    public boolean delete(Long id) {
        if (!sectionRepository.existsById(id)) {
            return false;
        }
        List<Article> articles = articleRepository.findBySectionId(id);
        for (Article article : articles) {
            articleService.delete(article.getId());
        }
        return sectionRepository.deleteById(id);
    }
}
