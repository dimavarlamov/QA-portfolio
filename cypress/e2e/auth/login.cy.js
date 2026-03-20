describe('Авторизация', () => {
  it('Авторизация (Валидная)', () => {
    cy.visit('/auth/login');
    cy.get('input[name="email"]').type('demavarlamov@gmail.com');
    cy.get('input[name="password"]').type('qwerty123');
    cy.get('.auth-button').click();
    cy.url().should('include', '/catalog');
  });
});