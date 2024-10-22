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