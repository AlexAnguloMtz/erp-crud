export const type = (selector, value) => {
    if (value !== undefined && value !== null && value !== '') {
        cy.get(selector).clear({ force: true }).type(value, { force: true });
    }
}