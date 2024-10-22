import { testUserCredentials } from '../environment/environment';
import { faker } from '@faker-js/faker';
import { getCreateNewItemButton, getItemFormSubmitButton, getItemSavedDialog, getCloseSavedItemDialog, getCreationErrorDialog, getItemCreationForm } from '../helpers/crud-module-helpers';
import { getSideBarLink } from '../helpers/main-screen-helpers';
import { visitLoginPage, logIn } from '../helpers/login-helpers';
import { type } from '../helpers/form-helpers';

describe('brands module', () => {

    beforeEach(() => {
        visitLoginPage();

        logIn(testUserCredentials());

        getSideBarLink('#brands').click();

    });

    context('create brand', () => {
        it('brand is created successfully', () => {
            getCreateNewItemButton().click();

            fillCreateBrandForm(validBrand());

            getItemFormSubmitButton().click();

            getItemSavedDialog().should('contain', 'Se guardó el registro');
        });

        describe('field validations', () => {
            const testCases = [
                {
                    testName: 'Name is required',
                    prepareInput: (input) => input.name = '',
                    errorSelector: 'name',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Name max length',
                    prepareInput: (input) => input.name = 'x'.repeat(61),
                    errorSelector: 'name',
                    expectedError: 'Máximo 60 caracteres',
                },
            ];

            testCases.forEach(test => runInvalidCreateBrandInputTest(test));
        })
    });
});

// ######################## Helper functions ########################

const validBrand = () => {
    return {
        name: faker.company.name(),
    }
}

function fillCreateBrandForm({ name }) {
    getItemCreationForm().within(() => {
        type('input[name="name"]', name);
    });
}

function runInvalidCreateBrandInputTest(test) {
    it(test.testName, () => {
        const input = validBrand();

        test.prepareInput(input);

        getCreateNewItemButton().click();

        fillCreateBrandForm(input);

        getItemFormSubmitButton().click();

        cy.get(`.form-error.${test.errorSelector}`).should('contain', test.expectedError);
    })
}