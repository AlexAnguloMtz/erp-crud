import { WEB_URL } from "../environment/environment";

describe('home page', () => {
    it('redirects to login page when user is not authenticated', () => {
        cy.visit(WEB_URL + '/home');
        cy.url().should('eq', WEB_URL + '/login');
    });

    it('redirects to login page when user logs out', () => {
        cy.visit(WEB_URL + '/login');

        cy.get('#email').type('abarrey_root@gmail.com');
        cy.get('#password').type('12345678');
        cy.get('#submit').click();

        cy.url().should('eq', WEB_URL + '/home');

        cy.get('#user-options').click();

        cy.get('#logout').click();

        cy.url().should('eq', WEB_URL + '/login');
    });
})