class CarDetailsPage {
  openFirstCarFromCatalog() {
    cy.visit('/catalog');
    cy.get('.product-card').should('have.length.greaterThan', 0);
    cy.get('.product-card .details-btn').first().click();
  }

  pageShouldBeVisible() {
    cy.contains('h1', /.+/).should('be.visible');
    cy.get('.car-details-container').should('be.visible');
  }

  verifyCarInfo() {
    cy.get('#mainCarImage').should('be.visible');
    cy.contains('h3', 'Характеристики').should('be.visible');
    cy.get('.specs').should('be.visible');
    cy.get('.price').should('be.visible');
  }

  buyButtonShouldBeVisible() {
    cy.contains('button.btn.btn-success', 'Купить').should('be.visible');
  }

  buyCurrentCar() {
    cy.on('window:confirm', () => true);
    cy.contains('button.btn.btn-success', 'Купить').click();
  }
}

module.exports = new CarDetailsPage();