const AuthPage = require('../../support/pages/auth/AuthPage');
const FavoritesPage = require('../../support/pages/favorites/FavoritesPage');

describe('Избранное', () => {
  const email = Cypress.env('AUTH_EMAIL');
  const password = Cypress.env('AUTH_PASSWORD');

  const normalize = (text) => text.trim().replace(/\s+/g, ' ');

  const loginAsBuyer = () => {
    expect(email, 'AUTH_EMAIL').to.be.a('string').and.not.be.empty;
    expect(password, 'AUTH_PASSWORD').to.be.a('string').and.not.be.empty;

    AuthPage.visitLogin();
    AuthPage.fillLoginForm(email, password);
    AuthPage.submit();

    cy.url().should('include', '/catalog');
  };

  const getFirstCatalogTitle = () => {
    return cy
      .get('.product-card h3')
      .first()
      .invoke('text')
      .then(normalize);
  };

  const ensureFirstCatalogCardNotFavorite = () => {
    cy.get('.product-card').first().find('button.favorite-btn').then(($btn) => {
      const aria = ($btn.attr('aria-label') || '').trim();
      const isActive = $btn.hasClass('active') || aria.includes('Убрать из избранного');

      if (isActive) {
        cy.wrap($btn).click();
      }
    });
  };

  const openFirstCatalogCarDetails = () => {
    cy.get('.product-card').first().find('a.details-btn').click();
    cy.url().should('match', /\/cars\/\d+/);
  };

  beforeEach(() => {
    loginAsBuyer();
  });

  it('Страница избранного отображается корректно', () => {
    FavoritesPage.visit();
    cy.url().should('include', '/favorites');
    FavoritesPage.pageShouldBeVisible();
    FavoritesPage.verifyEmptyOrListState();
  });

  it('Добавление в избранное (сердечко на карточке в каталоге)', () => {
    getFirstCatalogTitle().then((title) => {
      ensureFirstCatalogCardNotFavorite();

      cy.get('.product-card').first().find('button.favorite-btn').click();

      FavoritesPage.visit();
      FavoritesPage.pageShouldBeVisible();
      FavoritesPage.verifyCardWithTitle(title);
      FavoritesPage.verifyTitleExistsExactlyOnce(title);
    });
  });

  it('Удаление из избранного (сердечко в списке избранного)', () => {
    getFirstCatalogTitle().then((title) => {
      ensureFirstCatalogCardNotFavorite();
      cy.get('.product-card').first().find('button.favorite-btn').click();

      FavoritesPage.visit();
      FavoritesPage.verifyCardWithTitle(title);

      FavoritesPage.clickFavoriteToggleByTitle(title);

      FavoritesPage.verifyTitleAbsent(title);
    });
  });

  it('Добавление в избранное (кнопка ❤ на странице автомобиля)', () => {
    getFirstCatalogTitle().then((title) => {
      ensureFirstCatalogCardNotFavorite();

      openFirstCatalogCarDetails();
      cy.contains('button.favorite-btn-large', 'Добавить в избранное').should('be.visible').click();

      FavoritesPage.visit();
      FavoritesPage.verifyCardWithTitle(title);
      FavoritesPage.verifyTitleExistsExactlyOnce(title);
    });
  });

  it('Удаление из избранного после добавления с карточки автомобиля', () => {
    getFirstCatalogTitle().then((title) => {
      ensureFirstCatalogCardNotFavorite();

      openFirstCatalogCarDetails();
      cy.contains('button.favorite-btn-large', 'Добавить в избранное').click();

      FavoritesPage.visit();
      FavoritesPage.verifyCardWithTitle(title);

      FavoritesPage.clickFavoriteToggleByTitle(title);

      FavoritesPage.verifyTitleAbsent(title);
    });
  });

  it('Открытие карточки автомобиля из избранного', () => {
    getFirstCatalogTitle().then((title) => {
      ensureFirstCatalogCardNotFavorite();
      cy.get('.product-card').first().find('button.favorite-btn').click();

      FavoritesPage.visit();
      FavoritesPage.clickDetailsByTitle(title);

      cy.contains('h1', title).should('be.visible');
      cy.get('.car-details-container').should('be.visible');
    });
  });

  it('Избранное: защита от дублей при повторном добавлении', () => {
    getFirstCatalogTitle().then((title) => {
      // 1) добавили
      ensureFirstCatalogCardNotFavorite();
      cy.get('.product-card').first().find('button.favorite-btn').click();

      FavoritesPage.visit();
      FavoritesPage.verifyTitleExistsExactlyOnce(title);

      // 2) убрали
      cy.visit('/catalog');
      cy.url().should('include', '/catalog');
      ensureFirstCatalogCardNotFavorite();

      // 3) добавили снова
      cy.get('.product-card').first().find('button.favorite-btn').click();

      FavoritesPage.visit();
      FavoritesPage.verifyTitleExistsExactlyOnce(title);
    });
  });
});