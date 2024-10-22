import { testUserCredentials } from '../environment/environment';
import { faker } from '@faker-js/faker';
import { getCreateNewItemButton, getItemFormSubmitButton, getItemSavedDialog, getCloseSavedItemDialog, getCreationErrorDialog, getItemCreationForm } from '../helpers/crud-module-helpers';
import { getSideBarLink } from '../helpers/main-screen-helpers';
import { visitLoginPage, logIn } from '../helpers/login-helpers';
import { type } from '../helpers/form-helpers';

describe('users module', () => {
    beforeEach(() => {
        visitLoginPage();

        logIn(testUserCredentials());

        getSideBarLink('#users').click();
    });

    context('create user', () => {
        it('user is created successfully', () => {
            getCreateNewItemButton().click();

            fillCreateUserForm({
                values: validUser(),
                selectRole: true
            });

            getItemFormSubmitButton().click();

            getItemSavedDialog().should('contain', 'Se guardó el registro');
        });

        it('two users cannot have the same email', () => {
            // Save first user
            const firstUser = validUser();

            getCreateNewItemButton().click();

            fillCreateUserForm({
                values: firstUser,
                selectRole: true
            });

            getItemFormSubmitButton().click();

            getItemSavedDialog().should('contain', 'Se guardó el registro');

            getCloseSavedItemDialog().click();

            // Try to save second user with same email
            const secondUser = validUser();

            secondUser.email = firstUser.email;

            getCreateNewItemButton().click();

            fillCreateUserForm({
                values: firstUser,
                selectRole: true
            });

            getItemFormSubmitButton().click();

            // Check error message for duplicated email
            getCreationErrorDialog().within(() => {
                cy.contains('Usuario ya existe').should('exist');
                cy.contains('El correo ya pertenece a otro usuario').should('exist');
            });
        })

        describe('empty fields validations', () => {
            const testCases = [
                {
                    testName: 'Name is required',
                    prepareInput: (input) => input.values.name = '',
                    errorClass: 'name',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Last name is required',
                    prepareInput: (input) => input.values.lastName = '',
                    errorClass: 'last-name',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'District is required',
                    prepareInput: (input) => input.values.district = '',
                    errorClass: 'district',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Street is required',
                    prepareInput: (input) => input.values.street = '',
                    errorClass: 'street',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Street number is required',
                    prepareInput: (input) => input.values.streetNumber = '',
                    errorClass: 'street-number',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Phone is required',
                    prepareInput: (input) => input.values.phone = '',
                    errorClass: 'phone',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Zip code is required',
                    prepareInput: (input) => input.values.zipCode = '',
                    errorClass: 'zip-code',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Email is required',
                    prepareInput: (input) => input.values.email = '',
                    errorClass: 'email',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Role is required',
                    prepareInput: (input) => input.selectRole = false,
                    errorClass: 'role',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Password is required',
                    prepareInput: (input) => input.values.password = '',
                    errorClass: 'password',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Confirmed password is required',
                    prepareInput: (input) => input.values.confirmedPassword = '',
                    errorClass: 'confirmed-password',
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
                    errorClass: 'name',
                    expectedError: 'Máximo 60 caracteres',
                },
                {
                    testName: 'Last name max lenght',
                    prepareInput: (input) => input.values.lastName = 'x'.repeat(61),
                    errorClass: 'last-name',
                    expectedError: 'Máximo 60 caracteres',
                },
                {
                    testName: 'District max length',
                    prepareInput: (input) => input.values.district = 'x'.repeat(61),
                    errorClass: 'district',
                    expectedError: 'Máximo 60 caracteres',
                },
                {
                    testName: 'Street max length',
                    prepareInput: (input) => input.values.street = 'x'.repeat(61),
                    errorClass: 'street',
                    expectedError: 'Máximo 60 caracteres',
                },
                {
                    testName: 'Street number max length',
                    prepareInput: (input) => input.values.streetNumber = 'x'.repeat(11),
                    errorClass: 'street-number',
                    expectedError: 'Máximo 10 caracteres',
                },
                {
                    testName: 'Password max length',
                    prepareInput: (input) => input.values.password = 'x'.repeat(61),
                    errorClass: 'password',
                    expectedError: 'Máximo 60 caracteres',
                },
                {
                    testName: 'Confirmed password max length',
                    prepareInput: (input) => input.values.confirmedPassword = 'x'.repeat(61),
                    errorClass: 'confirmed-password',
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
                    errorClass: 'email',
                    expectedError: 'Correo inválido',
                },
                {
                    testName: 'Zip code must be exactly 5 digits',
                    prepareInput: (input) => input.values.zipCode = 'abcde',
                    errorClass: 'zip-code',
                    expectedError: 'Deben ser 5 dígitos',
                },
                {
                    testName: 'Phone must be exactly 10 digits',
                    prepareInput: (input) => input.values.phone = 'abcdefghij',
                    errorClass: 'phone',
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
    getItemCreationForm().within(() => {
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
            clickRandomDropdownValue('#user-role-dropdown');
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

function runInvalidCreateUserInputTest(test) {
    it(test.testName, () => {
        const input = {
            values: validUser(),
            selectRole: true
        };

        test.prepareInput(input);

        getCreateNewItemButton().click();

        fillCreateUserForm({
            values: input.values,
            selectRole: input.selectRole
        })

        getItemFormSubmitButton().click();

        cy.get(`.form-error.${test.errorClass}`).should('contain', test.expectedError);
    })
}