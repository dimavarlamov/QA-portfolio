const AuthPage = require('../../support/pages/auth/AuthPage');
const OrdersPage = require('../../support/pages/checkout/OrdersPage');

describe('Мои покупки', () => {
  const email = Cypress.env('BUYER_EMAIL') || Cypress.env('AUTH_EMAIL');
  const password = Cypress.env('BUYER_PASSWORD') || Cypress.env('AUTH_PASSWORD');

  beforeEach(() => {
    expect(email, 'buyer email').to.be.a('string').and.not.be.empty;
    expect(password, 'buyer password').to.be.a('string').and.not.be.empty;

    AuthPage.visitLogin();
    AuthPage.fillLoginForm(email, password);
    AuthPage.submit();

    cy.url().should('include', '/catalog');

    OrdersPage.visit();
    cy.url().should('include', '/profile/orders');
    OrdersPage.pageShouldBeVisible();
  });

  it('Страница "Мои покупки" отображается корректно', () => {
    OrdersPage.verifyListOrEmptyState();
  });

  it('Детали покупки открываются корректно', () => {
    OrdersPage.openFirstOrderDetailsIfExists();
    OrdersPage.verifyDetailsPageOrEmptyState();
  });
});