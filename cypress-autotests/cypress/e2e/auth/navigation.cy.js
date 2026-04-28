const AuthPage = require('../../support/pages/auth/AuthPage');

describe('Навигация между страницами auth', () => {
  it('С login страницы переход на регистрацию', () => {
    AuthPage.visitLogin();

    AuthPage.clickRegisterLink();
    cy.url().should('include', '/register');
    cy.contains('h2.auth-title', 'Регистрация').should('be.visible');
  });

  it('С регистрации переход на login', () => {
    AuthPage.visitRegister();

    AuthPage.clickLoginLink();
    cy.url().should('include', '/auth/login');
    cy.contains('h2.auth-title', 'Добро пожаловать').should('be.visible');
  });
});