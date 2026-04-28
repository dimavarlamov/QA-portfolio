const AuthPage = require('../../support/pages/auth/AuthPage');

describe('Регистрация', () => {
  const uniqueEmail = () => `qa_${Date.now()}@mail.test`;

  beforeEach(() => {
    AuthPage.visitRegister();
  });

  it('Регистрация (валидные данные)', () => {
    AuthPage.fillRegisterForm({
      lastName: 'Иванов',
      firstName: 'Иван',
      patronymic: 'Иванович',
      email: uniqueEmail(),
      password: 'qwerty123!',
      matchingPassword: 'qwerty123!',
    });

    AuthPage.submit();

    cy.url().should('include', '/auth/login');
  });

  it('Регистрация (пустые поля)', () => {
    AuthPage.disableNativeValidation();
    AuthPage.submitFormDirectly();

    cy.url().should('include', '/register');
    AuthPage.shouldShowError();
    cy.get('.error li').should('have.length.greaterThan', 0);
  });

  it('Регистрация (невалидный email)', () => {
    AuthPage.disableNativeValidation();

    AuthPage.fillRegisterForm({
      lastName: 'Иванов',
      firstName: 'Иван',
      patronymic: 'Иванович',
      email: 'user@@mail',
      password: 'qwerty123',
      matchingPassword: 'qwerty123!',
    });

    AuthPage.submit();

    cy.url().should('include', '/register');
    AuthPage.shouldShowError();
    cy.get('.error').should('contain.text', 'email');
  });
});