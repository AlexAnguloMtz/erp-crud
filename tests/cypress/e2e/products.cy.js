import { testUserCredentials } from '../environment/environment';
import { faker } from '@faker-js/faker';
import { getCreateNewItemButton, getItemFormSubmitButton, getItemSavedDialog, getCloseSavedItemDialog, getCreationErrorDialog, getItemCreationForm } from '../helpers/crud-module-helpers';
import { getSideBarLink } from '../helpers/main-screen-helpers';
import { visitLoginPage, logIn } from '../helpers/login-helpers';
import { type } from '../helpers/form-helpers';

describe('products module', () => {

    beforeEach(() => {
        visitLoginPage();

        logIn(testUserCredentials());

        getSideBarLink('#products').click();

    });

    context('product form', () => {
        it('product is saved successfully', () => {
            createProduct();
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
                    testName: 'SKU is required',
                    prepareInput: (input) => input.sku = '',
                    errorClass: 'sku',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'SKU must have an exact length',
                    prepareInput: (input) => input.sku = 'abcde',
                    errorClass: 'sku',
                    expectedError: 'Deben ser 8 caracteres',
                },
                {
                    testName: 'Sale price is required',
                    prepareInput: (input) => input.salePrice = '',
                    errorClass: 'salePrice',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Sale price must be a valid quantity',
                    prepareInput: (input) => input.salePrice = 'abcde',
                    errorClass: 'salePrice',
                    expectedError: 'Precio no válido',
                },
                {
                    testName: 'Sale price cannot be zero',
                    prepareInput: (input) => input.salePrice = '0',
                    errorClass: 'salePrice',
                    expectedError: 'Precio no válido',
                },
                {
                    testName: 'Sale price cannot be negative',
                    prepareInput: (input) => input.salePrice = '-10.00',
                    errorClass: 'salePrice',
                    expectedError: 'Precio no válido',
                },
                {
                    testName: 'Sale price cannot be too big',
                    prepareInput: (input) => input.salePrice = '250000000.00',
                    errorClass: 'salePrice',
                    expectedError: 'Precio no válido',
                },
                {
                    testName: 'Brand is required',
                    prepareInput: (input) => input.selectBrand = false,
                    errorClass: 'brand',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Product category is required',
                    prepareInput: (input) => input.selectProductCategory = false,
                    errorClass: 'productCategory',
                    expectedError: 'Valor requerido',
                },
                {
                    testName: 'Inventory unit is required',
                    prepareInput: (input) => input.selectInventoryUnit = false,
                    errorClass: 'inventoryUnit',
                    expectedError: 'Valor requerido',
                },
            ];

            testCases.forEach(test => runInvalidCreateProductInputTest(test));
        })
    });
});

// ######################## Helper functions ########################

function createProduct() {
    getCreateNewItemButton().click();

    const product = validProduct();

    fillProductForm(product);

    getItemFormSubmitButton().click();

    getItemSavedDialog().should('contain', 'Se guardó el registro');

    return product;
}

function validProduct() {
    return {
        name: faker.commerce.product(),
        sku: faker.string.alphanumeric(8).toUpperCase(),
        salePrice: validSalePrice(),
        selectBrand: true,
        selectProductCategory: true,
        selectInventoryUnit: true,
    }
}

function validSalePrice() {
    const integerPortion = faker.number.int({ min: 1, max: 100000 });
    const firstDecimal = faker.number.int({ min: 0, max: 9 });
    const secondDecimal = faker.number.int({ min: 0, max: 9 });
    const decimalPortion = `${firstDecimal}${secondDecimal}`;
    return `${integerPortion}.${decimalPortion}`;
}

function fillProductForm({
    name,
    sku,
    salePrice,
    selectBrand,
    selectProductCategory,
    selectInventoryUnit,
}) {
    getItemCreationForm().within(() => {
        type('input[name="name"]', name);
        type('input[name="sku"]', sku);
        type('input[name="salePrice"]', salePrice);
    });

    if (selectBrand) {
        clickRandomDropdownValue('#brand-dropdown');
    }

    if (selectProductCategory) {
        clickRandomDropdownValue('#product-category-dropdown');
    }

    if (selectInventoryUnit) {
        clickRandomDropdownValue('#inventory-unit-dropdown');
    }
}

function getEditProductButton(sku) {

}

function runInvalidCreateProductInputTest(test) {
    it(test.testName, () => {
        const input = validProduct();

        test.prepareInput(input);

        getCreateNewItemButton().click();

        fillProductForm(input);

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