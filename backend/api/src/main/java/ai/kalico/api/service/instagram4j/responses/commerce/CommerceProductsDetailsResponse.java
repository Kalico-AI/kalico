package com.kalico.api.service.instagram4j.responses.commerce;

import com.kalico.api.service.instagram4j.models.commerce.Product;
import com.kalico.api.service.instagram4j.models.user.Profile;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommerceProductsDetailsResponse extends IGResponse {
    private Profile merchant;
    private Product product_item;
}
