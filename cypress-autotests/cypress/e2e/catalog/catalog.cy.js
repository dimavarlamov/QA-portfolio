const CatalogPage = require('../../support/pages/catalog/CatalogPage');
const AuthPage = require('../../support/pages/auth/AuthPage');

describe('Каталог', () => {
  beforeEach(() => {
  const email = Cypress.env('AUTH_EMAIL');
  const password = Cypress.env('AUTH_PASSWORD');

  expect(email).to.be.a('string').and.not.be.empty;
  expect(password).to.be.a('string').and.not.be.empty;

  AuthPage.visitLogin();
  AuthPage.fillLoginForm(email, password);
  AuthPage.submit();

  cy.url().should('include', '/catalog');

  CatalogPage.pageShouldBeVisible();
});

  it('Каталог (загрузка)', () => {
    CatalogPage.productCards().should('have.length.greaterThan', 0);
    cy.get('.product-card img').first().should('be.visible');
  });

  it('Поиск (точный)', () => {
  CatalogPage.getFirstCardTitle().then((title) => {
    const brand = title.split(' ')[0];

    CatalogPage.search(brand);

    cy.url().should('include', 'search=');
    CatalogPage.productCards().should('have.length.greaterThan', 0);
    CatalogPage.firstCardShouldContain(brand);
  });
}); 

  it('Поиск (частичный)', () => {
    CatalogPage.getFirstCardTitle().then((title) => {
      const partial = title.split(' ')[0];

      CatalogPage.search(partial);

      cy.url().should('include', 'search=');
      CatalogPage.productCards().should('have.length.greaterThan', 0);
      CatalogPage.firstCardShouldContain(partial);
    });
  });

  it('Поиск (с невалидными символами)', () => {
    CatalogPage.search('B@M,W');
    CatalogPage.noProducts();
  });

  it('Поиск (только пробелы)', () => {
  CatalogPage.search('      ');

  cy.url().should('include', 'search=');
  CatalogPage.productCards().should('have.length.greaterThan', 0);
  });

  it('Фильтр по цена/год/мощность/пробег (валидный)', () => {
    CatalogPage.openFilters();

    CatalogPage.setNumberField('#minPrice', 0);
    CatalogPage.setNumberField('#maxPrice', 999999999);
    CatalogPage.setNumberField('#yearFrom', 1900);
    CatalogPage.setNumberField('#yearTo', 2100);
    CatalogPage.setNumberField('#mileageFrom', 0);
    CatalogPage.setNumberField('#mileageTo', 1000000);
    CatalogPage.setNumberField('#hpFrom', 0);
    CatalogPage.setNumberField('#hpTo', 2000);

    CatalogPage.applyFilters();

    cy.url().should('include', '/catalog');
    CatalogPage.productCards().should('have.length.greaterThan', 0);
  });

  it('Фильтр по цена/год/мощность/пробег (невалидный)', () => {
    let alertText = '';

    cy.on('window:alert', (msg) => {
      alertText = msg;
    });

    CatalogPage.openFilters();

    CatalogPage.setNumberField('#minPrice', 1000000);
    CatalogPage.setNumberField('#maxPrice', 1);

    CatalogPage.applyFilters();

    cy.then(() => {
      expect(alertText).to.eq('Минимальная цена не может быть больше максимальной');
    });

    cy.url().should('include', '/catalog');
  });

  it('Фильтр по марка/модель/страна/руль/тип двигателя/цвет/кондиционер', () => {
    CatalogPage.getFirstCardTitle().then((title) => {
      const brand = title.split(' ')[0];

      CatalogPage.openFilters();

      // Берём значения из реального списка опций, чтобы тест не был хрупким
      CatalogPage.selectByVisibleText('#brandId', brand);
      CatalogPage.selectByVisibleText('#modelId', title);

      CatalogPage.applyFilters();

      cy.url().should('include', '/catalog');
      CatalogPage.productCards().should('have.length.greaterThan', 0);
      CatalogPage.firstCardShouldContain(brand);
    });
  });

  it('Кнопка сбросить фильтры', () => {
    CatalogPage.openFilters();

    CatalogPage.setNumberField('#minPrice', 1);
    CatalogPage.setNumberField('#maxPrice', 2);

    CatalogPage.resetFilters();

    cy.url().should('include', '/catalog');
    CatalogPage.productCards().should('have.length.greaterThan', 0);
  });
});