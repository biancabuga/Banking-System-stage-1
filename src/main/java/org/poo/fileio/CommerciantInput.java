package org.poo.fileio;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public final class CommerciantInput {
    private int id;
    private String description;
    private List<String> commerciants;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<String> getCommerciants() {
        return commerciants;
    }

    public void setCommerciants(final List<String> commerciants) {
        this.commerciants = commerciants;
    }
}
