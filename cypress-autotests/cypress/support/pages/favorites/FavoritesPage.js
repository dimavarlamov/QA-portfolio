class FavoritesPage {
  visit() {
    cy.visit('/favorites');
  }

  pageShouldBeVisible() {
    cy.contains('h1', 'Избранные автомобили').should('be.visible');
  }

  verifyEmptyOrListState() {
    cy.get('body').then(($body) => {
      if ($body.find('.empty-message').length > 0) {
        cy.get('.empty-message').should('be.visible');
        cy.contains('.empty-message p', 'У вас пока нет избранных автомобилей.').should('be.visible');
        cy.contains('.empty-message a.btn', 'Перейти в каталог')
          .should('be.visible')
          .and('have.attr', 'href', '/catalog');
      } else {
        cy.get('.product-grid').should('be.visible');
        cy.get('.product-card').should('have.length.greaterThan', 0);
      }
    });
  }

  firstCardTitle() {
    return cy
      .get('.product-card h3')
      .first()
      .invoke('text')
      .then((text) => text.trim().replace(/\s+/g, ' '));
  }

  verifyCardWithTitle(title) {
    cy.contains('.product-card h3', title).should('be.visible');
  }

  verifyTitleExistsExactlyOnce(title) {
    cy.get('.product-card h3').then(($titles) => {
      const matches = [...$titles].filter((el) => {
        const current = el.textContent.trim().replace(/\s+/g, ' ');
        return current === title;
      }).length;

      expect(matches, `cards with title "${title}"`).to.eq(1);
    });
  }

  verifyTitleAbsent(title) {
    cy.contains('.product-card h3', title).should('not.exist');
  }

  clickFavoriteToggleByTitle(title) {
    cy.contains('.product-card', title).within(() => {
      cy.get('button.favorite-btn').click();
    });
  }

  clickDetailsByTitle(title) {
    cy.contains('.product-card', title).within(() => {
      cy.get('a.details-btn').click();
    });
  }
}

module.exports = new FavoritesPage();