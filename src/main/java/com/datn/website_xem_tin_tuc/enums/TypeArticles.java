package com.datn.website_xem_tin_tuc.enums;

public enum TypeArticles {
    ARTICLE("Bài viết"),
    VIDEO("Video"),
    PODCAST("Podcast"),
    IMAGE("Hình ảnh"),
    SLIDE("Slide");

    private final String label;

    TypeArticles(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
