import { Injectable } from "@angular/core";
import { defer, delay, Observable, of, throwError } from "rxjs";
import { PaginatedResponse } from "../common/paginated-response";
import { PaginatedRequest } from "../common/paginated-request";
import { Role } from "./auth-service";

type Location = {
    state: string,
    city: string,
}

const locations: Location[] = [
    { state: 'CDMX', city: 'Ciudad de México' },
    { state: 'Jalisco', city: 'Guadalajara' },
    { state: 'Nuevo León', city: 'Monterrey' },
    { state: 'Puebla', city: 'Puebla' },
    { state: 'Quintana Roo', city: 'Cancún' },
    { state: 'Tamaulipas', city: 'Tampico' },
    { state: 'Baja California', city: 'Tijuana' },
    { state: 'Michoacán', city: 'Morelia' },
    { state: 'Veracruz', city: 'Veracruz' },
    { state: 'Sonora', city: 'Hermosillo' },
    { state: 'San Luis Potosí', city: 'San Luis Potosí' },
    { state: 'Yucatán', city: 'Mérida' },
    { state: 'Oaxaca', city: 'Oaxaca' },
    { state: 'Aguascalientes', city: 'Aguascalientes' },
    { state: 'Durango', city: 'Durango' },
    { state: 'Colima', city: 'Colima' },
    { state: 'Chihuahua', city: 'Chihuahua' },
    { state: 'Nayarit', city: 'Tepic' },
    { state: 'Guerrero', city: 'Acapulco' },
    { state: 'Zacatecas', city: 'Zacatecas' },
    { state: 'Tlaxcala', city: 'Tlaxcala' }
];

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
    role: Role,
}

const names = ['Juan', 'Ana', 'Carlos', 'María', 'Luis', 'Sofía'];
const lastNames = ['García', 'Martínez', 'Hernández', 'López', 'Pérez'];
const phones = ['5512345678', '5523456789', '5534567890', '5545678901', '5556789012', '5567890123', '5578901234', '5589012345'];

const roles = [
    {
        id: '1',
        description: 'Super Usuario',
    },
    {
        id: '2',
        description: 'Administrador',
    },
    {
        id: '3',
        description: 'Gerente',
    },
    {
        id: '4',
        description: 'Usuario',
    },
];

const emailProviders = ['gmail', 'yahoo', 'yandex', 'outlook'];
const districts = ['Centro', 'Norte', 'Sur', 'Este', 'Oeste'];
const streets = ['Avenida Reforma', 'Calle Juárez', 'Avenida 16 de Septiembre', 'Calle Madero', 'Boulevard Ávila Camacho'];
const streetNumbers = ['100', '200', '300', '400', '500'];
const zipCodes = ['01000', '08000', '64000', '72000', '77500'];

function getRandomItem<T>(items: T[]): T {
    const randomIndex = Math.floor(Math.random() * items.length);
    return items[randomIndex];
}

function removeAccents(str: string): string {
    const accentsMap: { [key: string]: string } = {
        'á': 'a', 'é': 'e', 'í': 'i', 'ó': 'o', 'ú': 'u', 'ü': 'u',
        'Á': 'A', 'É': 'E', 'Í': 'I', 'Ó': 'O', 'Ú': 'U', 'Ü': 'U',
        'ñ': 'n', 'Ñ': 'N'
    };

    return str.replace(/[áéíóúüÁÉÍÓÚÜñÑ]/g, match => accentsMap[match] || match);
}


function createRandomUserPreview(id: string): UserDetails {
    const location = getRandomItem(locations);
    const name = getRandomItem(names)
    const lastName = getRandomItem(lastNames)
    const emailProvider = getRandomItem(emailProviders)
    const email = name.toLocaleLowerCase() + lastName.toLocaleLowerCase() + "@" + emailProvider + ".com"
    return {
        id,
        name: getRandomItem(names),
        lastName: getRandomItem(lastNames),
        phone: getRandomItem(phones),
        city: location.city,
        state: location.state,
        role: getRandomItem(roles),
        email: removeAccents(email),
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