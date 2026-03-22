describe('Профиль пользователя', () => {

  beforeEach(() => {
    cy.visit('/auth/login');

    cy.get('input[name="email"]').clear().type('demavarlamov@gmail.com');
    cy.get('input[name="password"]').clear().type('qwerty123');

    cy.get('button.auth-button').click();

    cy.url().should('include', '/catalog');

    cy.visit('/profile');

    cy.url().should('include', '/profile');
    cy.contains('h2.auth-title', 'Профиль пользователя').should('be.visible');
  });

  it('Страница профиля отображается корректно', () => {
    cy.get('input[type="email"]').should('be.disabled'); // FIX
    cy.get('input[name="firstName"]').should('be.visible');
    cy.get('input[name="lastName"]').should('be.visible');
    cy.get('input[name="phone"]').should('be.visible');
    cy.contains('button', 'Сохранить изменения').should('be.visible');
  });

  it('Редактирование своего профиля (валидными данными)', () => {
    cy.get('input[name="firstName"]').clear().type('Дмитрий');
    cy.get('input[name="lastName"]').clear().type('Тестов');
    cy.get('input[name="patronymic"]').clear().type('Юрьевич');
    cy.get('input[name="passportSeries"]').clear().type('1234');
    cy.get('input[name="passportNumber"]').clear().type('123456');
    cy.get('input[name="address"]').clear().type('Москва');
    cy.get('input[name="phone"]').clear().type('+7 999 123 45 67');

    cy.contains('button', 'Сохранить изменения').click();

    cy.url().should('include', '/profile');
  });

  it('Редактирование своего профиля (невалидными данными): пустое имя', () => {
    cy.get('input[name="firstName"]').clear();
    cy.get('input[name="lastName"]').clear().type('Тестов');

    cy.contains('button', 'Сохранить изменения').click();

    cy.get('input[name="firstName"]').then(input => {
      expect(input[0].checkValidity()).to.be.false;
    });
  });

  it('Редактирование своего профиля (невалидными данными): серия паспорта содержит буквы', () => {
    cy.get('input[name="passportSeries"]').clear().type('12ab');

    cy.contains('button', 'Сохранить изменения').click();

    cy.get('input[name="passportSeries"]').then(input => {
      expect(input[0].checkValidity()).to.be.false;
    });
  });

  it('Редактирование своего профиля (невалидными данными): номер паспорта содержит буквы', () => {
    cy.get('input[name="passportNumber"]').clear().type('123ab');

    cy.contains('button', 'Сохранить изменения').click();

    cy.get('input[name="passportNumber"]').then(input => {
      expect(input[0].checkValidity()).to.be.false;
    });
  });

  it('Редактирование своего профиля (невалидными данными): некорректный формат телефона', () => {
    cy.get('input[name="phone"]').clear().type('123');

    cy.contains('button', 'Сохранить изменения').click();

    cy.get('input[name="phone"]').then(input => {
      expect(input[0].checkValidity()).to.be.false;
    });
  });

  it('Выход из аккаунта из профиля', () => {
    cy.get('form[action="/logout"] button').click();

    cy.url().should('include', '/auth/login');
  });

});