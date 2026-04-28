const AuthPage = require('../../support/pages/auth/AuthPage');

describe('Выход из аккаунта', () => {
  const email = Cypress.env('AUTH_EMAIL');
  const password = Cypress.env('AUTH_PASSWORD');

  beforeEach(() => {
    AuthPage.visitLogin();

    expect(email).to.be.a('string').and.not.be.empty;
    expect(password).to.be.a('string').and.not.be.empty;

    AuthPage.fillLoginForm(email, password);
    AuthPage.submit();

    cy.url().should('include', '/catalog');
  });

  it('Logout (выход из системы)', () => {
  cy.intercept('POST', '/logout').as('logout');

  AuthPage.clickLogout();

  cy.wait('@logout')
    .its('response.statusCode')
    .should('be.oneOf', [200, 302, 303]);

  cy.url().should('include', '/auth/login');

  // пользователь не авторизован
  cy.get('form[action="/logout"]').should('not.exist');

  cy.contains('h2.auth-title', 'Добро пожаловать').should('be.visible');
});
});