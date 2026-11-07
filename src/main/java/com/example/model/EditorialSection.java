package com.example.model;

public final class EditorialSection {

    private Long id;
    private String name;
    private boolean availableForPublication;

    private EditorialSection() {
    }

    public static EditorialSection newInstance(Long id, String name, boolean availableForPublication) {
        EditorialSection section = new EditorialSection();
        section.id = id;
        section.name = name;
        section.availableForPublication = availableForPublication;
        return section;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailableForPublication() {
        return availableForPublication;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvailableForPublication(boolean availableForPublication) {
        this.availableForPublication = availableForPublication;
    }
}
