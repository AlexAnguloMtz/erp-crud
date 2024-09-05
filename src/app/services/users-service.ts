import { Injectable } from "@angular/core";
import { defer, delay, Observable, of, throwError } from "rxjs";
import { PaginatedResponse } from "../common/paginated-response";
import { PaginatedRequest } from "../common/paginated-request";

export type CreateUserCommand = {
    name: string,
    lastName: string,
    state: string,
    city: string,
    district: string,
    street: string,
    streetNumber: string,
    zipCode: string,
    email: string,
    phone: string,
    roleId: string,
}

export type UserDetails = {
    id: string,
    name: string,
    lastName: string,
    state: string,
    city: string,
    district: string,
    street: string,
    streetNumber: string,
    zipCode: string,
    email: string,
    phone: string,
    role: string,
}

const names = ['John', 'Jane', 'Alice', 'Bob', 'Charlie', 'Diana'];
const lastNames = ['Smith', 'Doe', 'Johnson', 'Williams', 'Jones'];
const phones = ['555-1234', '555-5678', '555-8765', '555-4321'];
const cities = ['New York', 'Los Angeles', 'Chicago', 'Houston', 'Phoenix'];
const states = ['NY', 'CA', 'IL', 'TX', 'AZ'];
const roles = ['Admin', 'User', 'Moderator', 'Guest'];
const emails = ['firstemail@gmail.com', 'secondemail@gmail.com', 'thirdemail@gmail.com', 'fourthemail@gmail.com'];

// Added arrays for missing fields
const districts = ['Downtown', 'Midtown', 'Uptown', 'Suburb', 'Countryside'];
const streets = ['Main St', 'Elm St', 'Maple Ave', 'Oak Dr', 'Pine Rd'];
const streetNumbers = ['101', '202', '303', '404', '505'];
const zipCodes = ['10001', '90001', '60601', '77001', '85001'];

function getRandomItem<T>(items: T[]): T {
    const randomIndex = Math.floor(Math.random() * items.length);
    return items[randomIndex];
}

function createRandomUserPreview(id: string): UserDetails {
    return {
        id,
        name: getRandomItem(names),
        lastName: getRandomItem(lastNames),
        phone: getRandomItem(phones),
        city: getRandomItem(cities),
        state: getRandomItem(states),
        role: getRandomItem(roles),
        email: getRandomItem(emails),
        district: getRandomItem(districts),
        street: getRandomItem(streets),
        streetNumber: getRandomItem(streetNumbers),
        zipCode: getRandomItem(zipCodes),
    };
}

export function createRandomUserPreviews(amount: number): UserDetails[] {
    const userPreviews: UserDetails[] = [];
    for (let i = 0; i < amount; i++) {
        userPreviews.push(createRandomUserPreview(String(i)));
    }
    return userPreviews;
}

@Injectable({
    providedIn: 'root'
})
export class UsersService {
    getUsers(request: PaginatedRequest): Observable<PaginatedResponse<UserDetails>> {
        console.log(JSON.stringify(request));

        const randomUsers = createRandomUserPreviews(150);

        const filteredUsers = filter(randomUsers, request);

        const totalItems = filteredUsers.length;
        const pageNumber = request.pageNumber ?? 0;
        const pageSize = request.pageSize ?? 15;

        const totalPages = Math.ceil(totalItems / pageSize);
        const start = pageNumber * pageSize;
        const end = start + pageSize;
        const items = filteredUsers.slice(start, end);

        const response: PaginatedResponse<UserDetails> = {
            pageNumber,
            pageSize,
            totalPages,
            totalItems,
            isLastPage: pageNumber >= totalPages - 1,
            items
        };

        return of(response).pipe(delay(2000));
        // return defer(() => {
        //    return throwError(() => new UserExistsError("UserExists")).pipe(delay(2000));
        //});
    }

    createUser(command: CreateUserCommand): Observable<boolean> {
        console.log('creating user ' + JSON.stringify(command));
        return of(true).pipe(delay(2000));
    }
}

function filter(users: UserDetails[], request: PaginatedRequest): UserDetails[] {
    let filtered = [...users];

    // Apply search filter if the search property is defined and not empty
    if (request.search) {
        const searchTerm = request.search.toLowerCase();
        filtered = filtered.filter(x =>
            (x.name && x.name.toLowerCase().includes(searchTerm)) ||
            (x.lastName && x.lastName.toLowerCase().includes(searchTerm)) ||
            (x.email && x.email.toLowerCase().includes(searchTerm))
        );
    }

    return filtered;
}