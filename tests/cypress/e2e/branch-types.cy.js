import { testUserCredentials } from '../environment/environment';
import { faker } from '@faker-js/faker';
import { getCreateNewItemButton, getItemFormSubmitButton, getItemSavedDialog, getCloseSavedItemDialog, getCreationErrorDialog, getItemCreationForm } from '../helpers/crud-module-helpers';
import { getSideBarLink } from '../helpers/main-screen-helpers';
import { visitLoginPage, logIn } from '../helpers/login-helpers';
import { type } from '../helpers/form-helpers';

describe('branch types module', () => {

    beforeEach(() => {
        visitLoginPage();

        logIn(testUserCredentials());

        getSideBarLink('#branch-types').click();

    });

    context('create branch type', () => {
        it('branch type is created successfully', () => {
            getCreateNewItemButton().click();

            fillCreateBranchTypeForm(validBranchType());

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
                {
                    testName: 'Description is required',
                    prepareInput: (input) => input.description = '',
                    errorClass: 'description',
                    expectedError: 'Valor requerido',
                },
            ];

            testCases.forEach(test => runInvalidCreateBranchTypeInputTest(test));
        })
    });
});

// ######################## Helper functions ########################

const validBranchType = () => {
    return {
        name: faker.company.name(),
        description: faker.company.buzzAdjective(),
    }
}

function fillCreateBranchTypeForm({ name, description }) {
    getItemCreationForm().within(() => {
        type('input[name="name"]', name);
        type('textarea[name="description"]', description);
    });
}

function runInvalidCreateBranchTypeInputTest(test) {
    it(test.testName, () => {
        const input = validBranchType();

        test.prepareInput(input);

        getCreateNewItemButton().click();

        fillCreateBranchTypeForm(input);

        getItemFormSubmitButton().click();

        cy.get(`.form-error.${test.errorClass}`).should('contain', test.expectedError);
    })
}