package com.example.factory;

import com.example.annotation.Cumponent;
import com.example.facade.EditorialSectionFacade;
import com.example.model.EditorialSection;

@Cumponent
public class EditorialSectionFactory {

    public EditorialSection create(Long id, EditorialSectionFacade facade) {
        return EditorialSection.newInstance(id, facade.name(), facade.availableForPublication());
    }
}
