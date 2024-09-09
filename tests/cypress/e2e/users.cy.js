import { faker } from '@faker-js/faker';

describe('users module', () => {
    beforeEach(() => {
        cy.visit('http://localhost:4200/login');

        cy.get('#email').type('abarrey_root@gmail.com');
        cy.get('#password').type('12345678');
        cy.get('#submit').click();

        cy.url().should('eq', 'http://localhost:4200/home');

        cy.get('#module-users').click();
    });

    context('create user', () => {
        it('user was created successfully', () => {
            cy.get('#create-new').click();

            fillUserForm({
                selector: '#create-user-form',
                values: validUser(),
                selectState: true,
                selectRole: true
            });

            cy.get('#create-user-submit').click();

            cy.get('#user-saved-dialog').should('contain', 'Se guardó el usuario');
        });

        it('', () => {
        });

        it('', () => {
        });
    });
});

const validUser = () => {
    const password = faker.internet.password();
    return {
        name: faker.person.firstName(),
        lastName: faker.person.lastName(),
        city: faker.location.city(),
        district: faker.location.secondaryAddress(),
        street: faker.location.street(),
        streetNumber: faker.location.buildingNumber(),
        zipcode: faker.string.numeric(5),
        email: faker.internet.email(),
        phone: faker.string.numeric(10),
        password: password,
        confirmedPassword: password,
    }
}

function fillUserForm({ selector, values, selectState, selectRole }) {
    cy.get(selector).within(() => {
        cy.get('#create-user-name').type(values.name);
        cy.get('#create-user-last-name').type(values.lastName);
        cy.get('#create-user-city').type(values.city);
        cy.get('#create-user-district').type(values.district);
        cy.get('#create-user-street').type(values.street);
        cy.get('#create-user-street-number').type(values.streetNumber);
        cy.get('#create-user-zip-code').type(values.zipcode);
        cy.get('#create-user-email').type(values.email);
        cy.get('#create-user-phone').type(values.phone);
        cy.get('#create-user-password').type(values.password);
        cy.get('#create-user-confirmed-password').type(values.confirmedPassword);
    });

    if (selectState) {
        clickRandomDropdownValue('#create-user-state');
    }

    if (selectRole) {
        clickRandomDropdownValue('#create-user-role');
    }
}

function clickRandomDropdownValue(id) {
    cy.get(id).click();

    cy.get(`${id} ${id}_list`)
        .children()
        .then(children => {
            const childrenArray = Array.from(children);

            const randomIndex = Math.floor(Math.random() * childrenArray.length);
            const randomChild = childrenArray[randomIndex];

            cy.wrap(randomChild).click();
        });
}