package com.ideaas.ecomm.ecomm.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Image {

    private String url;
    private boolean isMain;
    private String link;

    public Image(String url, boolean isMain) {
        this.url = url;
        this.isMain = isMain;
    }
}
