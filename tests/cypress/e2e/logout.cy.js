it('log out with redirection to login page', () => {
    cy.visit('http://localhost:4200/login');

    cy.get('#email').type('abarrey_root@gmail.com');
    cy.get('#password').type('12345678');
    cy.get('#submit').click();

    cy.url().should('eq', 'http://localhost:4200/home');

    cy.get('#user-options').click();

    cy.get('#logout').click();

    cy.url().should('eq', 'http://localhost:4200/login');
});