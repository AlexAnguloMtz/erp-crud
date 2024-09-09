describe('Login Page Tests', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200/login');
  });

  it('visits login page', () => {
    cy.url().should('include', '/login');
  });

  it('fails on empty email', () => {
    cy.get('#email').clear();
    cy.get('#password').type('ValidPassword123');
    cy.get('#submit').click();
    cy.get('#email-error').should('contain', 'Ingresa un email');
  });

  it('fails on invalid email format', () => {
    cy.get('#email').type('invalid-email');
    cy.get('#password').type('ValidPassword123');
    cy.get('#submit').click();
    cy.get('#email-error').should('contain', 'Email inválido');
  });

  it('fails on long email', () => {
    cy.get('#email').type('a'.repeat(61) + '@example.com');
    cy.get('#password').type('ValidPassword123');
    cy.get('#submit').click();
    cy.get('#email-error').should('contain', 'Máximo 60 caracteres');
  });

  it('fails on empty password', () => {
    cy.get('#email').type('valid.email@example.com');
    cy.get('#password').clear();
    cy.get('#submit').click();
    cy.get('#password-error').should('contain', 'Ingresa una contraseña');
  });

  it('fails on short password', () => {
    cy.get('#email').type('valid.email@example.com');
    cy.get('#password').type('short');
    cy.get('#submit').click();
    cy.get('#password-error').should('contain', 'Mínimo 8 caracteres');
  });

  it('fails on long password', () => {
    cy.get('#email').type('valid.email@example.com');
    cy.get('#password').type('a'.repeat(61));
    cy.get('#submit').click();
    cy.get('#password-error').should('contain', 'Máximo 60 caracteres');
  });

  it('hides password by default', () => {
    cy.get('#password').should('have.attr', 'type', 'password');
  });

  it('shows password on show password click', () => {
    cy.get('#password-toggle').click();
    cy.get('#password').should('have.attr', 'type', 'text');
  });

  it('redirects to home page on successful login', () => {
    cy.get('#email').type('abarrey_root@gmail.com');
    cy.get('#password').type('12345678');
    cy.get('#submit').click();
    cy.url().should('eq', 'http://localhost:4200/home');
  });
});
