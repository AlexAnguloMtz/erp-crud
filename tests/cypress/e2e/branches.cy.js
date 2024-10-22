import { testUserCredentials } from '../environment/environment';
import { faker } from '@faker-js/faker';
import { getCreateNewItemButton, getItemFormSubmitButton, getItemSavedDialog, getCloseSavedItemDialog, getCreationErrorDialog, getItemCreationForm } from '../helpers/crud-module-helpers';
import { getSideBarLink } from '../helpers/main-screen-helpers';
import { visitLoginPage, logIn } from '../helpers/login-helpers';
import { type } from '../helpers/form-helpers';

describe('branches module', () => {

    beforeEach(() => {
        visitLoginPage();

        logIn(testUserCredentials());

        getSideBarLink('#branches').click();

    });

    context('create branch', () => {
        it('branch is created successfully', () => {
            getCreateNewItemButton().click();

            fillCreateBranchForm(validBranch());

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
                    testName: 'Phone is required',
                    prepareInput: (input) => input.phone = '',
                    errorClass: 'phone',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Phone must be 10 digits',
                    prepareInput: (input) => input.phone = 'abcd',
                    errorClass: 'phone',
                    expectedError: 'Deben ser 10 dígitos',
                },
                {
                    testName: 'District is required',
                    prepareInput: (input) => input.district = '',
                    errorClass: 'district',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'District max length',
                    prepareInput: (input) => input.district = 'x'.repeat(61),
                    errorClass: 'district',
                    expectedError: 'Máximo 60 caracteres',
                },
                {
                    testName: 'Street is required',
                    prepareInput: (input) => input.street = '',
                    errorClass: 'street',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Street max length',
                    prepareInput: (input) => input.street = 'x'.repeat(61),
                    errorClass: 'street',
                    expectedError: 'Máximo 60 caracteres',
                },
                {
                    testName: 'Street number is required',
                    prepareInput: (input) => input.streetNumber = '',
                    errorClass: 'streetNumber',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Street number max length',
                    prepareInput: (input) => input.streetNumber = 'x'.repeat(11),
                    errorClass: 'streetNumber',
                    expectedError: 'Máximo 10 caracteres',
                },
                {
                    testName: 'Zip code is required',
                    prepareInput: (input) => input.zipCode = '',
                    errorClass: 'zipCode',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Zip code must be 5 digits',
                    prepareInput: (input) => input.zipCode = 'abcde',
                    errorClass: 'zipCode',
                    expectedError: 'Deben ser 5 dígitos',
                },
                {
                    testName: 'Branch type is required',
                    prepareInput: (input) => input.selectBranchType = false,
                    errorClass: 'branchType',
                    expectedError: 'Valor requerido',
                },
            ];

            testCases.forEach(test => runInvalidCreateBranchInputTest(test));
        })
    });
});

// ######################## Helper functions ########################

const validBranch = () => {
    return {
        name: faker.company.name(),
        phone: faker.string.numeric(10),
        district: faker.location.secondaryAddress(),
        street: faker.location.street(),
        streetNumber: faker.location.buildingNumber(),
        zipCode: faker.string.numeric(5),
        selectBranchType: true,
    }
}

function fillCreateBranchForm({
    name,
    phone,
    district,
    street,
    streetNumber,
    zipCode,
    selectBranchType,
}) {
    getItemCreationForm().within(() => {
        type('input[name="name"]', name);
        type('input[name="phone"]', phone);
        type('input[name="district"]', district);
        type('input[name="street"]', street);
        type('input[name="streetNumber"]', streetNumber);
        type('input[name="zipCode"]', zipCode);
    });

    if (selectBranchType) {
        clickRandomDropdownValue('#branch-type-dropdown');
    }
}

function runInvalidCreateBranchInputTest(test) {
    it(test.testName, () => {
        const input = validBranch();

        test.prepareInput(input);

        getCreateNewItemButton().click();

        fillCreateBranchForm(input);

        getItemFormSubmitButton().click();

        cy.get(`.form-error.${test.errorClass}`).should('contain', test.expectedError);
    })
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