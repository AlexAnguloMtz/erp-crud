import { Injectable } from "@angular/core";
import { defer, delay, Observable, of, throwError } from "rxjs";
import { PaginatedResponse } from "../common/paginated-response";

export type UserPreview = {
    name: string
    lastName: string
    email: string
    phone: string
    city: string
    state: string
    role: string
}

const names = ['John', 'Jane', 'Alice', 'Bob', 'Charlie', 'Diana'];
const lastNames = ['Smith', 'Doe', 'Johnson', 'Williams', 'Jones'];
const phones = ['555-1234', '555-5678', '555-8765', '555-4321'];
const cities = ['New York', 'Los Angeles', 'Chicago', 'Houston', 'Phoenix'];
const states = ['NY', 'CA', 'IL', 'TX', 'AZ'];
const roles = ['Admin', 'User', 'Moderator', 'Guest'];
const emails = ['firstemail@gmail.com', 'secondemail@gmail.com', 'thirdemail@gmail.com', 'fourthemail@gmail.com'];

function getRandomItem<T>(items: T[]): T {
    const randomIndex = Math.floor(Math.random() * items.length);
    return items[randomIndex];
}

function createRandomUserPreview(): UserPreview {
    return {
        name: getRandomItem(names),
        lastName: getRandomItem(lastNames),
        phone: getRandomItem(phones),
        city: getRandomItem(cities),
        state: getRandomItem(states),
        role: getRandomItem(roles),
        email: getRandomItem(emails),
    };
}

export function createRandomUserPreviews(amount: number): UserPreview[] {
    const userPreviews: UserPreview[] = [];
    for (let i = 0; i < amount; i++) {
        userPreviews.push(createRandomUserPreview());
    }
    return userPreviews;
}


@Injectable({
    providedIn: 'root'
})
export class UsersService {
    getUsers(): Observable<PaginatedResponse<UserPreview>> {
        const randomUsers = createRandomUserPreviews(100)

        const response: PaginatedResponse<UserPreview> = {
            pageNumber: 0,
            pageSize: 10,
            totalPages: 20,
            totalItems: 100,
            isLastPage: false,
            items: randomUsers,
        }

        return of(response)
            .pipe(delay(2000));
        // return defer(() => {
        //    return throwError(() => new UserExistsError("UserExists")).pipe(delay(2000));
        //});
    }

}