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

const names = ['Juan', 'Ana', 'Carlos', 'María', 'Luis', 'Sofía'];
const lastNames = ['García', 'Martínez', 'Hernández', 'López', 'Pérez'];
const phones = ['5512345678', '5523456789', '5534567890', '5545678901', '5556789012', '5567890123', '5578901234', '5589012345'];
const cities = ['Ciudad de México', 'Guadalajara', 'Monterrey', 'Puebla', 'Cancún'];
const states = ['CDMX', 'JAL', 'NL', 'PUE', 'QR'];
const roles = ['Administrador', 'Usuario', 'Moderador', 'Invitado'];
const emails = ['usuario1@gmail.com', 'usuario2@gmail.com', 'usuario3@gmail.com', 'usuario4@gmail.com'];
const districts = ['Centro', 'Norte', 'Sur', 'Este', 'Oeste'];
const streets = ['Avenida Reforma', 'Calle Juárez', 'Avenida 16 de Septiembre', 'Calle Madero', 'Boulevard Ávila Camacho'];
const streetNumbers = ['100', '200', '300', '400', '500'];
const zipCodes = ['01000', '08000', '64000', '72000', '77500'];

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

const randomUsers = createRandomUserPreviews(150);

@Injectable({
    providedIn: 'root'
})
export class UsersService {
    getUsers(request: PaginatedRequest): Observable<PaginatedResponse<UserDetails>> {
        console.log(JSON.stringify(request));

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