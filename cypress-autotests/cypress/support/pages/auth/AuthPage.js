class AuthPage {
  visitLogin() {
    cy.visit('/auth/login');
  }

  visitRegister() {
    cy.visit('/register');
  }

  fillLoginForm(email, password) {
    cy.get('input[name="email"]').clear().type(email);
    cy.get('input[name="password"]').clear().type(password, { log: false });
  }

  fillRegisterForm({
    lastName,
    firstName,
    patronymic = '',
    email,
    password,
    matchingPassword,
  }) {
    cy.get('input[name="lastName"]').clear().type(lastName);
    cy.get('input[name="firstName"]').clear().type(firstName);
    cy.get('input[name="patronymic"]').clear().type(patronymic);
    cy.get('input[name="email"]').clear().type(email);
    cy.get('input[name="password"]').clear().type(password, { log: false });
    cy.get('input[name="matchingPassword"]').clear().type(matchingPassword, { log: false });
  }

  submit() {
    cy.get('button.auth-button').click();
  }

  submitFormDirectly() {
    cy.get('form.auth-form').submit();
  }

  disableNativeValidation() {
    cy.get('form.auth-form').invoke('attr', 'novalidate', 'novalidate');
  }

  shouldShowError() {
    cy.get('.error').should('be.visible');
  }

  shouldShowSuccess() {
    cy.get('.success').should('be.visible');
  }

  clickRegisterLink() {
    cy.contains('.auth-link a', 'Зарегистрироваться').click();
  }

  clickLoginLink() {
    cy.contains('.auth-link a', 'Войти').click();
  }

  clickLogout() {
  cy.contains('button', 'Выйти').click();
}

}

module.exports = new AuthPage();