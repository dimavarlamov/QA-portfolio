class ProfilePage {
  visit() {
    cy.visit('/profile');
  }

  pageShouldBeVisible() {
    cy.contains('h2.auth-title', 'Профиль пользователя').should('be.visible');
  }

  balanceShouldBeVisible() {
    cy.contains('.auth-link', 'Баланс:').should('be.visible');
  }

  logout() {
    cy.contains('button', 'Выйти').click();
  }

  fillForm({
    firstName,
    lastName,
    patronymic = '',
    passportSeries = '',
    passportNumber = '',
    address = '',
    phone = '',
  }) {
    cy.get('input[name="firstName"]').clear().type(firstName);
    cy.get('input[name="lastName"]').clear().type(lastName);
    cy.get('input[name="patronymic"]').clear().type(patronymic);
    cy.get('input[name="passportSeries"]').clear().type(passportSeries);
    cy.get('input[name="passportNumber"]').clear().type(passportNumber);
    cy.get('input[name="address"]').clear().type(address);
    cy.get('input[name="phone"]').clear().type(phone);
  }

  submit() {
    cy.contains('button.auth-button', 'Сохранить изменения').click();
  }

  emailFieldShouldBeReadonly() {
    cy.get('input[type="email"]').should('be.disabled');
  }

  firstNameShouldHaveValue(value) {
    cy.get('input[name="firstName"]').should('have.value', value);
  }

  lastNameShouldHaveValue(value) {
    cy.get('input[name="lastName"]').should('have.value', value);
  }

  passportSeriesShouldBeInvalid() {
    cy.get('input[name="passportSeries"]').should('be.invalid');
  }

  passportNumberShouldBeInvalid() {
    cy.get('input[name="passportNumber"]').should('be.invalid');
  }

  phoneShouldBeInvalid() {
    cy.get('input[name="phone"]').should('be.invalid');
  }

  invalidFieldShouldBeInvalid(name) {
    cy.get(`input[name="${name}"]`).should('be.invalid');
  }

  invalidFieldShouldBeValid(name) {
    cy.get(`input[name="${name}"]`).should('be.valid');
  }
}

module.exports = new ProfilePage();