package com.cafy.application.view;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as= IcategoryView.class)
public interface iCategoryView {

    int getNumberOfCategory();
}
