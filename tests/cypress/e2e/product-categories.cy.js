import { testUserCredentials } from '../environment/environment';
import { faker } from '@faker-js/faker';
import { getCreateNewItemButton, getItemFormSubmitButton, getItemSavedDialog, getCloseSavedItemDialog, getCreationErrorDialog, getItemCreationForm } from '../helpers/crud-module-helpers';
import { getSideBarLink } from '../helpers/main-screen-helpers';
import { visitLoginPage, logIn } from '../helpers/login-helpers';
import { type } from '../helpers/form-helpers';

describe('product categories module', () => {

    beforeEach(() => {
        visitLoginPage();

        logIn(testUserCredentials());

        getSideBarLink('#product-categories').click();

    });

    context('create product category', () => {
        it('product category is created successfully', () => {
            getCreateNewItemButton().click();

            fillCreateProductCategoryForm(validProductCategory());

            getItemFormSubmitButton().click();

            getItemSavedDialog().should('contain', 'Se guardó el registro');
        });

        describe('field validations', () => {
            const testCases = [
                {
                    testName: 'Name is required',
                    prepareInput: (input) => input.name = '',
                    errorClass: 'name',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Name max length',
                    prepareInput: (input) => input.name = 'x'.repeat(61),
                    errorClass: 'name',
                    expectedError: 'Máximo 60 caracteres',
                },
            ];

            testCases.forEach(test => runInvalidCreateProductCategoryInputTest(test));
        })
    });
});

// ######################## Helper functions ########################

const validProductCategory = () => {
    return {
        name: faker.company.name(),
    }
}

function fillCreateProductCategoryForm({ name }) {
    getItemCreationForm().within(() => {
        type('input[name="name"]', name);
    });
}

function runInvalidCreateProductCategoryInputTest(test) {
    it(test.testName, () => {
        const input = validProductCategory();

        test.prepareInput(input);

        getCreateNewItemButton().click();

        fillCreateProductCategoryForm(input);

        getItemFormSubmitButton().click();

        cy.get(`.form-error.${test.errorClass}`).should('contain', test.expectedError);
    })
}