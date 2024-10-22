import { WEB_URL } from '../environment/environment';

export const visitLoginPage = () => {
    cy.visit(WEB_URL + '/login');
}

export const logIn = ({ email, password }) => {
    cy.get('#email').type(email);
    cy.get('#password').type(password);
    cy.get('#submit').click();

    // Users should be redirected to main page after they log in
    cy.url().should('eq', WEB_URL + '/home');
}