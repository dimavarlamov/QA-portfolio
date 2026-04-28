const AuthPage = require('../../support/pages/auth/AuthPage');
const CarDetailsPage = require('../../support/pages/checkout/CarDetailsPage');

describe('Покупка автомобиля', () => {
  const email = Cypress.env('BUYER_EMAIL') || Cypress.env('AUTH_EMAIL');
  const password = Cypress.env('BUYER_PASSWORD') || Cypress.env('AUTH_PASSWORD');

  beforeEach(() => {
    expect(email, 'buyer email').to.be.a('string').and.not.be.empty;
    expect(password, 'buyer password').to.be.a('string').and.not.be.empty;

    AuthPage.visitLogin();
    AuthPage.fillLoginForm(email, password);
    AuthPage.submit();

    cy.url().should('include', '/catalog');

    CarDetailsPage.openFirstCarFromCatalog();
    CarDetailsPage.pageShouldBeVisible();
  });

  it('Карточка автомобиля отображается и доступна кнопка "Купить"', () => {
    CarDetailsPage.verifyCarInfo();
    CarDetailsPage.buyButtonShouldBeVisible();
  });

  it('Оформление покупки покупателем', () => {
    cy.intercept('POST', '/purchase/buy/*').as('buyCar');

    CarDetailsPage.buyCurrentCar();

    cy.wait('@buyCar').then(({ response }) => {
      expect(response?.statusCode).to.be.oneOf([200, 302, 303]);
    });

    cy.location('pathname').should('match', /\/profile\/orders|\/cars\/\d+/);

    cy.get('body')
      .invoke('text')
      .should('match', /(Ваши покупки|Детали покупки|успеш|покуп)/i);
  });
});