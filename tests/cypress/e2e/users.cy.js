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

            fillCreateUserForm({
                values: validUser(),
                selectState: true,
                selectRole: true
            });

            cy.get('#create-user-submit').click();

            cy.get('#user-saved-dialog').should('contain', 'Se guardó el usuario');
        });

        describe('cannot create user with invalid data', () => {
            const testCases = [
                {
                    testName: 'Name is required',
                    prepareInput: (input) => input.values.name = '',
                    errorSelector: 'name',
                    expectedError: 'Valor requerido',
                },
            ];

            testCases.forEach(test => {
                it(test.testName, () => {
                    const input = {
                        values: validUser(),
                        selectState: true,
                        selectRole: true
                    };

                    test.prepareInput(input);

                    cy.get('#create-new').click();

                    fillCreateUserForm({
                        values: input.values,
                        selectState: input.selectState,
                        selectRole: input.selectRole
                    })

                    cy.get('#create-user-submit').click();

                    cy.get(`.form-error.${test.errorSelector}`).should('contain', test.expectedError);
                })
            })
        });
    });
});

// ######################## Helper functions ########################

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

function fillCreateUserForm({ values, selectState, selectRole }) {
    cy.get('#create-user-form').within(() => {
        type('#create-user-name', values.name);
        type('#create-user-last-name', values.lastName);
        type('#create-user-city', values.city);
        type('#create-user-district', values.district);
        type('#create-user-street', values.street);
        type('#create-user-street-number', values.streetNumber);
        type('#create-user-zip-code', values.zipcode);
        type('#create-user-email', values.email);
        type('#create-user-phone', values.phone);
        type('#create-user-password', values.password);
        type('#create-user-confirmed-password', values.confirmedPassword);
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

const type = (selector, value) => {
    if (value !== undefined && value !== null && value !== '') {
        cy.get(selector).type(value);
    }
};