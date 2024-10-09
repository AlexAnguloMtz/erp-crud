import { WEB_URL } from '../environment/environment';
import { faker } from '@faker-js/faker';

describe('users module', () => {
    beforeEach(() => {
        cy.visit(WEB_URL + '/login');

        cy.get('#email').type('abarrey_root@gmail.com');
        cy.get('#password').type('12345678');
        cy.get('#submit').click();

        cy.url().should('eq', WEB_URL + '/home');

        cy.get('#module-users').click();
    });

    context('create user', () => {
        it('user was created successfully', () => {
            cy.get('#create-new').click();

            fillCreateUserForm({
                values: validUser(),
                selectRole: true
            });

            cy.get('#create-user-submit').click();

            cy.get('#user-saved-dialog').should('contain', 'Se guardó el usuario');
        });

        it('two users cannot have the same email', () => {
            // Save first user
            const firstUser = validUser();

            cy.get('#create-new').click();

            fillCreateUserForm({
                values: firstUser,
                selectRole: true
            });

            cy.get('#create-user-submit').click();

            cy.get('#user-saved-dialog').should('contain', 'Se guardó el usuario');

            cy.get("#close-saved-user-dialog").click();

            // Try to save second user with same email
            const secondUser = validUser();

            secondUser.email = firstUser.email;

            cy.get('#create-new').click();

            fillCreateUserForm({
                values: firstUser,
                selectRole: true
            });

            cy.get('#create-user-submit').click();

            // Check error message for duplicated email
            cy.get('#message-dialog').within(() => {
                cy.contains('Usuario ya existe').should('exist');
                cy.contains('El correo ya pertenece a otro usuario').should('exist');
            });
        })

        describe('empty fields validations', () => {
            const testCases = [
                {
                    testName: 'Name is required',
                    prepareInput: (input) => input.values.name = '',
                    errorSelector: 'name',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Last name is required',
                    prepareInput: (input) => input.values.lastName = '',
                    errorSelector: 'last-name',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'District is required',
                    prepareInput: (input) => input.values.district = '',
                    errorSelector: 'district',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Street is required',
                    prepareInput: (input) => input.values.street = '',
                    errorSelector: 'street',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Street number is required',
                    prepareInput: (input) => input.values.streetNumber = '',
                    errorSelector: 'street-number',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Phone is required',
                    prepareInput: (input) => input.values.phone = '',
                    errorSelector: 'phone',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Zip code is required',
                    prepareInput: (input) => input.values.zipCode = '',
                    errorSelector: 'zip-code',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Email is required',
                    prepareInput: (input) => input.values.email = '',
                    errorSelector: 'email',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Role is required',
                    prepareInput: (input) => input.selectRole = false,
                    errorSelector: 'role',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Password is required',
                    prepareInput: (input) => input.values.password = '',
                    errorSelector: 'password',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Confirmed password is required',
                    prepareInput: (input) => input.values.confirmedPassword = '',
                    errorSelector: 'confirmed-password',
                    expectedError: 'Valor requerido',
                },
            ];

            testCases.forEach(test => runInvalidCreateUserInputTest(test));
        })

        describe('max length validations', () => {
            const testCases = [
                {
                    testName: 'Name max length',
                    prepareInput: (input) => input.values.name = 'x'.repeat(61),
                    errorSelector: 'name',
                    expectedError: 'Máximo 60 caracteres',
                },
                {
                    testName: 'Last name max lenght',
                    prepareInput: (input) => input.values.lastName = 'x'.repeat(61),
                    errorSelector: 'last-name',
                    expectedError: 'Máximo 60 caracteres',
                },
                {
                    testName: 'District max length',
                    prepareInput: (input) => input.values.district = 'x'.repeat(61),
                    errorSelector: 'district',
                    expectedError: 'Máximo 60 caracteres',
                },
                {
                    testName: 'Street max length',
                    prepareInput: (input) => input.values.street = 'x'.repeat(61),
                    errorSelector: 'street',
                    expectedError: 'Máximo 60 caracteres',
                },
                {
                    testName: 'Street number max length',
                    prepareInput: (input) => input.values.streetNumber = 'x'.repeat(11),
                    errorSelector: 'street-number',
                    expectedError: 'Máximo 10 caracteres',
                },
                {
                    testName: 'Password max length',
                    prepareInput: (input) => input.values.password = 'x'.repeat(61),
                    errorSelector: 'password',
                    expectedError: 'Máximo 60 caracteres',
                },
                {
                    testName: 'Confirmed password max length',
                    prepareInput: (input) => input.values.confirmedPassword = 'x'.repeat(61),
                    errorSelector: 'confirmed-password',
                    expectedError: 'Máximo 60 caracteres',
                },
            ];

            testCases.forEach(test => runInvalidCreateUserInputTest(test));
        })

        describe('structure validations', () => {
            const testCases = [
                {
                    testName: 'Email must have a valid structure',
                    prepareInput: (input) => input.values.email = 'fake-email',
                    errorSelector: 'email',
                    expectedError: 'Correo inválido',
                },
                {
                    testName: 'Zip code must be exactly 5 digits',
                    prepareInput: (input) => input.values.zipCode = 'abcde',
                    errorSelector: 'zip-code',
                    expectedError: 'Deben ser 5 dígitos',
                },
                {
                    testName: 'Phone must be exactly 10 digits',
                    prepareInput: (input) => input.values.phone = 'abcdefghij',
                    errorSelector: 'phone',
                    expectedError: 'Deben ser 10 dígitos',
                },
            ];

            testCases.forEach(test => runInvalidCreateUserInputTest(test));
        })
    });
});

// ######################## Helper functions ########################

const validUser = () => {
    const password = faker.internet.password();
    return {
        name: faker.person.firstName(),
        lastName: faker.person.lastName(),
        district: faker.location.secondaryAddress(),
        street: faker.location.street(),
        streetNumber: faker.location.buildingNumber(),
        zipCode: faker.string.numeric(5),
        email: faker.internet.email(),
        phone: faker.string.numeric(10),
        password: password,
        confirmedPassword: password,
    }
}

function fillCreateUserForm({ values, selectRole }) {
    cy.get('#create-item-form').within(() => {
        type('input[name="user-name"]', values.name);
        type('input[name="user-last-name"]', values.lastName);
        type('input[name="user-district"]', values.district);
        type('input[name="user-street"]', values.street);
        type('input[name="user-street-number"]', values.streetNumber);
        type('input[name="user-zip-code"]', values.zipCode);
        type('input[name="user-email"]', values.email);
        type('input[name="user-phone"]', values.phone);
        type('input[name="user-password"]', values.password);
        type('input[name="user-confirmed-password"]', values.confirmedPassword);

        if (selectRole) {
            clickRandomDropdownValue('#create-user-role');
        }
    });
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
        cy.get(selector).clear({ force: true }).type(value, { force: true });
    }
};

function runInvalidCreateUserInputTest(test) {
    it(test.testName, () => {
        const input = {
            values: validUser(),
            selectRole: true
        };

        test.prepareInput(input);

        cy.get('#create-new').click();

        fillCreateUserForm({
            values: input.values,
            selectRole: input.selectRole
        })

        cy.get('#create-user-submit').click();

        cy.get(`.form-error.${test.errorSelector}`).should('contain', test.expectedError);
    })
}