const AuthPage = require('../../support/pages/auth/AuthPage');

describe('Авторизация', () => {
  const email = Cypress.env('AUTH_EMAIL');
  const password = Cypress.env('AUTH_PASSWORD');

  beforeEach(() => {
    AuthPage.visitLogin();
  });

  it('Авторизация (валидная)', () => {
    expect(email, 'AUTH_EMAIL').to.be.a('string').and.not.be.empty;
    expect(password, 'AUTH_PASSWORD').to.be.a('string').and.not.be.empty;

    AuthPage.fillLoginForm(email, password);
    AuthPage.submit();

    cy.url().should('include', '/catalog');
  });

  it('Авторизация (неверный пароль)', () => {
    expect(email, 'AUTH_EMAIL').to.be.a('string').and.not.be.empty;

    AuthPage.fillLoginForm(email, 'wrong-password-123');
    AuthPage.submit();

    cy.url().should('include', '/auth/login');
    AuthPage.shouldShowError();
    cy.get('.error')
      .should('be.visible')
      .and('contain.text', 'Неверное имя пользователя или пароль');
  });
});