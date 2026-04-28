class CatalogPage {
  visit() {
    cy.visit('/catalog');
  }

  pageShouldBeVisible() {
    cy.get('.product-grid').should('be.visible');
  }

  productCards() {
    return cy.get('.product-card');
  }

  noProducts() {
    cy.get('.no-products')
      .should('be.visible')
      .and('contain.text', 'Автомобили не найдены');
  }

  search(term) {
    cy.get('.search-input').clear().type(term);
    cy.get('.search-form').submit();
  }

  openFilters() {
    cy.get('.filter-button').click();
    cy.get('.filter-form').should('have.class', 'active');
  }

  setNumberField(selector, value) {
    cy.get(selector).clear().type(String(value));
  }

  selectByVisibleText(selector, text) {
    cy.get(selector).then(($select) => {
      const options = [...$select[0].options];
      const match = options.find((opt) => opt.text.trim() === text.trim());

      if (match) {
        cy.wrap($select).select(match.value);
      } else {
        throw new Error(`Не найден option "${text}" в ${selector}`);
      }
    });
  }

  applyFilters() {
    cy.get('#filter-form').submit();
  }

  resetFilters() {
    cy.get('.reset-btn').click();
  }

  getFirstCardTitle() {
    return cy
      .get('.product-card h3')
      .first()
      .invoke('text')
      .then((text) => text.trim().replace(/\s+/g, ' '));
  }

  firstCardShouldContain(text) {
    cy.get('.product-card h3').first().should('contain.text', text);
  }

  firstCardShouldExist() {
    cy.get('.product-card').first().should('be.visible');
  }
}

module.exports = new CatalogPage();