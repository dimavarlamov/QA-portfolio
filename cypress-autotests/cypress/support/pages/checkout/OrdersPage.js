class OrdersPage {
  visit() {
    cy.visit('/profile/orders');
  }

  pageShouldBeVisible() {
    cy.contains('h1', 'Ваши покупки').should('be.visible');
  }

  verifyListOrEmptyState() {
    cy.get('body').then(($body) => {
      const hasOrders = $body.find('table.styled-table tbody tr').length > 0;

      if (hasOrders) {
        cy.get('table.styled-table').should('be.visible');
        cy.get('table.styled-table tbody tr').first().within(() => {
          cy.get('td').should('have.length.at.least', 2);
          cy.contains('a.btn-small', 'Детали').should('be.visible');
        });
      } else {
        cy.contains('p', 'У вас пока нет покупок.').should('be.visible');
      }
    });
  }

  openFirstOrderDetailsIfExists() {
    cy.get('body').then(($body) => {
      const hasOrders = $body.find('table.styled-table tbody tr').length > 0;

      if (hasOrders) {
        cy.get('table.styled-table tbody tr')
          .first()
          .contains('a.btn-small', 'Детали')
          .click();
      }
    });
  }

  verifyDetailsPageOrEmptyState() {
    cy.get('body').then(($body) => {
      const onDetailsPage = $body.find('.order-container').length > 0;

      if (onDetailsPage) {
        cy.contains('h1', 'Детали покупки №').should('be.visible');
        cy.get('.order-info').should('be.visible');
        cy.get('.car-card').should('be.visible');
        cy.get('.order-car-thumb').should('be.visible');
        cy.get('.car-price').should('contain.text', '₽');
        cy.contains('a.btn-back', 'Назад к списку заказов')
          .should('be.visible')
          .and('have.attr', 'href', '/profile/orders');
      } else {
        cy.contains('p', 'У вас пока нет покупок.').should('be.visible');
      }
    });
  }
}

module.exports = new OrdersPage();